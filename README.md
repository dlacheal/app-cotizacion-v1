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

## Estructura
- `/02_backend` → API REST
- `/03_frontend` → Aplicación web
- `/01_database` → Scripts y configuración DB

## Ejecución local
1. Levantar PostgreSQL
2. Iniciar backend (Spring Boot)
3. Iniciar frontend (Angular)

## Arquitectura
- Backend: Java 21 / Spring Boot 4
- Frontend: Angular
- Base de datos: PostgreSQL 10

## Estructura
- `/02_backend` → API REST
- `/03_frontend` → Aplicación web
- `/01_database` → Scripts y configuración DB

## Estructura del proyecto

app-cotizacion-v1/

│

├── backend/                 # Spring Boot

│   ├── src/

│   ├── pom.xml

│   └── README.md

│
├── frontend/                # Angular

│   ├── src/

│   ├── angular.json

│   └── README.md

│
├── database/                # PostgreSQL

│   ├── scripts/

│   │   ├── 01_schema.sql

│   │   ├── 02_tables.sql

│   │   └── 03_data.sql

│   ├── docker/

│   │   └── docker-compose.yml

│   └── README.md

│
├── .gitignore

├── README.md

└── docker-compose.yml       # (opcional) levantar todo el stack
