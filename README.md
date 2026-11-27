# Gestão de Materiais de Escritório — Backend

Backend do sistema de Gestão de Materiais de Escritório, desenvolvido com Java 21 e Spring Boot. A aplicação disponibiliza uma API REST para autenticação e gerenciamento de usuários, categorias, locais, materiais, patrimônios, movimentações, manutenções, notificações e auditoria.

## 1. Tecnologias

- Java 21 e Spring Boot
- Maven
- Spring Data JPA / Hibernate
- PostgreSQL 15
- Spring Security e JWT
- BCrypt para hash de senhas
- Docker e Docker Compose
- Swagger / OpenAPI
- GitHub Actions
- GitHub Container Registry (GHCR)

## 2. Requisitos

Para executar a aplicação com Docker:

- Git instalado;
- Docker Desktop instalado e em execução;
- Docker Compose disponível no terminal.

Para executar a aplicação diretamente pela IDE, também é necessário o JDK 21. O projeto contém Maven Wrapper (`mvnw` e `mvnw.cmd`).

## 3. Clonar o repositório

```bash
git clone https://github.com/JoaoPedroNevesAI/Gestao-materiais-escritorio-backend.git
cd Gestao-materiais-escritorio-backend
```

A branch de desenvolvimento da auditoria é `feat/auditoria-completa`. Para obtê-la:

```bash
git switch feat/auditoria-completa
```

Abra a pasta do projeto no Visual Studio Code ou na IDE de sua preferência.

## 4. Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto, no mesmo nível de `docker-compose.yml` e `Dockerfile`.

Exemplo para desenvolvimento:

```env
POSTGRES_PASSWORD=defina_uma_senha_local
JWT_SECRET_KEY=defina_uma_chave_jwt_forte
BACKEND_PORT=8080

# Bootstrap do primeiro administrador: habilite somente durante a inicialização
BOOTSTRAP_ADMIN_ENABLED=true
BOOTSTRAP_ADMIN_NOME=Administrador do Sistema
BOOTSTRAP_ADMIN_EMAIL=admin@exemplo.com
BOOTSTRAP_ADMIN_SENHA='defina_uma_senha_forte'
```

| Variável | Finalidade |
|---|---|
| `POSTGRES_PASSWORD` | Senha do usuário PostgreSQL configurado no Compose. |
| `JWT_SECRET_KEY` | Chave usada para assinar os tokens JWT. Deve ser forte e exclusiva do ambiente. |
| `BACKEND_PORT` | Porta externa usada pelo Compose de produção; no Compose de desenvolvimento, a porta publicada é configurada no YAML. |
| `BOOTSTRAP_ADMIN_ENABLED` | Habilita ou desabilita a criação inicial do administrador. O padrão é `false`. |
| `BOOTSTRAP_ADMIN_NOME` | Nome do primeiro administrador. |
| `BOOTSTRAP_ADMIN_EMAIL` | E-mail do primeiro administrador. |
| `BOOTSTRAP_ADMIN_SENHA` | Senha inicial em texto no `.env`; o backend a armazena com BCrypt. |

**Segurança:** o arquivo `.env` está listado no `.gitignore`. Não o adicione ao Git, não publique credenciais e não coloque senhas ou chaves reais em exemplos, commits, issues ou documentação.

## 5. Primeiro administrador (Admin Bootstrap)

Em um banco recém-criado, ainda não existe um administrador que possa autenticar e cadastrar outros administradores. Para resolver essa inicialização, o projeto possui a classe `AdminBootstrap`, executada na inicialização do Spring Boot.

O comportamento é:

1. O mecanismo verifica se `BOOTSTRAP_ADMIN_ENABLED` está habilitado.
2. Se estiver desabilitado, nenhuma conta é criada.
3. Se estiver habilitado, o backend verifica se já existe algum usuário com tipo `ADM`.
4. Se já existir um ADM, o bootstrap é ignorado.
5. Se não existir, o primeiro ADM é criado com as credenciais configuradas no `.env`, através de `UsuarioService.save()`.
6. O serviço aplica BCrypt à senha, valida duplicidade de e-mail/CPF e registra o evento de auditoria da criação.

### 5.1. Configuração no Docker Compose

O serviço `backend` precisa receber as quatro variáveis. Em `docker-compose.yml`, acrescente à seção `environment` do backend:

