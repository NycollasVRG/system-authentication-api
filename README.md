# system-authentication-api

API de autenticacao com JWT, cadastro de usuarios e recuperacao de senha.

## Funcionalidades

- Autenticacao com JWT (login)
- Cadastro de usuarios
- Recuperacao de senha via token (é enviada pelo terminal da aplicação)

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

## API Collection

Além da documentação disponível no Swagger, o projeto também disponibiliza uma coleção de requisições para facilitar os testes da API.

A coleção está localizada na pasta **`collection/`** do projeto e pode ser importada em clientes de API como:

* Postman
* Insomnia
* Bruno

O arquivo JSON já contém os principais endpoints configurados, incluindo:

* Cadastro de usuários
* Login (JWT)
* Recuperação de senha
* Reset de senha
