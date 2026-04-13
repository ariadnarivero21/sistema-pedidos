# sistema-pedidos

API REST desarrollada en Java Spring Boot, orientada a la gestión de un sistema de pedidos que incluye usuarios, carritos, productos y procesamiento de compras.

### Requisitos previos:

Antes de levantar el proyecto, tener instalado: 
- Java 21
- Maven
- Git
- Postman

## Clonar el repositorio:
```bash
git clone https://github.com/ariadnarivero21/sistema-pedidos.git
cd sistema-pedidos
```

## Levantar el proyecto
El proyecto puede ejecutarse de dos maneras:
- Desde un IDE ejecutando la clase ```SistemaPedidosApplication```
- Desde la consola ejecutando el comando 
```bash
mvn spring-boot:run
```

Una vez levantado el proyecto, la aplicación quedará disponible en ```http://localhost:8080```

La aplicación utiliza una base de datos en memoria (H2).
Para consultar a la base H2 Ingresar a ```http://localhost:8080/h2-console``` y colocar las credenciales:
- JDBC URL: jdbc:h2:mem:testdb
- User Name: sa
- Password:

## Probar los endpoints
- En la ubicación ```\sistema-pedidos\docs\postman``` se encuentra la collección con todos los Endpoints de la API para importar desde Postman.

## Seguridad
La API utiliza Basic Auth para proteger los endpoints.

Para probar los endpoints protegidos, usar credenciales válidas cargadas en la base de datos o las configuradas para el proyecto.

## Métricas

El sistema incluye un endpoint para consultar metricas de uso
```https
GET /metrics
```
Permite visualizar de los endpoints:
- cantidad de llamados
- llamados exitosos
- llamados fallidos
- tiempo de respuesta

### Notas
- La base H2 es en memoria, por lo que los datos se reinician al detener la aplicación.
- Las métricas implementadas en memoria también se reinician al reiniciar la app.
