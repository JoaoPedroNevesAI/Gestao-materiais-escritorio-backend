# Gestão de Materiais de Escritório — Backend

Backend do sistema de Gestão de Materiais de Escritório, desenvolvido com Java e Spring Boot. A aplicação disponibiliza uma API REST para gerenciamento dos dados do sistema, utilizando PostgreSQL como banco de dados e JWT para autenticação.

## Tecnologias utilizadas

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

## Requisitos

Para executar o backend, é necessário ter:

- Git, para clonar o repositório.
- Docker Desktop instalado e em execução.
- Docker Compose disponível por meio do Docker.
- Acesso à internet para baixar as imagens necessárias.

Para desenvolver e executar o projeto diretamente pelo Java, também é necessário ter o JDK 21 e o Maven (ou utilizar o Maven Wrapper, caso esteja disponível no projeto).

## 1. Obtendo o projeto

Clone o repositório:

```bash
git clone <URL_DO_REPOSITORIO>
```

Entre na pasta do projeto:

```bash
cd Gestao-materiais-escritorio-backend
```

Abra a pasta no Visual Studio Code ou na IDE de sua preferência.

## 2. Configuração das variáveis de ambiente

Antes de iniciar a aplicação, é necessário criar um arquivo `.env` na raiz do projeto.

Estrutura esperada:

```text
Gestao-materiais-escritorio-backend/
├── src/
├── Dockerfile
├── docker-compose.yml
├── compose.prod.yaml
├── pom.xml
├── .gitignore
└── .env
```

Conteúdo do arquivo `.env`:

```env
POSTGRES_PASSWORD=sua_senha_do_banco
JWT_SECRET_KEY=sua_chave_secreta_jwt
BACKEND_PORT=8080
```

### Finalidade das variáveis

| Variável | Descrição |
|---|---|
| `POSTGRES_PASSWORD` | Senha utilizada pelo PostgreSQL. |
| `JWT_SECRET_KEY` | Chave secreta utilizada pelo backend na autenticação JWT. |
| `BACKEND_PORT` | Porta externa utilizada pelo backend na configuração de produção. |

Os valores apresentados são exemplos de configuração. Cada ambiente deve utilizar suas próprias credenciais e uma chave JWT segura e compatível com a configuração da aplicação.

**Importante:** o arquivo `.env` contém informações privadas e não deve ser enviado ao GitHub. Verifique se ele está incluído no `.gitignore`.

## 3. Executando em ambiente de desenvolvimento

O arquivo `docker-compose.yml` configura a execução do backend e do PostgreSQL utilizando o Dockerfile do próprio projeto.

Na raiz do repositório, execute:

```bash
docker compose up --build -d
```

Esse comando:

1. Constrói a imagem Docker do backend a partir do código-fonte.
2. Cria os containers necessários.
3. Inicia o PostgreSQL.
4. Aguarda o banco de dados ficar saudável.
5. Inicia o backend.

### Verificando os containers

Para visualizar os containers em execução:

```bash
docker compose ps
```

Para consultar os logs do backend:

```bash
docker compose logs -f backend
```

Para consultar os logs do banco de dados:

```bash
docker compose logs -f db
```

### Endereços do ambiente de desenvolvimento

Com a configuração padrão do `docker-compose.yml`:

| Serviço | Endereço |
|---|---|
| Backend | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| PostgreSQL | `localhost:5433` |

Dentro da rede Docker, o backend acessa o PostgreSQL pelo nome do serviço `db`, utilizando a porta interna do banco.

## 4. Executando com a imagem publicada no GHCR

O projeto também possui uma imagem Docker publicada no GitHub Container Registry (GHCR).

Imagem:

```text
ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend:latest
```

Essa modalidade permite executar o backend em outra máquina sem precisar compilar o código-fonte ou instalar Java e Maven.

O arquivo `compose.prod.yaml` utiliza essa imagem publicada, em vez de construir o backend localmente.

### 4.1. Baixando a imagem

Com o Docker Desktop aberto, execute:

```bash
docker pull ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend:latest
```

O pacote está publicado no GitHub Container Registry como público, portanto o download da imagem não exige autenticação.

### 4.2. Iniciando o ambiente

Certifique-se de que o arquivo `compose.prod.yaml` e o `.env` estejam na raiz do projeto.

Execute:

```bash
docker compose -f compose.prod.yaml up -d
```

O Docker Compose iniciará o PostgreSQL e o backend utilizando a imagem publicada.

Caso a imagem ainda não esteja disponível localmente, o Docker poderá baixá-la automaticamente.

### 4.3. Verificando a execução

```bash
docker compose -f compose.prod.yaml ps
```

Para consultar os logs do backend:

```bash
docker compose -f compose.prod.yaml logs -f backend
```

O Swagger poderá ser acessado pelo endereço:

```text
http://localhost:<BACKEND_PORT>/swagger-ui/index.html
```

O valor de `BACKEND_PORT` é definido no `.env`. Caso não seja informado, a configuração utiliza a porta padrão definida no `compose.prod.yaml`.