```yaml
      BOOTSTRAP_ADMIN_ENABLED: "${BOOTSTRAP_ADMIN_ENABLED:-false}"
      BOOTSTRAP_ADMIN_NOME: "${BOOTSTRAP_ADMIN_NOME:-}"
      BOOTSTRAP_ADMIN_EMAIL: "${BOOTSTRAP_ADMIN_EMAIL:-}"
      BOOTSTRAP_ADMIN_SENHA: "${BOOTSTRAP_ADMIN_SENHA:-}"
```

Faça a mesma configuração em outro arquivo Compose usado para executar a imagem, caso também queira inicializar uma instalação por esse arquivo. A imagem usada precisa conter a classe `AdminBootstrap`.

### 5.2. Criar o primeiro ADM

Com o `.env` preenchido e `BOOTSTRAP_ADMIN_ENABLED=true`, construa e inicie o ambiente:

```bash
docker compose up -d --build
```

Consulte o log do backend:

```bash
docker compose logs -f backend
```

Quando o primeiro administrador for criado, o log informa `Administrador inicial criado com sucesso.`. Caso já exista um ADM, o log informa que o bootstrap foi ignorado.

O login pode ser realizado em `POST /api/auth/login`, usando o e-mail e a senha configurados. O primeiro ADM criado automaticamente aparece na auditoria com responsável `ANONIMO`, pois ainda não existia uma sessão autenticada no momento da criação.

### 5.3. Desabilitar após a inicialização

Depois de confirmar o primeiro ADM, altere no `.env`:

```env
BOOTSTRAP_ADMIN_ENABLED=false
```

Recrie o backend para aplicar o novo valor:

```bash
docker compose up -d --build backend
```

Mantenha o bootstrap desabilitado no uso normal. A checagem de existência evita duplicação enquanto houver um ADM, mas a variável deve permanecer `false` depois da inicialização para que a criação inicial não volte a ser habilitada acidentalmente.

> O bootstrap não é um endpoint público e não substitui o controle de acesso do `UsuarioController`. Depois da criação inicial, novos administradores devem ser cadastrados por um administrador autenticado.

## 6. Executar em desenvolvimento

O `docker-compose.yml` constrói o backend localmente usando o `Dockerfile` e inicia também o PostgreSQL.

```bash
docker compose up --build -d
```

Verificar os containers:

```bash
docker compose ps
```

Acompanhar os logs:

```bash
docker compose logs -f backend
docker compose logs -f db
```

Endereços do ambiente local conforme o Compose de desenvolvimento:

| Serviço | Endereço |
|---|---|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| PostgreSQL no host | `localhost:5433` |
| PostgreSQL na rede Docker | `db:5432` |

O backend utiliza `jdbc:postgresql://db:5432/ifescritorio` dentro da rede Docker. Para conectar ao banco pelo DBeaver no computador host, use `localhost`, porta `5433`, banco `ifescritorio`, usuário `postgres` e a senha definida em `POSTGRES_PASSWORD`.

## 7. Executar usando a imagem publicada no GHCR

O projeto publica a imagem no GitHub Container Registry:

```text
ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend:latest
```

A configuração `compose.prod.yaml` não faz parte do repositório neste momento e precisa ser criada manualmente na raiz do projeto, caso seja utilizada. Exemplo:

```yaml
services:
  db:
    image: postgres:15
    restart: unless-stopped
    environment:
      POSTGRES_DB: ifescritorio
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: "${POSTGRES_PASSWORD:?Configure POSTGRES_PASSWORD no arquivo .env}"
    volumes:
      - postgres-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d ifescritorio"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    image: ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend:latest
    restart: unless-stopped
    depends_on:
      db:
        condition: service_healthy
    ports:
      - "${BACKEND_PORT:-8080}:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/ifescritorio
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: "${POSTGRES_PASSWORD:?Configure POSTGRES_PASSWORD no arquivo .env}"
      SECURITY_JWT_SECRETKEY: "${JWT_SECRET_KEY:?Configure JWT_SECRET_KEY no arquivo .env}"
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      UPLOAD_DIR: /app/uploads/
      BOOTSTRAP_ADMIN_ENABLED: "${BOOTSTRAP_ADMIN_ENABLED:-false}"
      BOOTSTRAP_ADMIN_NOME: "${BOOTSTRAP_ADMIN_NOME:-}"
      BOOTSTRAP_ADMIN_EMAIL: "${BOOTSTRAP_ADMIN_EMAIL:-}"
      BOOTSTRAP_ADMIN_SENHA: "${BOOTSTRAP_ADMIN_SENHA:-}"

volumes:
  postgres-data:
  uploads:
```

