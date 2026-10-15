# Bienvenido/a a EventPass

Este repositorio contiene la arquitectura Backend basada en Microservicios Reactivos para el sistema **EventPass**, una plataforma diseñada para la gestión de concierto, control de capacidad por entradas (VIP, PLATEA, GENERAL), procesamiento de órdenes, pagos y emisión de entradas.

EventPass permite administrar usuarios y autenticación mediante JWT, crear eventos con sus respectivas categorías de tickets, gestionar el flujo transaccional de compra validando el stock en tiempo real entre microservicios, y persistir la información utilizando el patrón *Database-per-Service* mediante aislamiento lógico por esquemas de PostgreSQL y versionado con Flyway.

Este proyecto está pensado para ser utilizado de las siguientes maneras:
- Podrás compilar e inicializar cada microservicio de forma independiente según lo requieras.
- Podrás auditar la base de datos conectándote directamente al motor PostgreSQL contenedorizado y revisar los distintos esquemas aislados.
- Utilizar **Docker Compose** en lugar de levantar manualmente cada servicio, ya que se incluye la configuración completa que inicializa la base de datos, crea las redes internas e inyecta las variables de entorno para cada microservicio.

---

## Estructura

Abre el proyecto mediante terminal o en tu IDE de preferencia (IntelliJ IDEA o Visual Studio Code). El software tiene la siguiente estructura de carpetas y archivos:

```text
EventPass
   |/eventpass_apigateway
   |/eventpass_eventos
   |/eventpass_orders
   |/eventpass_payment
   |/eventpass_ticket
   |/eventpass_users
   |docker-compose.yml
   |README.md
```

En el archivo `docker-compose.yml` y en los archivos `application.yml` de cada servicio se definen las variables de entorno y puertos que utiliza Docker al momento de orquestar los contenedores:

1. **JWT_SECRET**: `EventPass2026_Clave_super_secreta_para_firmar_el_jwt_987654321` — Llave utilizada para firmar y validar los tokens de sesión.
2. **Puerto API Gateway (`8085`)**: Punto de entrada único para consumir todos los servicios desde el exterior (Postman / Cliente).
3. **Puertos de Microservicios**:
    - `8080`: Microservicio de Eventos (`ms_events` - Esquema: `events`)
    - `8081`: Microservicio de Órdenes (`ms_orders` - Esquema: `orders`)
    - `8082`: Microservicio de Pagos (`ms_payments` - Esquema: `payment`)
    - `8083`: Microservicio de Tickets (`ms_tickets` - Esquema: `tickets`)
    - `8084`: Microservicio de Usuarios (`ms_users` - Esquema: `users`)
4. **Puerto PostgreSQL (`5432`)**: Puerto expuesto localmente para conectar clientes de base de datos como DataGrip o DBeaver.

---

## Inicialización con Docker

Abre tu terminal, sitúate en la raíz del proyecto (`EventPass`) y sigue estos pasos:

### 1. Empaquetar los microservicios (Java 25)
Ejecuta el siguiente comando en cada carpeta de los microservicios en tu terminal Bash para compilar todos los proyectos y generar los archivos `.jar` utilizando el Maven Wrapper incluido:

```bash
    ./mvnw clean compile -DskipTests
```

### 2. Levantar la infraestructura
Una vez generados los `.jar`, levanta todos los contenedores en segundo plano ejecutando:

```bash
docker compose up --build -d
```

### 3. Accesos y Conexiones
1. **API Gateway (Punto de entrada principal):** Disponible en `http://localhost:8085`
2. **Base de datos PostgreSQL (`eventpass_db`):** Disponible para conectarse mediante DataGrip u otro gestor con las siguientes credenciales:
    - **Host:** `localhost` (o `127.0.0.1`)
    - **Port:** `5432`
    - **Database:** `eventpass`
    - **Esquemas creados por Flyway:** `events`, `orders`, `payment`, `tickets`, `users`
    - **Nombre de usuario:** `postgres`
    - **Contraseña:** `root`

---