**Observação:** o `compose.prod.yaml` prepara a execução da aplicação com a imagem publicada, mas não configura sozinho um domínio público ou HTTPS. Para disponibilizar o sistema na internet, ainda é necessário preparar a infraestrutura de hospedagem e segurança.

## 5. Utilizando a API

A aplicação disponibiliza uma interface Swagger para visualizar, consultar e testar os endpoints da API.

Acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

No ambiente executado com `compose.prod.yaml`, utilize a porta configurada para esse ambiente.

### Fluxo inicial de utilização

1. Acesse o Swagger UI.
2. Localize a operação responsável pelo cadastro de usuários.
3. Realize o cadastro de um usuário utilizando o modelo de dados apresentado pela documentação.
4. Localize a operação de login e informe as credenciais cadastradas.
5. Obtenha o token JWT retornado pela autenticação.
6. Utilize o recurso **Authorize** do Swagger para informar o token no formato solicitado pela API.
7. Execute as operações protegidas e demais funcionalidades disponibilizadas pelo backend.

O Swagger apresenta os endpoints, métodos HTTP, parâmetros, modelos de requisição e respostas disponíveis na versão atual da aplicação.

As operações disponíveis devem ser consultadas diretamente na documentação da API, pois podem sofrer alterações durante a evolução do projeto.

## 6. Persistência dos dados

O Docker Compose utiliza volumes persistentes para armazenar os dados do PostgreSQL e os arquivos enviados pela aplicação.

Volumes configurados:

| Volume | Finalidade |
|---|---|
| `postgres-data` | Armazenamento persistente do banco PostgreSQL. |
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

Cada instalação do Compose possui seus próprios volumes e seu próprio banco de dados. Os dados não são compartilhados automaticamente entre máquinas diferentes.

## 7. Comandos úteis do Docker Compose

| Comando | Finalidade |
|---|---|
| `docker compose up --build -d` | Constrói e inicia o ambiente de desenvolvimento. |
| `docker compose ps` | Lista os containers do ambiente. |
| `docker compose logs -f backend` | Acompanha os logs do backend. |
| `docker compose logs -f db` | Acompanha os logs do PostgreSQL. |
| `docker compose restart` | Reinicia os serviços. |
| `docker compose down` | Para e remove os containers e a rede, preservando os volumes. |

Para a configuração de produção, os mesmos comandos podem ser utilizados acrescentando:

```bash
-f compose.prod.yaml
```

Por exemplo:

```bash
docker compose -f compose.prod.yaml ps
```

## 8. Integração contínua e publicação da imagem

O projeto utiliza GitHub Actions para automatizar a integração contínua.

O workflow `CI Backend` realiza etapas como:

- Configuração do ambiente Java.
- Execução do build e dos testes com Maven.
- Utilização do PostgreSQL para os testes.
- Construção da imagem Docker.
- Publicação da imagem no GitHub Container Registry, conforme as condições configuradas no workflow.

A imagem publicada é identificada pelo seguinte endereço:

```text
ghcr.io/joaopedronevesai/gestao-materiais-escritorio-backend
```

São utilizadas as tags configuradas no workflow, incluindo `latest` e a identificação do commit.

Isso permite que outras máquinas obtenham a imagem diretamente do GHCR, sem precisar reconstruir o backend.

## 9. Segurança e recomendações

- Nunca publique arquivos `.env` ou credenciais no repositório.
- Utilize chaves JWT próprias e seguras em cada ambiente.
- Não exponha diretamente o PostgreSQL à internet.
- Utilize HTTPS ao disponibilizar a API publicamente.
- Configure adequadamente as permissões de acesso e o CORS para os clientes autorizados.
- Mantenha backups dos volumes e dos dados importantes.
- Antes de atualizar a aplicação, valide a nova imagem e a compatibilidade com o banco de dados.

## 10. Solução de problemas

### Docker não está em execução

Verifique se o Docker Desktop está aberto e se o mecanismo Docker está funcionando.

### Arquivo `.env` não encontrado ou variável ausente

Verifique se o arquivo `.env` está na raiz do projeto e se contém as variáveis exigidas pelo Compose.

### Backend não inicia

Consulte os logs:

```bash
docker compose logs backend
```

Verifique também se o PostgreSQL está saudável:

```bash
docker compose ps
```

### Falha na conexão com o banco

Confira as credenciais do `.env`, a URL de conexão utilizada pelo backend e o estado do container PostgreSQL.

### Swagger não abre

Verifique se o container do backend está em execução, se a aplicação terminou de inicializar e se o endereço utilizado corresponde à porta configurada.

---

## Observação sobre o ambiente de produção

A configuração com imagem publicada no GHCR permite executar o backend em uma máquina diferente daquela utilizada no desenvolvimento.

Para uma implantação pública definitiva, ainda devem ser configurados e verificados o servidor de hospedagem, HTTPS, firewall, backups, gerenciamento de segredos e estratégia de atualização do banco de dados.

A execução local da imagem Docker não deve ser confundida com a disponibilização pública do sistema.
