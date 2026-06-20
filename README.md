# ✈️ SkyBooking — Backend API

![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-brightgreen?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-orange?logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-yellow?logo=jsonwebtokens&logoColor=white)
![Status](https://img.shields.io/badge/Status-En%20desarrollo-lightgrey)

Sistema de gestión de reservas de vuelos con autenticación JWT, dos roles de usuario y control completo del ciclo de vida de una reserva aérea.

---

## 🧱 Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.1 |
| Seguridad | Spring Security + JWT (jjwt 0.12.7) |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | MySQL |
| Build | Maven 3.9 |
| Utilidades | Lombok |

---

## 👥 Roles

### 🧍 CLIENT
Usuario registrado que puede buscar vuelos y gestionar sus propias reservas.

- Registrarse e iniciar sesión / ver y modificar perfil propio
- Búsqueda de vuelos con soporte de **vuelos directos y con escalas**
- Reservas de **solo ida** o **ida y vuelta**
- Seleccionar número de asiento y clase de viaje (Economy, Premium Economy, Business, First Class)
- Agregar múltiples piezas de equipaje por pasaje
- **Precios dinámicos:** el costo final considera precio base, clase, carry-on y tarifas de la aerolínea
- Ver reservas activas e historial completo
- Cancelar reservas (completa o por pasajero individual)
- Modificar reservas existentes (cambiar asiento, clase o agregar equipaje)
- Ver y gestionar su saldo de millas (frequent flyer)
- Consultar el estado en tiempo real de sus vuelos

### 🛠️ ADMIN
Operador del sistema con acceso completo para gestionar la flota, rutas y usuarios.

- ABMCL completo de aerolíneas, aviones, aeropuertos, vuelos y usuarios
- Definir tarifas de equipaje por aerolínea y asignar aviones a su flota
- Crear vuelos con validación: **el avión debe pertenecer a la aerolínea seleccionada**
- **Protección de datos:** no se puede modificar un vuelo que ya tiene reservas vendidas
- Actualizar el estado de vuelos (Scheduled → Boarding → Departed → In Air → Landed / Delayed / Cancelled)
- Consultar todas las reservas del sistema
- Gestionar pasajeros
- Ajustar manualmente el saldo de millas de un pasajero
- Ver estadísticas de ocupación por vuelo

---

## 📦 Entidades principales

```
Airline      → Aerolínea (código IATA/ICAO, país, website)
Airport      → Aeropuerto (código IATA/ICAO, ciudad, zona horaria, coordenadas)
Plane        → Avión con capacidad por clase, asociado a una aerolínea
Flight       → Vuelo con origen, destino, horarios, precio base y estado
Client       → Usuario del sistema con rol CLIENT o ADMIN
Passenger    → Pasajero con datos de documento, fecha de nacimiento y millas
Booking      → Reserva con código único, estado y precio total
Passage      → Pasaje individual que vincula un vuelo, una reserva y un pasajero
Luggage      → Equipaje asociado a un pasaje
MilesTransaction → Historial de acreditación/expiración/ajuste de millas
```

### Estados de vuelo

```
SCHEDULED → BOARDING → DEPARTED → IN_AIR → LANDED
                  ↓
              DELAYED / CANCELLED
```

### Estados de reserva

```
PENDING → CONFIRMED → CANCELLED
                ↓
            EXPIRED
```

### Clases de viaje

`ECONOMY` · `PREMIUM_ECONOMY` · `BUSINESS` · `FIRST_CLASS`

---

## 🔐 Autenticación

La API utiliza **JWT Bearer Tokens**. Para acceder a los endpoints protegidos se debe incluir el header:

```
Authorization: Bearer <token>
```

El token se obtiene en el endpoint de login y tiene un tiempo de expiración configurable.

---

## ⚙️ Configuración

Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:

```properties
MYSQL_URL=jdbc:mysql://localhost:3306/skybooking
MYSQL_USER=tu_usuario
MYSQL_PASSWORD=tu_contraseña
```

> El archivo `.env` está excluido del repositorio vía `.gitignore`.

---

## 🚀 Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/skybooking.git
cd skybooking

# Crear el archivo .env con las variables de entorno (ver sección anterior)

# Ejecutar con Maven Wrapper
./mvnw spring-boot:run
```

La aplicación inicia por defecto en `http://localhost:8080`.

Hibernate ejecuta `spring.jpa.hibernate.ddl-auto=update` al arrancar, por lo que las tablas se crean/actualizan automáticamente. El archivo `data.sql` se ejecuta siempre para cargar datos iniciales.

---

## 🗂️ Estructura del proyecto

```
src/
└── main/
    └── java/com/skybooking/backend/
        ├── models/
        │   ├── enums/          # BookingStatus, FlightStatus, TravelClass, etc.
        │   ├── Airline.java
        │   ├── Airport.java
        │   ├── Booking.java
        │   ├── Client.java
        │   ├── Flight.java
        │   ├── Luggage.java
        │   ├── MilesTransaction.java
        │   ├── Passage.java
        │   ├── Passenger.java
        │   └── Plane.java
        └── repositories/       # JpaRepository por entidad
```

---

## 📋 Funcionalidades planificadas

- [ ] Endpoints de autenticación (registro / login / refresh token)
- [ ] CRUD de vuelos (solo ADMIN)
- [ ] Búsqueda de vuelos con filtros (CLIENT)
- [ ] Creación y cancelación de reservas (CLIENT)
- [ ] Gestión de equipaje por pasaje
- [ ] Sistema de millas: acreditación automática al confirmar un vuelo
- [ ] Historial de transacciones de millas por pasajero
- [ ] Actualización de estado de vuelo (ADMIN)
- [ ] Panel de estadísticas de ocupación (ADMIN)
- [ ] Validaciones de negocio (asientos disponibles, fechas, solapamientos)
- [ ] Itinerarios con escalas (búsqueda de vuelos combinados)
- [ ] Precios dinámicos por clase y aerolínea
- [ ] Modificación de reservas (asiento, clase, equipaje)

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos.