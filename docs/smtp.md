# Como funciona a conexao SMTP

O SMTP (Simple Mail Transfer Protocol) e o protocolo usado para o
envio de e-mails. Neste projeto ele e usado para entregar o token de
recuperacao de senha.

## Fluxo do envio

Quando o usuario solicita a recuperacao de senha:

1. `PasswordRecoveryController` recebe o e-mail
2. `PasswordRecoveryService` gera um token e chama `MailService`
3. `MailService` verifica se o envio esta configurado:
   - Se `spring.mail.username` estiver vazio, apenas registra o token no log e encerra
   - Se estiver configurado, monta o e-mail e chama o `JavaMailSender`
4. `JavaMailSender` (do Spring Boot) abre uma conexao SMTP com o servidor
   e transmite a mensagem

## Servidor e porta

A configuracao padrao usa o Gmail:

- Servidor: `smtp.gmail.com`
- Porta: `587` (STARTTLS)

### O que e STARTTLS

O SMTP na porta 587 nao e criptografado por padrao. O STARTTLS e um
comando que "sobe" a conexao para TLS no meio da conversa, protegendo as
credenciais de autenticacao. Por isso o `application.yaml` define:

```yaml
spring:
  mail:
    host: ${MAIL_HOST:smtp.gmail.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME:}
    password: ${MAIL_PASSWORD:}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
```

- `mail.smtp.auth: true` — exige autenticacao (usuario e senha) antes de aceitar o envio
- `mail.smtp.starttls.enable: true` — habilita o STARTTLS

## Autenticacao e App Password

O Gmail nao aceita a senha normal da conta para SMTP. E preciso gerar uma
App Password (veja `variaveis-de-ambiente.md`). A configuracao usa:

- `MAIL_USERNAME` = e-mail Gmail (ex.: `seuemail@gmail.com`)
- `MAIL_PASSWORD` = App Password de 16 caracteres

## Comportamento sem configuracao

Se `MAIL_USERNAME` estiver vazio, o `MailService` nao tenta conectar no
servidor: o token e apenas registrado no log com o seguinte formato:

```
Token de recuperação para seuemail@gmail.com: <token>
```

Isso permite testar o fluxo de recuperacao sem depender de um servidor
SMTP configurado.

## E se o envio falhar?

Se o SMTP estiver configurado, mas a conexao ou autenticacao falhar, o
`MailService` captura a excecao, registra um aviso no log e continua
normalmente (o token ja foi logado):

```
Não foi possível enviar o e-mail real. O token foi registrado no log.
```

## Resumo do `MailService`

| Situacao                              | Comportamento                          |
|---------------------------------------|----------------------------------------|
| Sem `MAIL_USERNAME`                   | So registra o token no log             |
| SMTP configurado e envio bem-sucedido | E-mail entregue no destinatario        |
| SMTP configurado e envio falho        | Aviso no log + token registrado        |
