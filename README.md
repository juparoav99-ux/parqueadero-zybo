# Sistema de Gestión de Parqueadero - Zybo

API REST desarrollada en Java + Spring Boot para la gestión de parqueaderos.

## Tecnologías Utilizadas

- Java 21
- Spring Boot 3.4.1
- Spring Data JPA
- MySQL 9.5
- Maven

## Requisitos Previos

- Java 17 o superior
- MySQL 8.0 o superior
- Maven 3.9+

## Configuración de Base de Datos

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE parqueadero;
```

2. Configurar credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/parqueadero
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

## Ejecución
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints

### Usuarios (CRUD)
- `POST /usuarios` - Crear usuario
- `GET /usuarios/{id}` - Obtener usuario
- `PUT /usuarios/{id}` - Actualizar usuario
- `DELETE /usuarios/{id}` - Eliminar usuario

### Vehículos (CRUD)
- `POST /vehiculos` - Crear vehículo
- `GET /vehiculos/{id}` - Obtener vehículo
- `PUT /vehiculos/{id}` - Actualizar vehículo
- `DELETE /vehiculos/{id}` - Eliminar vehículo

### Estancias
- `POST /estancias/ingreso` - Registrar ingreso de vehículo
- `POST /estancias/{id}/salida` - Registrar salida y calcular cobro

### Eventos
- `POST /eventos/dispatch` - Despachar eventos pendientes

## Ejemplos de Uso

### Crear Usuario
```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombres":"Juan Perez","documento":"123456","telefono":"3001234567"}'
```

### Crear Vehículo
```bash
curl -X POST http://localhost:8080/vehiculos \
  -H "Content-Type: application/json" \
  -d '{"placa":"ABC123","usuarioId":1}'
```

### Registrar Ingreso
```bash
curl -X POST http://localhost:8080/estancias/ingreso \
  -H "Content-Type: application/json" \
  -d '{"vehiculoId":1}'
```

### Registrar Salida
```bash
curl -X POST http://localhost:8080/estancias/1/salida
```

## Reglas de Negocio

### Cobro
- Tarifa: **100 pesos por minuto**
- Redondeo: Si hay segundos adicionales, se cobra el minuto completo (redondeo hacia arriba)

### Concurrencia
- Un vehículo solo puede tener una estancia ABIERTA a la vez
- Una estancia solo puede cerrarse una vez
- Se utilizan locks pesimistas (`PESSIMISTIC_WRITE`) para evitar condiciones de carrera

### Eventos (Patrón Outbox)
- Al registrar una salida, se crea un evento `SALIDA_REGISTRADA` en estado `PENDIENTE`
- El endpoint `/eventos/dispatch` procesa los eventos pendientes y los marca como `ENVIADO`

## Ejecutar Pruebas
```bash
mvn test
```

Las pruebas incluyen validación de concurrencia:
- Dos hilos intentando ingresar el mismo vehículo simultáneamente
- Dos hilos intentando cerrar la misma estancia simultáneamente

## Estructura del Proyecto
```
src/main/java/com/zybo/Parqueadero/
├── controller/
│   ├── UsuarioController.java
│   ├── VehiculoController.java
│   ├── EstanciaController.java
│   └── EventoController.java
├── service/
│   ├── UsuarioService.java
│   ├── VehiculoService.java
│   ├── EstanciaService.java
│   └── EventoOutboxService.java
├── repository/
│   ├── UsuarioRepository.java
│   ├── VehiculoRepository.java
│   ├── EstanciaRepository.java
│   └── EventoOutboxRepository.java
├── entity/
│   ├── Usuario.java
│   ├── Vehiculo.java
│   ├── Estancia.java
│   └── EventoOutbox.java
├── dto/
│   ├── UsuarioDTO.java
│   ├── VehiculoDTO.java
│   ├── IngresoDTO.java
│   ├── EstanciaDTO.java
│   └── EventoOutboxDTO.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ConflictException.java
│   ├── BadRequestException.java
│   └── GlobalExceptionHandler.java
└── ParqueaderoApplication.java
```

## Autor

Juan Rozo - Prueba Técnica Zybo