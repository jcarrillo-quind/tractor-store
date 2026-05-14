## Qué vamos a construir

**The Tractor Store** es un e-commerce de tractores. La especificación funcional es compartida; el stack concreto es el de este repo (Angular en el front, API en el back).

### Equipos y responsabilidades

| Equipo | Alcance en el sitio |
|--------|----------------------|
| **Team Explore** | Home, listados por categoría, tiendas, recomendaciones, cabecera y pie. |
| **Team Decide** | Ficha de producto, selector de variantes (SKU/color), estado *out of stock* según inventario. |
| **Team Checkout** | Carrito, *mini-cart*, checkout con formulario, confirmación de pedido (*thanks*). |

### API prevista (orientación)

Todas las respuestas en **JSON**; el **carrito** se mantendrá con **cookies de sesión** (las operaciones de escritura del carrito mutan la cookie).

- **Catálogo:** home/teasers, categorías con filtros, detalle de producto, recomendaciones por color (CSV de SKUs), listado de tiendas físicas.
- **Inventario:** stock por SKU.
- **Carrito:** carrito completo, resumen *mini*, alta y baja de líneas por SKU.
- **Pedidos:** creación de pedido (datos personales + tienda de recogida) y consulta por id.

En fases posteriores el front consumirá estos endpoints desde el shell o desde MFEs dedicados (`mfe-explore`, `mfe-decide`, `mfe-checkout`).

---

## Estado actual — Fase 1

### Backend (`backend/`)

Primera fase **sin Spring**: dominio y casos de uso en **Java 21** puro, organización al estilo **Spring Modulith** (un paquete por módulo de negocio) y **arquitectura limpia** por módulo:

- **entities** — modelo de dominio.
- **usecases** — casos de uso y **ports** (interfaces).
- **adapters** — infraestructura de esta fase (memoria, carga de datos).

**Módulos:** `catalog`, `store`, `inventory`, `cart`, `order`, más `bootstrap` para componer el `SeedBundle` desde **JSON** (`seed-data.json`).

**Incluye:** recomendaciones por distancia de color en RGB, filtrado por categoría, búsqueda de producto por id, demos de carrito/pedido en dominio, tests de catálogo y un `main` de demostración (`TractorStoreDemo`).

Para detalle técnico del back, revisa el `pom.xml` y el árbol bajo `backend/src/main/java/com/tractorstore/`.

### Frontend (`frontend/`)

Monorepo **Nx** pensado para la **app shell** en Angular y, más adelante, los MFEs por equipo.

**Qué hay hoy**

- Aplicación **`shell`**: Angular con configuración **Rspack** (`apps/shell`).
- **Lint** (ESLint) y **tests unitarios** (Jest) en el proyecto `shell`, según los targets Nx (`npx nx show project shell`).
- Proyecto **`shell-e2e`**: **Playwright** con un spec de ejemplo; los targets concretos se listan con `npx nx show project shell-e2e`.

**Qué falta (fase 1)**

- No hay aún UI de producto: rutas y pantallas de catálogo, PDP, carrito, checkout ni integración HTTP con el backend.
- Pendiente definir consumo de API (cliente, CORS, cookies de sesión del carrito cuando exista el API REST).

**Comandos** (desde `frontend/`)

```sh
npx nx serve shell
npx nx build shell
npx nx graph
```

Documentación ampliada del front: [frontend/README.md](frontend/README.md).