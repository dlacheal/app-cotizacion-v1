# Documentación técnica — app-cotizacion-v1

## Alcance y estructura

Este documento describe el código y los scripts que están dentro de esta carpeta. No incluye las carpetas homónimas que existen en el directorio padre.

| Componente | Ubicación |
|---|---|
| Base de datos | `01_Base Datos/` |
| API backend | `02_Backend/springboot-backend/` |
| Aplicación web | `03_Frontend/cotizacion-app/` |

El sistema permite mantener catálogos y usuarios, elaborar cotizaciones con sus partidas y consultar inventario. La aplicación web consume una API REST; el backend persiste la información en PostgreSQL.

## Tecnologías y configuración efectiva

- **Base de datos:** PostgreSQL 16 en el `docker-compose.yml` del proyecto.
- **Backend:** Java 21, Spring Boot 4.0.0, Spring Web MVC, Spring Data JPA, Spring Security, JWT y MapStruct; construcción con Maven Wrapper.
- **Frontend:** Angular 21, TypeScript 5.9, RxJS 7.8, SweetAlert2 y jsPDF/jsPDF-AutoTable. El `package.json` identifica npm 11.7.0. Bootstrap aparece en el README de raíz, pero no figura como dependencia del frontend.
- **Puerto backend:** `8081`.
- **Base de datos local:** `db_cotizacion_v1` en `localhost:5432`. El usuario configurado en Compose es `postgres`; el backend usa el usuario indicado en `application.properties`.
- **URL base de API desde Angular:** `http://<hostname-del-navegador>:8081/api/v1`; en un entorno sin `window`, el fallback es `http://localhost:8081/api/v1`.

Los valores de conexión están repartidos entre Compose, `application.properties` y los README. No se reproducen contraseñas aquí. Antes de ejecutar, verifica que el usuario y la contraseña configurados en el backend coincidan con los del contenedor.

## Arquitectura

### Backend

El punto de entrada es `AppCotizacionV1Application`. La API separa responsabilidades en:

- `presentation/controller`: rutas HTTP y códigos de respuesta.
- `presentation/dto`: objetos de entrada y salida.
- `service/interfaces` y `service/impl`: operaciones de negocio.
- `persistence/entity` y `persistence/repository`: entidades JPA y acceso a datos.
- `mapper`: conversión entre entidades y DTO mediante MapStruct.
- `config/security`: autenticación, filtro JWT y carga de usuario/roles.

La configuración JPA no crea el esquema automáticamente; el esquema se entrega mediante SQL. El nivel de log de Hibernate está configurado para mostrar SQL.

### Frontend

Aplicación Angular con rutas definidas en `src/app/app.routes.ts`. Las funcionalidades están organizadas por dominio:

`login`, `dashboard`, `reportes`, `cotizacion`, `cliente`, `producto`, `proveedor`, `categoria`, `persona`, `usuario`, `almacen`, `moneda`, `metodo-pago` y `authority`.

Las rutas de negocio pasan por `authGuard`. `AuthService` conserva token, nombre de usuario, nombre de persona y roles en `localStorage`; el interceptor agrega el encabezado Bearer a las peticiones y cierra la sesión/redirige al login ante una respuesta HTTP 401. El módulo de cotizaciones genera documentos PDF en el navegador; reportes también contiene generación de PDF.

### Base de datos

Los scripts se ejecutan en orden alfabético desde `01_Base Datos/scripts/`:

| Script | Responsabilidad |
|---|---|
| `00_drop_all.sql` | Elimina vistas, funciones, triggers, tablas y secuencias existentes. **Es destructivo.** |
| `01_schema.sql` | Crea las secuencias de identificadores. |
| `02_tables.sql` | Crea las tablas. |
| `03_constraints_indexes.sql` | Agrega claves, restricciones e índices. |
| `04_views.sql` | Crea vistas de cotizaciones e inventario. |
| `05_functions_triggers.sql` | Calcula importes y mantiene los totales de cabecera. |
| `06_data_init.sql` | Inserta datos de catálogo y ejemplos de negocio. |

