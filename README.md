# Bank App (Java Spring Boot + Angular 18)

Gestión bancaria básica:
- **Clientes**
- **Cuentas**
- **Movimientos** (crédito / débito con reglas de negocio)
- **Reportes**: estado de cuenta por rango de fechas + **PDF en Base64**

Incluye:
- Script de base de datos **`BaseDatos.sql`**
- Colección de Postman **`API-BANK.postman_collection.json`**
- Despliegue con **Docker Compose** (MySQL + Backend + Frontend)

---

## Tecnologías

### Backend
- Java + Spring Boot (REST)
- JPA / Hibernate
- MySQL
- Maven

### Frontend
- Angular 18
- TypeScript
- Nginx (para servir el build en contenedor)

---

## Requisitos

### Para correr local
**Backend**
- Java 17 (recomendado) *(o la versión definida en tu proyecto)*
- Maven 3.8+
- MySQL 8
- IDE sugeridos: IntelliJ IDEA / Eclipse / VS Code

**Frontend**
- Node.js 22+ (recomendado) *(o versión compatible con Angular 18)*
- npm 10+
- IDE sugerido: VS Code

### Para correr con Docker
- Docker Desktop (incluye Docker Compose)

---

## Estructura del proyecto
bank-app/
backend/ # Spring Boot API
frontend/ # Angular 18
BaseDatos.sql # script DB
API-BANK.postman_collection.json
docker-compose.yml

## Levantar proyecto: qué levantar primero 
 **Local:**  
1) MySQL + correr `BaseDatos.sql` → 2) Backend → 3) Frontend → 4) Postman.

 **Docker:**  
1) `docker compose up -d --build` 