Baixe e inicie a imagem:

```bash
docker compose -f compose.prod.yaml pull
docker compose -f compose.prod.yaml up -d
```

Para acompanhar:

```bash
docker compose -f compose.prod.yaml ps
docker compose -f compose.prod.yaml logs -f backend
```

**Atenção:** a imagem GHCR só conterá o `AdminBootstrap` depois que a versão que inclui essa classe for integrada à branch `master` e publicada pelo workflow. Para testar alterações ainda não publicadas, use `docker compose up --build -d` com o Compose de desenvolvimento.

## 8. Autenticação e autorização

O login é realizado por:

```http
POST /api/auth/login
Content-Type: application/json
```

Corpo:

```json
{
  "email": "admin@exemplo.com",
  "senha": "senha_informada_no_login"
}
```

A resposta contém `token`, `nome`, `role`, `id` e `email`. O campo `role` retorna `ROLE_ADM` ou `ROLE_COLABORADOR`.

Nas requisições protegidas, envie:

```http
Authorization: Bearer <token>
```

No Swagger, use o botão **Authorize** e informe o token no formato solicitado pela documentação OpenAPI.

### Cadastro de usuários

O endpoint é `POST /api/usuario`:

- Se `tipo` for omitido, o serviço define `COLABORADOR`.
- O cadastro de colaborador pode ser público.
- Para enviar `tipo: "ADM"`, a requisição precisa estar autenticada com um usuário ADM.
- Um colaborador autenticado ou uma pessoa sem token não pode cadastrar um ADM; o controller retorna `403 Forbidden`.

O frontend pode ocultar a ação de cadastrar administrador para colaboradores, mas essa decisão visual não substitui a validação do backend.

## 9. Auditoria

O backend registra eventos na tabela `registro_auditoria`. O serviço de auditoria é chamado pelas operações de negócio; o frontend não deve criar os eventos manualmente. Sua responsabilidade é consultar e apresentar o histórico.

A consulta é feita por:

```http
GET /api/auditoria
Authorization: Bearer <token-de-ADM>
```

O endpoint é exclusivo para administradores (`ROLE_ADM`). O retorno é paginado e ordenado pela data/hora mais recente primeiro.

### Filtros disponíveis

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `usuarioId` | Long | ID do usuário responsável pelo evento. |
| `acao` | Enum | Tipo da operação, por exemplo `CRIACAO` ou `APROVACAO`. |
| `entidade` | String | Módulo/entidade, por exemplo `MATERIAL` ou `USUARIO`. A comparação não diferencia maiúsculas de minúsculas. |
| `registroId` | Long | ID do registro de negócio afetado. |
| `dataInicio` | LocalDateTime | Limite inferior inclusivo, formato `yyyy-MM-ddTHH:mm:ss`. |
| `dataFim` | LocalDateTime | Limite superior inclusivo, formato `yyyy-MM-ddTHH:mm:ss`. |
| `page` | Integer | Índice da página, começando em zero. |
| `size` | Integer | Quantidade de itens; padrão 20 e máximo 100. |

Exemplo combinado:

```http
GET /api/auditoria?entidade=MATERIAL&acao=ATUALIZACAO&page=0&size=20
```

Exemplo de intervalo de um dia:

```http
GET /api/auditoria?dataInicio=2026-11-19T00:00:00&dataFim=2026-11-19T23:59:59
```

Se `dataInicio` for posterior a `dataFim`, a API retorna `400 Bad Request`.

### Campos de cada evento

- `id`: identificador do evento de auditoria;
- `usuarioId`, `usuarioNome`, `usuarioTipo`: identificação do responsável;
- `acao`: tipo da ação;
- `entidade`: módulo afetado;
- `registroId`: identificador do registro de negócio;
- `descricao`: descrição legível da operação;
- `dadosAnteriores`: objeto JSON com os dados relevantes antes da alteração, ou `null`;
- `dadosNovos`: objeto JSON com os dados relevantes depois da operação, ou `null`;
- `dataHora`: data/hora do evento.

Para operações sem usuário autenticado, como login malsucedido e criação automática do primeiro ADM, `usuarioId` pode ser `null` e `usuarioNome`/`usuarioTipo` são `ANONIMO`.

### Ações e cobertura atual