El Compose monta esta carpeta de scripts en `/docker-entrypoint-initdb.d`. PostgreSQL ejecuta esos archivos al inicializar un volumen de datos vacío; reiniciar el contenedor con un volumen ya creado no vuelve a ejecutar los scripts automáticamente.

#### Tablas y relaciones principales

| Grupo | Tablas | Relaciones relevantes |
|---|---|---|
| Catálogos | `categoria`, `proveedor`, `cliente`, `almacen`, `moneda`, `forma_pago`, `estado_cotizacion` | `producto` puede referenciar categoría y proveedor. |
| Seguridad y personas | `persona`, `usuario`, `rol`, `usuario_rol` | Usuario referencia persona; `usuario_rol` asocia usuarios y roles. |
| Productos e inventario | `producto`, `inventario` | Inventario referencia producto y almacén; se impide repetir el par producto-almacén. |
| Cotizaciones | `cotizacion`, `cotizacion_detalle` | La cabecera referencia cliente, usuario, moneda, estado y opcionalmente forma de pago. El detalle referencia cotización y producto. El borrado de una cotización elimina sus detalles mediante `ON DELETE CASCADE`. |

En `cotizacion_detalle` existe la columna `id_proveedor`, aunque el script de restricciones no declara una clave foránea para esa columna.

Las restricciones adicionales incluyen nombre único de categoría, DNI/email únicos de persona, username único, código de moneda único, nombre de estado único, cantidad de detalle mayor que cero e impuesto entre 0 y 100. Hay índices para nombre de producto, fecha/cliente de cotización y producto/almacén de inventario.

#### Vistas y cálculos

- `vw_cotizacion_cabecera`: datos del cliente, vendedor, moneda, importes, estado y condiciones.
- `vw_cotizacion_detalle`: datos de producto y partidas, costos, margen, impuesto y total por línea.
- `vw_inventario_producto_almacen`: producto, almacén, stock y valorización estimada usando `precio_base`.
- El trigger de detalle calcula precio de venta unitario como costo más margen; costo, subtotal, utilidad, impuesto, total de línea e inversión con impuesto.
- Un trigger posterior a insertar, actualizar o eliminar detalles suma los importes de las partidas y actualiza la cabecera de la cotización.
- El impuesto predeterminado en el detalle es 18 %. El cálculo de inversión con impuesto usa el mismo porcentaje de impuesto definido en la partida.

## API REST

Salvo login y registro, las rutas están protegidas por autenticación. La API se sirve bajo `/api/v1`.

| Recurso | Rutas y operaciones implementadas |
|---|---|
| Autenticación y usuarios | `POST /auth/login`, `POST /auth/register`; `GET /auth/usuarios`, `GET /auth/usuarios/{id}`, `PUT /auth/usuarios/{id}`, `DELETE /auth/usuarios/{id}` |
| Cotizaciones | `GET /cotizaciones` (paginado), `GET /cotizaciones/lista`, `GET /cotizaciones/{id}`, `POST /cotizaciones`, `PUT /cotizaciones/{id}`, `PATCH /cotizaciones/{id}/estado/{estadoNombre}`, `DELETE /cotizaciones/{id}` |
| Categorías | `GET /categorias`, `GET /categorias/{id}`, `POST /categorias`, `PUT /categorias/{id}`, `DELETE /categorias/{id}` |
| Clientes | `GET /clientes`, `/clientes/lista`, `/clientes/{id}`, `/clientes/buscar`; `POST /clientes`, `PUT /clientes/{id}`, `DELETE /clientes/{id}` |
| Productos | `GET /productos`, `/productos/activos`, `/productos/{id}`; `POST /productos`, `PUT /productos/{id}`, `DELETE /productos/{id}` |
| Proveedores | `GET /proveedores`, `/proveedores/{id}`, `/proveedores/buscar`; `POST /proveedores`, `PUT /proveedores/{id}`, `DELETE /proveedores/{id}` |
| Almacenes | `GET /almacenes`, `/almacenes/lista`, `/almacenes/{id}`; `POST /almacenes`, `PUT /almacenes/{id}`, `DELETE /almacenes/{id}` |
| Personas | `GET /personas`, `/personas/lista`, `/personas/{id}`, `/personas/dni/{dni}`, `/personas/validar-existencia/{dni}`; `POST /personas`, `PUT /personas/{id}`, `DELETE /personas/{id}` |
| Monedas | `GET /monedas`, `/monedas/lista`, `/monedas/id/{id}`, `/monedas/{codigo}`; `POST /monedas`, `PUT /monedas/{id}`, `DELETE /monedas/{id}` |
| Formas de pago | `GET /formas-pago`, `/formas-pago/lista`, `/formas-pago/{id}`; `POST /formas-pago`, `PUT /formas-pago/{id}`, `DELETE /formas-pago/{id}` |
| Estados de cotización | `GET /estados-cotizacion`, `/estados-cotizacion/{id}`, `/estados-cotizacion/buscar`; `POST /estados-cotizacion` |
| Inventario | `PATCH /inventario/ajustar-stock`; `GET /inventario/producto/{idProducto}`, `/inventario/almacen/{idAlmacen}`, `/inventario/consultar`; `DELETE /inventario/{id}` |
| Roles/autoridades | `GET /authorities/usuario/{idUsuario}`, `POST /authorities`, `DELETE /authorities` |

