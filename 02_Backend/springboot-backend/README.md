# Backend – app-cotizacion-v1

API backend de la aplicación **app-cotizacion-v1**, desarrollada con Spring Boot y Maven.  
Este servicio expone la lógica de negocio y el acceso a datos necesarios para el módulo de cotizaciones.


## Tecnologías


- **Java**
    - Versión: (21)
- **Spring Boot**
    - spring-boot-starter-web (API REST)
    - spring-boot-starter-validation (validaciones)
    - spring-boot-starter-test (tests)
    - Otros starters según dependencias definidas en `pom.xml`
- **Maven**
    - Gestión de dependencias y ciclo de vida del proyecto
    - Wrapper: `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties`
- **Base de datos**
    - PostgreSQL
- **Control de versiones**
    - Git (`.gitignore`, `.gitattributes`)



## Requisitos

Antes de ejecutar el proyecto, asegúrate de contar con:

- **Java JDK**
    - Versión compatible con la definida en `pom.xml` (`<maven.compiler.source>` / `<java.version>`)
- **Maven**
    - Opcional si usas el wrapper (`mvnw` / `mvnw.cmd`), pero recomendable.
- **Base de datos**
    - Motor instalado y corriendo (si no se usa H2 en memoria).
    - Usuario, contraseña y base de datos creados, según configuración en `src/main/resources/application.properties` o `application.yml`.
- **Git** (opcional)
    - Para clonar el repositorio y gestionar versiones.


## Estructura
Estructura principal del proyecto:

```markdown

springboot-backend/
├── src/
│   ├── main/
│   │   ├── java/com/cotizacion/backend/  
│   │   │	├── common/
│   │   │	│	├── exception/
│   │   │	│	└── util/
│   │   │	│── config/
│   │   │	│── mapper/
│   │   │	│── persistence/
│   │   │	│	   ├── entity/
│   │   │	│	   └── repository/
│   │   │	│── presentation/
│   │   │	│	│── controller/
│   │   │	│	└── dto/
│   │   │	│	    ├── request/
│   │   │	│	    └── response/
│   │   │	└── service/
│   │   │	        ├── interfaces/
│   │   │	        └── impl/
│   │	│── AppCotizacionV1Application.java
│   │   └── resources/
│   └── test/
├── pom.xml
└── README.md


```

Estructura lógica típica (puede variar según tu implementación real):

- `controller` o `web`: controladores REST (endpoints).
- `service`: lógica de negocio.
- `repository`: acceso a BD (Spring Data JPA u otro).
- `model` / `entity` / `domain`: entidades de dominio y DTOs.
- `config`: clases de configuración (seguridad, CORS, etc.).
- `exception`: manejo centralizado de errores.

---

## Configuración


1. **Variables de entorno (opcional pero recomendado)**  
   Configura, si aplica:
```markdown
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
```
2. **Archivo `application.properties` o `application.yml`**  
   Edita en `src/main/resources`:

    - Configuración de datasource (URL, usuario, contraseña, driver).
    - `spring.jpa.*` (dialecto, DDL auto, etc.) si usas JPA/Hibernate.
    - Puerto del servidor:
```markdown
     server.port=8081
```


3. **Perfiles de Spring (opcional)**  
   Si utilizas perfiles:
    - `application-dev.properties`
    - `application-prod.properties`
    - Ejecuta el proyecto con:
```markdown
     -Dspring.profiles.active=dev
```


## Endpoints del API

La base URL de la aplicación está configurada en el puerto `8081`:

```text
Base URL: http://localhost:8081
```

### Endpoints Principales

| Recurso | Método HTTP | Ruta | Descripción |
| :--- | :--- | :--- | :--- |
| **Cotizaciones** | `GET` | `/api/v1/cotizaciones` | Listar todas las cotizaciones |
| | `POST` | `/api/v1/cotizaciones` | Registrar una nueva cotización |
| **Productos** | `GET` | `/api/v1/productos` | Listar todos los productos registrados |
| **Clientes** | `GET` | `/api/v1/clientes` | Listar todos los clientes |
| **Proveedores** | `GET` | `/api/v1/proveedores` | Listar todos los proveedores |
| **Almacenes** | `GET` | `/api/v1/almacenes` | Listar todos los almacenes |
| **Categorías** | `GET` | `/api/v1/categorias` | Listar categorías de productos |
| **Monedas** | `GET` | `/api/v1/monedas` | Listar monedas configuradas |
| **Formas de Pago** | `GET` | `/api/v1/formas-pago` | Listar formas de pago |
| **Estados Cotización** | `GET` | `/api/v1/estados-cotizacion` | Listar estados disponibles |
| **Inventario** | `GET` | `/api/v1/inventario` | Ver inventario consolidado |
| **Personas** | `GET` | `/api/v1/personas` | Listar personas registradas |

### Seguridad / Roles

| Recurso | Método HTTP | Ruta | Descripción |
| :--- | :--- | :--- | :--- |
| **Registro** | `POST` | `/api/v1/auth/register` | Registrar un nuevo usuario |
| **Authorities** | `GET` | `/api/v1/authorities/usuario/{id}` | Listar los roles de un usuario |
| | `POST` | `/api/v1/authorities` | Asignar un rol a un usuario (Many-to-Many en BD) |
| | `DELETE` | `/api/v1/authorities` | Revocar un rol de un usuario |

Si el proyecto integra **Swagger / OpenAPI** en el futuro, las URL sugeridas son:

```text
Swagger UI: http://localhost:8081/swagger-ui.html
OpenAPI JSON: http://localhost:8081/v3/api-docs
```

---

## Ejecución

Desde la raíz del proyecto: `...\app-cotizacion-v1\02_Backend\springboot-backend`

### 1. Compilar y ejecutar con Maven

**Linux / macOS:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

**Windows (PowerShell):**
```powershell
.\mvnw clean install
.\mvnw spring-boot:run
```

**Windows (CMD):**
```cmd
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

También puedes ejecutar directamente el JAR generado:

```bash
java -jar target/springboot-backend-0.0.1-SNAPSHOT.jar
```

---

## Notas

- Este backend forma parte del proyecto **app-cotizacion-v1** y está pensado para integrarse con el frontend en Angular.
- El modelo de roles se implementa mediante una relación de Muchos a Muchos (`@ManyToMany`) en JPA, mapeado en base de datos a través de las tablas `Rol` y `Usuario_Rol`.
- Mantener actualizado el archivo `pom.xml` y ejecutar `mvn dependency:tree` periódicamente para revisar dependencias.
- Cualquier cambio en la estructura o endpoints debe reflejarse en este `README.md`.
