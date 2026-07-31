# Variaveis de ambiente

As configuracoes da aplicacao sao controladas por variaveis de ambiente
com valores padrao definidos no `application.yaml`.

## Variaveis usadas

| Variavel          | Padrao     | Descricao                            |
|-------------------|------------|--------------------------------------|
| `GROUP_DB_USER`   | `postgres` | Usuario do PostgreSQL                |
| `GROUP_DB_PASSWORD` | `postgres` | Senha do PostgreSQL                |
| `MAIL_USERNAME`   | (vazio)    | E-mail usado no envio SMTP           |
| `MAIL_PASSWORD`   | (vazio)    | Senha do SMTP (App Password no Gmail)|
| `MAIL_HOST`       | `smtp.gmail.com` | Servidor SMTP                  |
| `MAIL_PORT`       | `587`      | Porta do SMTP                         |

Enquanto `MAIL_USERNAME` e `MAIL_PASSWORD` estiverem vazios, a aplicacao
nao tenta enviar e-mails: o token de recuperacao e apenas registrado no
log do servidor.

## Como configurar

### No IntelliJ

1. Abra o menu `Run` e clique em `Edit Configurations...`
2. Selecione a configuracao da aplicacao
3. Em `Environment variables`, adicione:

```
GROUP_DB_USER=postgres;GROUP_DB_PASSWORD=postgres;MAIL_USERNAME=seuemail@gmail.com;MAIL_PASSWORD=xxxxxxxxxxxxxxxx
```

4. Clique em `Apply` e `OK`

### No PowerShell (Windows)

```powershell
$env:GROUP_DB_USER = "postgres"
$env:GROUP_DB_PASSWORD = "postgres"
$env:MAIL_USERNAME = "seuemail@gmail.com"
$env:MAIL_PASSWORD = "xxxxxxxxxxxxxxxx"
.\mvnw.cmd spring-boot:run
```

## App Password do Gmail

Para usar o Gmail como remetente, o e-mail exige uma senha de aplicativo
(App Password), nao a senha normal da conta.

1. Ative a verificacao em duas etapas em
   https://myaccount.google.com/security
2. Gere uma senha de aplicativo em
   https://myaccount.google.com/apppasswords
3. Escolha um nome (ex.: "Authentication API") e clique em `Create`
4. Copie a senha de 16 caracteres gerada e use em `MAIL_PASSWORD`
