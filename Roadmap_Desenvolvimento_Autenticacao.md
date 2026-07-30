# Roadmap de Desenvolvimento

## 📍 Fase 1: O Motor de Login (Nosso próximo alvo)

Nesta etapa, vamos fazer a ponte entre as credenciais do usuário e a
geração do token.

### AuthService

Criar a regra de negócio para: - Receber o e-mail e a senha. - Buscar o
usuário no banco de dados. - Validar se a senha informada corresponde à
senha armazenada (utilizando **BCrypt**). - Caso as credenciais sejam
válidas, acionar o **JwtEncoder** para assinar e gerar o token JWT.

### AuthController

Expor o endpoint:

``` http
POST /api/auth/login
```

Responsabilidades: - Receber a requisição de login. - Chamar o
`AuthService`. - Retornar o token JWT na resposta.

### Ajuste no Repository

Implementar o método abaixo no `UserRepository`:

``` java
findByEmail(String email)
```

Esse método será responsável por localizar o usuário através do e-mail.

------------------------------------------------------------------------

## 📍 Fase 2: Recuperação de Senha

Nesta etapa será implementado o fluxo completo de recuperação de senha.

### Entidade e Repository

-   Mapear a tabela `password_reset_tokens` para a entidade
    `PasswordResetToken` (JPA).
-   Criar o respectivo repositório.

### Envio de E-mail

Configurar o `JavaMailSender` e criar um `MailService` responsável por
enviar o e-mail contendo o código ou link de recuperação.

### Endpoints de Recuperação

Criar os endpoints:

``` http
POST /api/auth/recuperar-senha
POST /api/auth/resetar-senha
```

Aplicar validações como: - Verificar se o token existe. - Verificar se o
token expirou. - Permitir a alteração da senha apenas para tokens
válidos.

------------------------------------------------------------------------

## 📍 Fase 3: Refinamento e Tratamento de Erros

Padronizar as respostas da API em casos de erro.

### GlobalExceptionHandler

Criar um `@ControllerAdvice` para capturar exceções da aplicação, como:

-   Usuário não encontrado
-   Senha incorreta
-   E-mail já cadastrado
-   Token inválido ou expirado

Retornar respostas HTTP padronizadas, por exemplo:

-   **400 Bad Request**
-   **401 Unauthorized**
-   **404 Not Found**

As mensagens devem ser retornadas em formato JSON consistente.

------------------------------------------------------------------------

## 📍 Fase 4: Garantia de Qualidade e Testes E2E

Após a conclusão da implementação da aplicação, realizar a validação
completa dos fluxos.

### Testes de API (E2E)

Criar testes automatizados cobrindo cenários como:

1.  Cadastrar usuário.
2.  Realizar login.
3.  Receber o JWT.
4.  Acessar rota protegida com token válido.
5.  Tentar acessar rota protegida sem token.
6.  Tentar acessar rota protegida com token inválido.

### Testes Unitários

Criar testes para os componentes responsáveis pelas regras de negócio,
como:

-   Services
-   Validators
-   Expiração de tokens
-   Geração de JWT
-   Recuperação de senha