Los endpoints de cotizaciones reciben DTOs de creación/actualización que contienen cabecera y partidas. Los campos y validaciones exactos se definen en `presentation/dto/request` y `presentation/dto/response`. La paginación sigue los parámetros Spring Data, por ejemplo `?page=0&size=10`.

## Puesta en marcha local

Requisitos: Docker Compose, JDK 21 y Node/npm compatibles con Angular 21.

1. Desde la raíz de este proyecto, iniciar PostgreSQL:

   ```powershell
   docker compose -f "01_Base Datos/docker/docker-compose.yml" up -d
   ```

   La primera inicialización ejecuta los scripts SQL. Confirma la conexión con los valores de configuración del proyecto antes de continuar.

2. Iniciar el backend en otra terminal:

   ```powershell
   Set-Location "02_Backend/springboot-backend"
   .\mvnw.cmd spring-boot:run
   ```

3. Instalar dependencias del frontend (si aún no están instaladas) e iniciar Angular:

   ```powershell
   Set-Location "03_Frontend/cotizacion-app"
   npm ci
   npm start
   ```

   La URL de desarrollo que muestra Angular CLI se abre en el navegador. El frontend espera el backend en el puerto `8081`.

Para validar la compilación, los manifiestos incluyen `.\mvnw.cmd test` en backend y `npm test` / `npm run build` en frontend. Esta documentación no ejecutó la aplicación ni las pruebas.

## Recomendaciones (no aplicadas)

Estas observaciones son propuestas; no se modificó código ni configuración al preparar este documento.

1. **Alinear la documentación de versiones y estructura.** El README raíz menciona PostgreSQL 10, mientras Compose configura PostgreSQL 16; también hay diferencias en nombres/rutas de carpetas. El README de base de datos documenta credenciales que no coinciden con la configuración efectiva del Compose/backend.
2. **Externalizar secretos y configuración por entorno.** Mantener contraseñas y clave JWT fuera del repositorio y suministrarlas desde un mecanismo local/CI adecuado; documentar explícitamente los parámetros requeridos sin publicar valores sensibles.
3. **Restringir CORS y aplicar autorización por rol.** La configuración actual permite cualquier origen y la cadena de seguridad exige autenticación para todas las rutas salvo login/registro. Si se requieren permisos distintos, definir y probar reglas de autorización por recurso/operación.
4. **Formalizar migraciones y reinicialización segura.** El script `00_drop_all.sql` elimina datos y se ejecuta en la secuencia de inicialización. Separar el ciclo de desarrollo de la evolución de ambientes con datos persistentes y documentar copias de seguridad antes de cualquier reinicialización.
5. **Completar integridad referencial y pruebas de integración.** Evaluar una FK para `cotizacion_detalle.id_proveedor` y probar la consistencia de los cálculos entre UI, backend y triggers, así como autenticación, autorización y contratos de los endpoints.
