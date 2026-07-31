# system-authentication-api

API de autenticacao com JWT, cadastro de usuarios e recuperacao de senha.

## Funcionalidades

- Autenticacao com JWT (login)
- Cadastro de usuarios
- Recuperacao de senha via token

## Arquitetura em camadas

- Controller
- Service
- Validator
- Repository
- Entity
- DTO
- Exception / Handler
- Security
- Configuration

## Tecnologias

- Java 21
- Spring Boot 4.1.0
- Spring Security (OAuth2 Resource Server + JWT)
- Spring Data JPA
- PostgreSQL
- Nimbus JWT
- BCrypt
- Springdoc OpenAPI (Swagger)
- Maven

## Pre-requisitos

- Java 21
- Maven
- PostgreSQL
- Git

### Variaveis de ambiente

```
GROUP_DB_USER
GROUP_DB_PASSWORD
```

### Chaves RSA

Gere um par de chaves RSA (publica e privada) e configure no `application.yaml`:

```
jwt.public.key=classpath:app.pub
jwt.private.key=classpath:app.pem
```

Nao versionar as chaves.

### Executando

```bash
mvn spring-boot:run
```

## Links uteis

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```
