# Gestão de Materiais de Escritório — Backend

Backend do sistema de Gestão de Materiais de Escritório, desenvolvido com Java e Spring Boot. Disponibiliza uma API REST para gerenciamento dos dados do sistema, utilizando PostgreSQL como banco de dados e JWT para autenticação.

## 1. Tecnologias utilizadas

- Java 21
- Spring Boot
- Maven
- Spring Data JPA / Hibernate
- PostgreSQL 15
- Docker e Docker Compose
- JWT para autenticação
- Swagger / OpenAPI para documentação e testes da API
- GitHub Actions para integração contínua
- GitHub Container Registry (GHCR) para publicação da imagem Docker

## 2. Requisitos

Para executar o backend utilizando Docker:

- Git instalado.
- Docker Desktop instalado e em execução.
- Docker Compose disponível por meio do Docker.
- Acesso à internet para baixar as imagens necessárias.

Para desenvolver diretamente pelo Java, também é necessário ter o JDK 21 e o Maven ou o Maven Wrapper, caso disponível no projeto.

## 3. Obtendo o projeto

Clone o repositório:

```bash
git clone <URL_DO_REPOSITORIO>
```

Acesse a pasta:

```bash
cd Gestao-materiais-escritorio-backend
```

Abra o projeto no Visual Studio Code ou em outra IDE de sua preferência.

## 4. Configurando as variáveis de ambiente

Antes de iniciar a aplicação, crie um arquivo chamado `.env` na raiz do projeto.

Estrutura:

```text
Gestao-materiais-escritorio-backend/
├── src/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .gitignore
└── .env
```

Conteúdo do `.env`:

```env
POSTGRES_PASSWORD=sua_senha_do_banco
JWT_SECRET_KEY=sua_chave_secreta_jwt
BACKEND_PORT=8080
```

### Finalidade das variáveis

| Variável | Descrição |
|---|---|
| `POSTGRES_PASSWORD` | Senha utilizada pelo PostgreSQL. |
| `JWT_SECRET_KEY` | Chave secreta utilizada na autenticação JWT. |
| `BACKEND_PORT` | Porta externa utilizada na configuração de produção. |

Cada ambiente deve utilizar suas próprias credenciais. A chave JWT deve ser segura e compatível com a configuração da aplicação.

**Importante:** o `.env` é ignorado pelo `.gitignore` e não deve ser enviado ao GitHub. Nunca compartilhe senhas ou chaves secretas no repositório.

## 5. Executando em ambiente de desenvolvimento

O arquivo `docker-compose.yml` utiliza o Dockerfile do projeto para construir e executar o backend junto ao PostgreSQL.

Na raiz do repositório, execute:

```bash
docker compose up --build -d
```

Esse comando:

1. Constrói a imagem Docker do backend a partir do código-fonte.
2. Cria os containers necessários.
3. Inicia o PostgreSQL.
4. Aguarda o banco ficar saudável.
5. Inicia o backend.

### Verificando os containers

```bash
docker compose ps
```

### Consultando os logs

Logs do backend:

```bash
docker compose logs -f backend
```

Logs do PostgreSQL:

```bash
docker compose logs -f db
```

### Endereços do ambiente de desenvolvimento

| Serviço | Endereço |
|---|---|
| Backend | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| PostgreSQL | `localhost:5433` |

Dentro da rede Docker, o backend se comunica com o PostgreSQL utilizando o nome do serviço `db` e a porta interna do banco.

## 6. Executando com a imagem publicada no GHCR

O projeto possui uma imagem Docker publicada no GitHub Container Registry (GHCR).

Imagem:

```text
ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend:latest
```

Essa modalidade permite executar o backend em outra máquina sem precisar compilar o código-fonte ou instalar Java e Maven.

### 6.1. Criando o arquivo de produção

Atualmente, o arquivo `compose.prod.yaml` precisa ser criado manualmente na raiz do projeto, caso ainda não esteja disponível no repositório.

Crie o arquivo `compose.prod.yaml` no VS Code e adicione:

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
    volumes:
      - uploads:/app/uploads

volumes:
  postgres-data:
  uploads:
