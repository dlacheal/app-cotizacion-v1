# App-Cotizacion-v1

Sistema de cotización full-stack.
- Base de datos
- Backend
- Frontend

## Tecnologías
- DB: (PostgreSQL)
- Backend: Java / Spring Boot
- Frontend: (Angular, Bootstrap)


## Arquitectura
- Backend: Java 21 / Spring Boot 4
- Frontend: Angular
- Base de datos: PostgreSQL 10


## Ejecución local
1. Levantar PostgreSQL
2. Iniciar backend (Spring Boot)
3. Iniciar frontend (Angular)


## Estructura
- `/02_backend` → API REST
- `/03_frontend` → Aplicación web
- `/01_database` → Scripts y configuración DB


## Estructura del proyecto
```markdown

app-cotizacion-v1/
│
├── 01_database/                # PostgreSQL
│   ├── scripts/
│   │      ├── 00_drop_all.sql
│   │      ├── 01_schema.sql
│   │      ├── 02_tables.sql
│   │      ├── 03_constraints_indexes.sql
│   │      ├── 04_views.sql
│   │      ├── 05_functions_triggers.sql
│   │      └── 06_data_init.sql
│   ├── docker/
│   │   └── docker-compose.yml
│   └── README.md
│
├── 02_backend/                 # Spring Boot
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── 03_frontend/                # Angular
│   ├── src/
│   ├── angular.json
│   └── README.md
│
├── .gitignore
├── README.md
└── docker-compose.yml       # (opcional) levantar todo el stack

```


Autor 
---
David Raymundo Lache Alvarez