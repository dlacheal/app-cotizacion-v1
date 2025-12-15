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
│   │   │   │	├── exception/
│   │   │	│  	└── util/
│   │   │	│── config/
│   │   │	│── mapper/
│   │   │	│── persistence/
│   │   │	│	   ├── entity/
│   │   │	│	   └── repository/
│   │   │	│── presentation/
│   │   │	│   │── controller/
│   │   │	│	└── dto/
│   │   │	│  		├── request/
│   │   │	│	  	└── response/
│   │   │	└── service/
│   │   │   	├── interfaces/
│   │   │   	└── impl/
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


## Endpoints (opcional)

> Completar esta sección con los endpoints reales una vez definidos los controladores.

Ejemplo de formato sugerido:

```text
Base URL: http://localhost:8080/api

GET    /cotizaciones              - Listar cotizaciones
GET    /cotizaciones/{id}         - Obtener detalle de una cotización
POST   /cotizaciones              - Crear nueva cotización
PUT    /cotizaciones/{id}         - Actualizar cotización existente
DELETE /cotizaciones/{id}         - Eliminar cotización
```

Para cada endpoint se recomienda documentar:

- **Descripción** de la operación.
- **Parámetros de entrada** (path, query, body).
- **Ejemplo de request/response**.
- **Códigos de estado HTTP** esperados.

Si el proyecto integra **Swagger / OpenAPI**, incluir la URL de la documentación, por ejemplo:

```text
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
```

---


## Ejecución
```markdown

Desde la raíz del proyecto: `...\app-cotizacion-v1\02_Backend\springboot-backend`

### 1. Compilar y ejecutar con Maven


# Linux / macOS
./mvnw clean install
./mvnw spring-boot:run

# Windows (CMD/PowerShell)
mvnw clean install
mvnw spring-boot:run
```

También puedes ejecutar directamente el JAR generado:

```bash
java -jar target/app-cotizacion-v1-*.jar
```

(Ajustar el nombre del JAR según el `artifactId` y `version` definidos en `pom.xml`).



## Notas

- Este backend forma parte del proyecto **app-cotizacion-v1** y está pensado para integrarse con un frontend (web o móvil).
- Revisar las ramas del repositorio para distintas versiones o entornos (`develop`, `main`, `release`, etc.).
- Mantener actualizado el archivo `pom.xml` y ejecutar `mvnw dependency:tree` periódicamente para revisar dependencias obsoletas o vulnerables.
- Se recomienda:
  - Implementar validaciones de entrada a nivel DTO y a nivel de servicio.
  - Centralizar el manejo de errores con `@ControllerAdvice`.
  - Agregar seguridad (Spring Security, JWT, OAuth2, etc.) si el proyecto lo requiere.
- Cualquier cambio en la estructura o endpoints debe reflejarse en este `README.
