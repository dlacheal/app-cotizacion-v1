# Backend – app-cotizacion-v1 (con JWT Auth 2.0)

API backend de la aplicación **app-cotizacion-v1**, desarrollada con Spring Boot y Maven.  
Este servicio expone la lógica de negocio y el acceso a datos necesarios para el módulo de cotizaciones, protegido mediante autenticación basada en JWT.

---

## Tecnologías

- **Java**
  - Versión: 21
- **Spring Boot**
  - `spring-boot-starter-web` (API REST)
  - `spring-boot-starter-validation` (Validaciones)
  - `spring-boot-starter-security` (Seguridad y autorización)
  - `spring-boot-starter-data-jpa` (Acceso a base de datos)
- **JWT (Json Web Tokens)**
  - `jjwt-api`, `jjwt-impl`, `jjwt-jackson` (versión 0.12.5)
- **Base de Datos**
  - PostgreSQL
- **Gestión de dependencias**
  - Maven (Wrapper: `mvnw`)

---

## Requisitos

Antes de ejecutar el proyecto, asegúrate de contar con:
- **Java JDK 21**
- **PostgreSQL** (con la base de datos `db_cotizacion_v1` inicializada usando los scripts correspondientes).
- **Maven** (o usa el wrapper `./mvnw` incluido).

---

## Estructura de Seguridad Agregada

Se ha incorporado un nuevo módulo de seguridad bajo el paquete `com.cotizacion.backend.config.security`:

- **`SecurityConfig`**: Define la cadena de filtros de seguridad (`SecurityFilterChain`), activa CORS, deshabilita CSRF, configura sesiones sin estado (`STATELESS`) y declara los beans de encriptación (`BCryptPasswordEncoder`) y administración de autenticación (`AuthenticationManager`).
- **`JwtTokenProvider`**: Clase utilitaria para generar, validar y extraer claims (nombre de usuario, roles) de los tokens JWT usando firmas HMAC-SHA256.
- **`JwtAuthenticationFilter`**: Filtro HTTP que intercepta cada petición saliente, extrae el token Bearer del encabezado `Authorization` y carga el contexto de seguridad en caso de ser un token válido.
- **`UserDetailsServiceImpl`**: Implementación de `UserDetailsService` para cargar el usuario desde la base de datos y asociar sus roles/autoridades correspondientes.

---

## Configuración

### Archivo `application.properties`
Ubicado en `src/main/resources/application.properties`:
- **Puerto:** `8081`
- **Datasource:** Configuración de conexión PostgreSQL (`jdbc:postgresql://localhost:5432/db_cotizacion_v1`).
- **JWT Secret:** Puedes personalizar la firma en producción agregando:
  ```properties
  jwt.secret=TU_CLAVE_SECRETA_SUPER_LARGA_Y_SEGURA_EN_BASE64
  jwt.expiration=36000000
  ```

---

## Endpoints del API

### 1. Autenticación Pública (Sin Token)

| Método | Endpoint | Entrada (JSON) | Salida (JSON) | Descripción |
| :---: | :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | `LoginRequestDTO` | `LoginResponseDTO` | Autentica un usuario y retorna su token Bearer JWT con su perfil. |
| `POST` | `/api/v1/auth/register` | `UsuarioRegistroDTO` | Mensaje de éxito/error | Registra un usuario encriptando la contraseña con BCrypt. |

### 2. Endpoints Privados (Requieren Cabecera `Authorization: Bearer <JWT>`)

| Recurso | Método HTTP | Ruta | Descripción |
| :--- | :---: | :--- | :--- |
| **Cotizaciones** | `GET` / `POST` | `/api/v1/cotizaciones` | Listar y registrar cotizaciones |
| **Productos** | `GET` | `/api/v1/productos` | Listar todos los productos registrados |
| **Clientes** | `GET` | `/api/v1/clientes` | Listar todos los clientes |
| **Proveedores** | `GET` | `/api/v1/proveedores` | Listar todos los proveedores |
| **Almacenes** | `GET` | `/api/v1/almacenes` | Listar todos los almacenes |
| **Categorías** | `GET` | `/api/v1/categorias` | Listar categorías de productos |
| **Monedas** | `GET` | `/api/v1/monedas/lista` | Listar todas las monedas configuradas para los selectores |
| **Formas de Pago** | `GET` | `/api/v1/formas-pago` | Listar formas de pago |
| **Estados Cotización**| `GET` | `/api/v1/estados-cotizacion` | Listar estados disponibles en la base de datos |
| **Inventario** | `GET` | `/api/v1/inventario` | Ver inventario consolidado |
| **Personas** | `GET` | `/api/v1/personas` | Listar personas registradas |

---

## Ejecución

Desde la raíz del backend (`02_Backend\springboot-backend`):

### PowerShell (Windows)
```powershell
.\mvnw clean compile
.\mvnw spring-boot:run
```

### Linux / macOS
```bash
./mvnw clean compile
./mvnw spring-boot:run
```
