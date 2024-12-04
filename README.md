# BankingProject2

Este proyecto es un sistema bancario que implementa microservicios para la gestión de clientes, cuentas bancarias y transacciones. Está desarrollado en Java 17 utilizando Spring Boot, Spring WebFlux, JPA, MySQL, MongoDB, y otros componentes modernos del ecosistema Java. El proyecto sigue principios de SOLID, patrones de diseño, y buenas prácticas.

## Descripción General

El sistema consta de tres microservicios principales:

1.	AccountMs: Gestión de cuentas bancarias (creación, depósitos, retiros, etc.).
2.	CustomerMs: Gestión de clientes (creación, actualización, eliminación, etc.).
3.	TransactionMs: Gestión de transacciones (depósitos, retiros y transferencias entre cuentas).

Cada microservicio interactúa de forma independiente y está diseñado para trabajar bajo una arquitectura de microservicios desacoplada.

## Características Principales

### AccountMs

- Creación de cuentas bancarias con tipo (ahorros o corriente).
- Operaciones de depósito y retiro con validaciones específicas según el tipo de cuenta.
- Listado y consulta de cuentas por cliente.
- Persistencia en MySQL con JPA/Hibernate.

### CustomerMs

- Gestión de clientes, incluyendo creación, actualización y eliminación.
- Verificación de clientes con cuentas activas antes de eliminarlos.
- Integración con el microservicio de cuentas mediante Feign Client.
- Persistencia en MySQL con JPA/Hibernate.

### TransactionMs

- Registro de depósitos, retiros y transferencias entre cuentas.
- Validación de datos para cada transacción.
- Historial completo de transacciones almacenado en MongoDB.
- Implementación de programación reactiva con WebFlux y Reactor.

## Tecnologías Utilizadas

- Java 17
- Spring Boot (3.3.5)
- Spring Data JPA
- Spring WebFlux
- Spring Cloud OpenFeign
- MySQL (para CustomerMs y AccountMs)
- MongoDB (para TransactionMs)
- JUnit 5 y Mockito (para pruebas unitarias)
- Jacoco (para cobertura de código)
- Checkstyle (para estándares de codificación)
- OpenAPI (documentación de APIs)

## Modificaciones Realizadas

A lo largo del desarrollo, se realizaron las siguientes modificaciones y mejoras:

### AccountMs

1.	Implementación de pruebas unitarias:

- Pruebas para BankAccountService y BankAccountController usando Mockito y JUnit 5.
- Casos positivos y negativos para las operaciones de depósito, retiro y consulta.
  
2.	Cobertura de código:

- Integración de Jacoco para verificar la cobertura de pruebas.
- Alcanzamos más del 80% de cobertura en este microservicio.
  
3.	Organización del código:

- Refactorización para cumplir con principios SOLID y mejorar la mantenibilidad.

### CustomerMs

1.	Implementación de pruebas unitarias:

- Pruebas para CustomerService y CustomerController usando Mockito y MockMvc.
- Validación de creación de clientes, eliminación de clientes con cuentas activas y consulta de clientes.

2.	Documentación de API:

-	Uso de OpenAPI para generar documentación interactiva de la API.

### TransactionMs

1.	Implementación de programación reactiva:
   
- Uso de WebFlux y Reactor para la gestión de transacciones de manera no bloqueante.

3.	Pruebas unitarias:
   
- Pruebas para TransactionService y TransactionController con Mockito, Mono, y Flux.
- Casos para depósitos, retiros y transferencias.
  
3.	Integración con MongoDB:

- Configuración de MongoDB como base de datos para almacenar el historial de transacciones.

 
