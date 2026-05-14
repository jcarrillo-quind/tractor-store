# The Tractor Store — Frontend (monorepo Nx)

Workspace **Angular** dentro del repositorio **tractor-store**. Aquí vivirán la **aplicación shell** y, más adelante, los **micro-frontends** alineados con los equipos del producto.

---
- **Nx** + aplicación **`shell`** (Angular), bundler **Rspack** según la configuración del proyecto.
- **E2E** base con **Playwright** (`shell-e2e`, spec de ejemplo).
- **Contenido de producto aún no implementado:** la app sigue siendo el esqueleto típico (p. ej. componente de bienvenida Nx); no hay pantallas de catálogo, carrito ni checkout conectadas al API.

Comandos útiles:

```sh
# Servidor de desarrollo del shell
npx nx serve shell

# Build de producción
npx nx build shell

# Grafo de proyectos
npx nx graph

# Proyecto E2E Playwright (shell-e2e) — listar targets disponibles
npx nx show project shell-e2e
```

---

## Próximos pasos (resumen)

- Definir shell/CORS y cliente HTTP hacia el backend cuando exista la API REST.
- Extraer o añadir **micro-frontends** por equipo y rutas federadas o por módulos, según la decisión de arquitectura del hackatón.
- Sustituir la pantalla placeholder por flujos reales alineados con los endpoints de catálogo, inventario, carrito y pedidos.

---

## Nx — referencia rápida

- [Documentación Nx](https://nx.dev)
- [Tutorial monorepo Angular](https://nx.dev/getting-started/tutorials/angular-monorepo-tutorial)
- [Nx Console](https://nx.dev/getting-started/editor-setup)

Para **CI con Nx Cloud** y plantillas de workflow: `npx nx connect`, `npx nx g ci-workflow`.
