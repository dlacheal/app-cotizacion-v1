# CotizacionApp (Frontend con Autenticación JWT y Módulo de Estadísticas)

Este es el frontend del sistema de cotizaciones, desarrollado en **Angular 21** y estilizado con **Bootstrap**.  
Incluye una integración completa con el backend seguro para inicio de sesión, almacenamiento local del token, envío automático de credenciales en cada petición HTTP, y un avanzado módulo de estadísticas y reportes ejecutivos.

---

## Módulo de Estadísticas (Sidebar)

Se ha incorporado una nueva categoría en el menú lateral orientada a la analítica de negocio:

### 1. Dashboard Ejecutivo (`/dashboard`)
* **KPI Cards:** Resumen consolidador de métricas críticas (Total Cotizado, Costo de Inversión, Utilidad Bruta Estimada y Tasa de Conversión de Cotizaciones).
* **Gráfico de Tendencia Mensual:** Dibujado fluidamente con código **SVG nativo** utilizando curvas de Bézier dinámicas y degradados de color degradados.
* **Gráfico de Barras de Estados:** Muestra la distribución por estados de las cotizaciones con anchos optimizados (`width="30"`) y escalado automático proporcional al máximo histórico.
* **Monto por Divisas (PEN vs. USD):** Barra de distribución de facturación relativa por tipo de moneda.

### 2. Generador de Reportes (`/reportes`)
* **Filtros en Tiempo Real:** Filtros dinámicos por rango de fecha (con inicialización inteligente), estado, moneda y cliente.
* **Combobox de Cliente Filtrable:** Input personalizado con buscador predictivo y autocompletado en caliente.
  * *Interacciones avanzadas:* Cierre automático al hacer clic fuera del componente (`@HostListener` para detectar clics del documento) y detención de propagación en la caja de búsqueda.
* **KPIs Dinámicos de Consulta:** Tarjetas de resumen que cambian en vivo mientras el usuario escribe o modifica sus criterios de filtrado.
* **Exportación de Datos Avanzada:**
  * **Exportar PDF:** Genera reportes PDF ejecutivos estructurados utilizando `jsPDF` y `jspdf-autotable` con una caja de totales consolidados en Soles.
  * **Exportar Excel (CSV):** Descargas seguras en formato CSV optimizado con delimitador `;` y prefijo BOM UTF-8, resolviendo problemas de codificación. Utiliza internamente la API de **Blobs** (`URL.createObjectURL`) para evitar recortes accidentales en celdas que inician con el carácter `#`.
* **Carga de Datos Dinámica:** Las listas de selección de monedas, estados, clientes y cotizaciones se obtienen directamente desde la base de datos a través del backend.

---

## Consistencia de Diseño y UX

* **Visualización de Perfiles:** Traducción visual de los roles del backend en el Navbar (`ROLE_ADMIN` se renderiza como `ADMINISTRADOR` y `ROLE_USER` como `USUARIO`).
* **Diseño sin Botones Verdes:** Se reemplazaron todas las clases de éxito del framework (`btn-success`) por botones de clase primaria (`btn-primary` en azul LTS corporativo), incluyendo el botón *"Registrar Cotización"* (`btn-primary px-5 py-2.5 fw-semibold d-inline-flex align-items-center gap-2`), estandarizando la línea de diseño.
* **Superposición de Capas:** Ajuste del z-index y contextos de apilamiento en la plantilla de reportes para asegurar que los componentes flotantes interactivos (como el menú desplegable de clientes) se visualicen siempre por encima de las tarjetas de métricas del DOM.

---

## Estructura del Código

- **Estadísticas (Dashboard):** [dashboard.component.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/features/dashboard/dashboard.component.ts) | [dashboard.component.html](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/features/dashboard/dashboard.component.html)
- **Reportes:** [reportes.component.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/features/reportes/reportes.component.ts) | [reportes.component.html](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/features/reportes/reportes.component.html)
- **Servicios:** [auth.service.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/core/services/auth.service.ts)
- **Interceptors:** [auth.interceptor.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/core/interceptors/auth.interceptor.ts)
- **Guards:** [auth.guard.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/core/guards/auth.guard.ts)
- **Vistas y Estilos del Login:** [login.component.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/features/login/login.component.ts)
- **Enrutador:** Configurado en [app.routes.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/app.routes.ts) para proteger los paths y rutear el inicio al dashboard.

---

## Servidor de Desarrollo

Para iniciar el servidor local de desarrollo de Angular, ejecuta:

```bash
npm install
npm start
```

Una vez que compile e inicie, abre tu navegador en `http://localhost:4200/`. El sistema te redirigirá automáticamente a `/login` si no has iniciado sesión.

---

## Pruebas y Compilación

### Compilar Proyecto
Para generar la build de producción optimizada en la carpeta `dist/`:
```bash
npm run build
```

### Ejecutar Pruebas Unitarias
Para correr las pruebas unitarias usando el test runner **Vitest**:
```bash
npm test
```
