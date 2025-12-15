# Database – app-cotizacion-v1

Capa de base de datos del sistema **app-cotizacion-v1**, implementada en **PostgreSQL**, con scripts SQL versionados y ejecución automática mediante **Docker Compose**.

---

## 📌 Tecnologías
- PostgreSQL 16
- Docker / Docker Compose
- SQL (PL/pgSQL)
- Scripts versionados por orden de ejecución

---

## 📂 Estructura
```markdown
01_database/
│
├── scripts/
│ ├── 00_drop_all.sql
│ ├── 01_schema.sql
│ ├── 02_tables.sql
│ ├── 03_constraints_indexes.sql
│ ├── 04_views.sql
│ ├── 05_functions_triggers.sql
│ └── 06_data_init.sql
│
└── docker/
    └── docker-compose.yml
    
```


## 🧩 Descripción de los scripts

| Archivo | Descripción |
|------|------------|
| `00_drop_all.sql` | Limpieza total del esquema (tablas, vistas, funciones, triggers y secuencias) |
| `01_schema.sql` | Creación de secuencias |
| `02_tables.sql` | Creación de tablas |
| `03_constraints_indexes.sql` | Primary Keys, Foreign Keys, constraints e índices |
| `04_views.sql` | Vistas para consultas de negocio |
| `05_functions_triggers.sql` | Funciones y triggers (lógica de cálculo en BD) |
| `06_data_init.sql` | Datos iniciales de prueba (seed data) |

> ⚠️ Los scripts se ejecutan **automáticamente en orden alfabético** cuando el contenedor PostgreSQL se inicializa por primera vez.

---

## 🚀 Levantar la base de datos con Docker

### 1️⃣ Requisitos
- Docker
- Docker Compose

### 2️⃣ Ejecutar PostgreSQL

Desde la carpeta `database/docker`:

```bash
docker compose up -d
```

---

### 🔑 Credenciales por defecto

Database : db_cotizacion_v1
User     : cotizacion_user
Password : cotizacion_pass
Port     : 5432

---

### 🔌 Conexión desde el backend (Spring Boot)

spring.datasource.url=jdbc:postgresql://localhost:5432/db_cotizacion_v1
spring.datasource.username=cotizacion_user
spring.datasource.password=cotizacion_pass
spring.jpa.hibernate.ddl-auto=none
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect


### ♻️ Reinicializar la base de datos
```bash
docker compose down -v
docker compose up -d
```

Autor: David Raymundo Lache Alvarez
---
Proyecto app-cotizacion-v1