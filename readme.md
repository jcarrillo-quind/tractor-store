## Qué vamos a construir

**The Tractor Store** es un e-commerce de tractores. La especificación funcional es compartida; el stack concreto es el de este repo (Angular en el front, API en el back).

### Equipos y responsabilidades

| Equipo | Alcance en el sitio | API que consume |
|--------|----------------------|-----------------|
| **Team Explore** | Home, listados, tiendas, recomendaciones | `catalog/*` |
| **Team Decide** | PDP, variantes, *out of stock* | `catalog/products`, `inventory/{sku}` |
| **Team Checkout** | Carrito, mini-cart, checkout, *thanks* | `cart/*`, `orders/*`, `catalog/stores` |

### Contrato API

- Respuestas **JSON**; errores `{"code", "message", "details"?}` (400, 404, 409, 500).
- Carrito: cookie **HttpOnly** `TRACTOR_CART_SESSION` (7 días, `SameSite=Lax`, path `/`). El front debe enviar credenciales (`withCredentials: true` en Angular).
- Imágenes: campo **`imageUrl`** (URL absoluta) en categorías del home, variantes, tiendas y líneas de carrito/pedido (`label` + `imageUrl` en `lines[]`).

---

## Estado actual

### Backend (`backend/`)

**Java 21** · **Maven** · **Spring Boot 3.4.x** · dominio en arquitectura limpia + REST MVC por módulo.

| Fase | Contenido |
|------|-----------|
| **1 — Dominio** | Módulos `catalog`, `store`, `inventory`, `cart`, `order`, `bootstrap`; puertos + adaptadores en memoria; `seed-data.json` |
| **B2 — Calidad** | JUnit 5, AssertJ, Mockito, Spotless (`./mvnw spotless:apply`) |
| **3a — REST (esqueleto)** | Spring Boot, CORS dev, catálogo base (home, categorías, producto, tiendas) |
| **3b — REST (API completa)** | Recomendaciones, inventario, carrito con sesión por cookie, pedidos |
| **4 — Persistencia** | PostgreSQL + Flyway; catálogo, tiendas e inventario en BD; carrito/pedidos siguen en memoria |
| **4b — Persistencia + medios** | `PersistenceConfiguration` (postgres / inmemory); `image_url` en BD; API con `imageUrl` |

#### Fase 3b — Entregables de esta ampliación

- **Catálogo:** `GET /api/catalog/recommendations?skus={csv}&limit=` (dominio RGB, límite default 8).
- **Inventario:** `GET /api/inventory/{sku}`.
- **Carrito:** `GET /api/cart`, `GET /api/cart/mini`, `POST /api/cart/items`, `DELETE /api/cart/items/{sku}`; validación de SKU y stock (409 si agotado).
- **Pedidos:** `POST /api/orders`, `GET /api/orders/{id}`; vacía el carrito de la sesión al confirmar.
- **Infra:** `CartSessionSupport`, `CartSessionProperties`, `InMemoryCartSessionRepository`; servicios delegan en casos de uso del dominio.
- **Tests:** `InventoryControllerTest`, `CartAndOrderControllerTest`; ampliación de `CatalogControllerTest` (recomendaciones). **20 tests** en `./mvnw verify`.

#### Todos los endpoints (implementados)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/catalog/home` | Teasers por categoría |
| GET | `/api/catalog/categories/{filter}` | Listado + filtros disponibles |
| GET | `/api/catalog/products/{id}` | Detalle PDP |
| GET | `/api/catalog/recommendations?skus=&limit=` | Variantes por cercanía de color |
| GET | `/api/catalog/stores` | Tiendas de recogida |
| GET | `/api/inventory/{sku}` | `availableUnits`, `inStock` |
| GET | `/api/cart` | Estado completo; emite cookie si falta |
| GET | `/api/cart/mini` | `{ itemCount }` |
| POST | `/api/cart/items` | `{ "sku" }` |
| DELETE | `/api/cart/items/{sku}` | Elimina línea |
| POST | `/api/orders` | `{ customerName, customerEmail, pickupStoreId }` |
| GET | `/api/orders/{id}` | Confirmación (*thanks*) |

Respuestas de catálogo, tiendas, carrito y pedidos incluyen **`imageUrl`** donde hay imagen (ver fase 4b).

#### Fase 4 — PostgreSQL

```sh
docker compose up -d       # desde la raíz del repo
cd backend && ./mvnw spring-boot:run
```

Flyway aplica `V1__schema.sql`, `V2__seed_data.sql` y `V3__image_urls.sql`. Catálogo, tiendas e inventario leen de **PostgreSQL**; carrito y pedidos siguen en memoria.

**Switch de persistencia** (`application.yml` → `tractor-store.persistence.mode`):

| Modo | Arranque | Fuente de datos |
|------|----------|-----------------|
| `postgres` (default) | `./mvnw spring-boot:run` + Postgres | JPA + Flyway |
| `inmemory` | `./mvnw spring-boot:run -Dspring-boot.run.profiles=inmemory` | `seed-data.json` (sin Docker) |

`PersistenceConfiguration` expone los mismos puertos (`CatalogReadPort`, `StoreReadPort`, `InventoryReadPort`) en ambos modos; los servicios REST no conocen el adaptador concreto.

#### Fase 4b — URLs de imagen

- Columnas `image_url` en `categories`, `variants`, `stores` (URL completa en BD).
- Semilla alineada con el CDN del [blueprint](https://blueprint.the-tractor.store/) (`scene/500/`, `product/200/`, `store/200/`).
- El API devuelve `imageUrl` en home, PDP, listados, recomendaciones, tiendas, carrito y pedidos.

#### Comandos (`backend/`)

```sh
./mvnw verify              # dominio siempre; REST si hay Docker o TRACTOR_DB_URL
./mvnw spotless:apply
./mvnw spring-boot:run     # postgres: requiere localhost:5432
./mvnw spring-boot:run -Dspring-boot.run.profiles=inmemory   # sin Postgres
```

#### Ejemplos curl

```sh
curl http://localhost:8080/api/catalog/home
# featuredCategories[].imageUrl, teasers[].variants[].imageUrl

curl http://localhost:8080/api/catalog/products/agri-classic-100
# variants[].imageUrl

curl "http://localhost:8080/api/catalog/recommendations?skus=AGR-100-RED,MIN-20-RED"
curl http://localhost:8080/api/inventory/AGR-100-GREEN
curl http://localhost:8080/api/catalog/stores
curl -c cookies.txt http://localhost:8080/api/cart
curl -b cookies.txt -H "Content-Type: application/json" \
  -d '{"sku":"AGR-100-RED"}' http://localhost:8080/api/cart/items
# lines[].imageUrl, lines[].label
curl -b cookies.txt -H "Content-Type: application/json" \
  -d '{"customerName":"Ana","customerEmail":"a@t.com","pickupStoreId":"store-bogota-norte"}' \
  http://localhost:8080/api/orders
```

Arquitectura, paquetes y decisiones: [context.md](context.md).

### Frontend (`frontend/`)

- **Nx** + app **`shell`** (Angular, Rspack); Playwright de ejemplo.
- Backend con **API completa**, CORS, cookies y **`imageUrl`** en respuestas de catálogo/carrito.
- **Pendiente:** UI (home, listados, PDP, tiendas, carrito), modelos TS con `imageUrl`, `HttpClient` con `withCredentials`.

```sh
npx nx serve shell
```

Más detalle: [frontend/README.md](frontend/README.md).