O enum `TipoAcaoAuditoria` contém `CRIACAO`, `ATUALIZACAO`, `EXCLUSAO`, `SOLICITACAO`, `APROVACAO`, `REPROVACAO`, `CONCLUSAO`, `LOGIN_SUCESSO`, `LOGIN_FALHA`, `LEITURA`, `UPLOAD`, `DOWNLOAD`, `DESATIVACAO`, `REGENERACAO_QRCODE` e `OUTRO`.

As operações atualmente instrumentadas incluem:

- autenticação: login realizado e tentativa de login malsucedida;
- usuário: criação, atualização e exclusão;
- categoria: criação, atualização e exclusão;
- local: criação, atualização e desativação;
- material: criação, atualização, upload de imagem e desativação;
- patrimônio: criação automática pelo cadastro de material, atualização de local após aprovação de movimentação, regeneração de QR Code e desativação;
- movimentação: solicitação, aprovação e reprovação;
- manutenção: solicitação, aprovação e reprovação;
- notificação: criação, marcação como lida e exclusão.

A ação `CONCLUSAO` está implementada no método de serviço de manutenção, mas ainda não há endpoint HTTP no controller para acioná-la. `LEITURA` e `DOWNLOAD` existem no enum, mas não são registrados pelas consultas GET/downloads atuais. Não implementar telas que dependam desses eventos antes de disponibilizar a instrumentação/endpoint correspondente.

### Segurança dos dados de auditoria

Os snapshots não devem conter senhas, hashes de senha, tokens JWT ou outros dados sigilosos. Na atualização de usuário, a auditoria registra os nomes dos campos alterados, e não os valores de senha, CPF, telefone ou e-mail.

## 10. Documentação para o frontend

O guia de integração do frontend está em [`docs/INTEGRACAO_FRONTEND.md`](docs/INTEGRACAO_FRONTEND.md). Ele documenta:

- login e envio do JWT;
- identificação de ADM e COLABORADOR;
- cadastro de administradores;
- chamada e filtros do endpoint de auditoria;
- paginação e apresentação dos detalhes;
- respostas de autorização e limitações atuais.

## 11. Banco de dados e volumes

O Compose de desenvolvimento publica o PostgreSQL em `localhost:5433` e o backend acessa o banco internamente por `db:5432`.

Os volumes persistentes são:

| Volume | Finalidade |
|---|---|
| `postgres-data` | Dados do PostgreSQL. |
| `uploads` | Arquivos enviados/gerados pela aplicação. |

Comandos úteis:

```bash
# Parar containers e remover a rede, preservando os volumes
docker compose down

# Reiniciar os serviços
docker compose restart

# Reconstruir e iniciar o ambiente local
docker compose up --build -d
```

**Não use `docker compose down -v` sem ter certeza de que deseja excluir os volumes.** Isso pode apagar o banco de dados e os arquivos persistidos.

## 12. Testes e integração contínua

Para executar testes localmente com Maven Wrapper no Windows:

```powershell
.\mvnw.cmd clean verify
```

No Linux/macOS:

```bash
./mvnw clean verify
```

O GitHub Actions executa o build/testes Maven com PostgreSQL e valida a construção da imagem Docker. A publicação da imagem GHCR ocorre quando há push na branch `master`, conforme o workflow `.github/workflows/ci.yml`.

## 13. Solução de problemas

### Backend não inicia

```bash
docker compose ps
docker compose logs -f backend
```

Verifique variáveis ausentes no `.env`, conflitos de porta e erros de inicialização Spring.

### DBeaver não conecta

Verifique se o container `db` está em execução. No Compose de desenvolvimento, use host `localhost`, porta `5433`, banco `ifescritorio`, usuário `postgres` e a senha `POSTGRES_PASSWORD`.

### Primeiro ADM não é criado

Confira:

1. `BOOTSTRAP_ADMIN_ENABLED=true` no `.env`;
2. As quatro variáveis `BOOTSTRAP_ADMIN_*` repassadas para `environment` do serviço `backend`;
3. Se a imagem contém a classe `AdminBootstrap` (use o Compose com `build:` para alterações locais);
4. Se já existe um usuário com `tipo = ADM`;
5. Os logs de inicialização do backend.

### Swagger não abre

Verifique se o backend está ativo e acesse `http://localhost:8080/swagger-ui/index.html`.

---

## Segurança em produção

Antes de disponibilizar a aplicação publicamente, configure HTTPS, firewall, backups, gerenciamento seguro de segredos, política de atualização e migração do banco. Não exponha diretamente a porta do PostgreSQL à internet. A execução local por Docker, por si só, não representa uma implantação pública pronta para produção.