```

Salve o arquivo na raiz do projeto, junto ao `docker-compose.yml`.

### 6.2. Baixando a imagem publicada

Com o Docker Desktop aberto, execute:

```bash
docker pull ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend:latest
```

A imagem está publicada como pacote público no GHCR, portanto o download não exige autenticação.

### 6.3. Iniciando o ambiente

Certifique-se de que os arquivos `compose.prod.yaml` e `.env` estejam na raiz do projeto.

Execute:

```bash
docker compose -f compose.prod.yaml up -d
```

O Docker Compose iniciará o PostgreSQL e o backend utilizando a imagem publicada.

Caso a imagem ainda não esteja disponível localmente, o Docker poderá baixá-la automaticamente.

### 6.4. Verificando a execução

```bash
docker compose -f compose.prod.yaml ps
```

Para consultar os logs do backend:

```bash
docker compose -f compose.prod.yaml logs -f backend
```

O Swagger poderá ser acessado por:

```text
http://localhost:8080/swagger-ui/index.html
```

Caso a variável `BACKEND_PORT` tenha sido alterada, utilize a porta correspondente.

**Observação:** essa configuração permite executar o backend com a imagem publicada, mas não configura automaticamente um domínio público ou HTTPS. Para disponibilizar a aplicação na internet, ainda é necessário preparar a infraestrutura de hospedagem e segurança.

## 7. Utilizando a API

A aplicação disponibiliza uma interface Swagger para visualizar, consultar e testar os endpoints.

Acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

No ambiente de produção, utilize a porta configurada.

### Fluxo inicial de utilização

1. Acesse o Swagger UI.
2. Localize a operação responsável pelo cadastro de usuários.
3. Realize o cadastro utilizando o modelo de dados apresentado pela documentação.
4. Localize a operação de login e informe as credenciais cadastradas.
5. Obtenha o token JWT retornado pela autenticação.
6. Utilize o recurso **Authorize** do Swagger para informar o token no formato solicitado pela API.
7. Execute as operações protegidas e demais funcionalidades disponibilizadas pelo backend.

O Swagger apresenta os endpoints, métodos HTTP, parâmetros, modelos de requisição e respostas disponíveis na versão atual da aplicação.

As operações devem ser consultadas diretamente na documentação da API, pois podem sofrer alterações durante a evolução do projeto.

## 8. Persistência dos dados

O Docker Compose utiliza volumes persistentes para armazenar os dados do PostgreSQL e os arquivos enviados pela aplicação.

| Volume | Finalidade |
|---|---|
| `postgres-data` | Armazenamento persistente do PostgreSQL. |
| `uploads` | Armazenamento persistente dos arquivos enviados pela aplicação. |

Os dados permanecem armazenados mesmo quando os containers são reiniciados ou removidos sem a exclusão dos volumes.

Para reiniciar os serviços:

```bash
docker compose restart
```

Para parar e remover os containers e a rede:

```bash
docker compose down
```

**Atenção:** não utilize `docker compose down -v` sem necessidade. A opção `-v` remove os volumes e pode apagar os dados persistidos do banco e os arquivos enviados.

Cada instalação do Compose possui seus próprios volumes e seu próprio banco. Os dados não são compartilhados automaticamente entre máquinas diferentes.

## 9. Comandos úteis

| Comando | Finalidade |
|---|---|
| `docker compose up --build -d` | Constrói e inicia o ambiente de desenvolvimento. |
| `docker compose ps` | Lista os containers em execução. |
| `docker compose logs -f backend` | Acompanha os logs do backend. |
| `docker compose logs -f db` | Acompanha os logs do PostgreSQL. |
| `docker compose restart` | Reinicia os serviços. |
| `docker compose down` | Para e remove os containers e a rede, preservando os volumes. |

Para executar os mesmos comandos utilizando o arquivo de produção, acrescente:

```bash
-f compose.prod.yaml
```

Exemplo:

```bash
docker compose -f compose.prod.yaml ps
```

## 10. Integração contínua e publicação da imagem

O projeto utiliza GitHub Actions para automatizar o processo de integração contínua.

O workflow `CI Backend` executa etapas como:

- Configuração do ambiente Java.
- Execução do build e dos testes com Maven.
- Utilização do PostgreSQL para os testes.
- Construção da imagem Docker.
- Publicação da imagem no GitHub Container Registry, conforme as condições configuradas no workflow.

A imagem publicada utiliza o endereço:

```text
ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend
```

O workflow utiliza tags como `latest` e a identificação do commit.

Para atualizar uma instalação que utiliza o arquivo de produção, execute:

```bash
docker compose -f compose.prod.yaml pull
docker compose -f compose.prod.yaml up -d
```

Isso permite obter uma versão mais recente da imagem publicada e recriar o container do backend conforme necessário.

## 11. Segurança e recomendações

- Nunca publique arquivos `.env` ou credenciais no repositório.
- Utilize chaves JWT próprias e seguras em cada ambiente.
- Não exponha diretamente o PostgreSQL à internet.
- Utilize HTTPS ao disponibilizar a API publicamente.
- Configure adequadamente o CORS para as origens autorizadas.
- Mantenha backups dos volumes e dos dados importantes.
- Valide novas imagens antes de atualizar o ambiente.
- Considere uma estratégia de migração de banco de dados antes de atualizar versões em produção.

## 12. Solução de problemas

### Docker não está em execução

Verifique se o Docker Desktop está aberto e se o mecanismo Docker está funcionando.

### Arquivo `.env` não encontrado ou variável ausente

Verifique se o arquivo `.env` está na raiz do projeto e se contém todas as variáveis exigidas pelo Compose.

### Backend não inicia

Consulte os logs:

```bash
docker compose logs backend
```

Verifique também o estado dos containers:

```bash
docker compose ps
```

### Falha na conexão com o banco

Confira as credenciais do `.env`, a URL de conexão utilizada pelo backend e o estado do container PostgreSQL.

### Swagger não abre

Verifique se o backend está em execução, se a aplicação terminou de inicializar e se o endereço utilizado corresponde à porta configurada.

---

## Observação sobre implantação

A configuração com imagem publicada no GHCR permite executar o backend em outra máquina.

Para uma implantação pública definitiva, ainda é necessário configurar e verificar o servidor de hospedagem, HTTPS, firewall, backups, gerenciamento de segredos e estratégia de atualização do banco de dados.

A execução local da imagem Docker não deve ser confundida com a disponibilização pública do sistema.
