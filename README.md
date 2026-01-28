## To-Do List API (Spring Boot)

API REST para gerenciamento de tarefas (criar, listar, atualizar, excluir) seguindo Clean Architecture.

### Requisitos
- Java 17 (`/opt/homebrew/opt/openjdk@17` em macOS/arm64)
- Maven Wrapper (já incluído)
- Para SQL Server: Docker

### Perfis disponíveis
- `local`: H2 em memória (não precisa de SQL Server)
- `default`: SQL Server (usa variáveis de ambiente ou valores padrão)

### Subir local com H2 (perfil `local`)
```bash
cd api-spring
PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" JAVA_HOME="/opt/homebrew/opt/openjdk@17" \
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
Banco em memória criado automaticamente; Flyway desabilitado.

### Subir com SQL Server (perfil padrão)
1) Suba o banco via Docker:
```bash
docker run -d --name mssql \
  --platform linux/amd64 \
  -e 'ACCEPT_EULA=Y' \
  -e 'MSSQL_SA_PASSWORD=YourStrong!Passw0rd' \
  -e 'MSSQL_PID=Express' \
  -p 1433:1433 \
  -v mssql-data:/var/opt/mssql \
  mcr.microsoft.com/mssql/server:2022-latest
```
2) Exporte as variáveis se quiser customizar (opcional, já há defaults em `application.yml`):
```bash
export DB_HOST=localhost
export DB_PORT=1433
export DB_NAME=todo_db
export DB_USERNAME=sa
export DB_PASSWORD=YourStrong!Passw0rd
```
3) Suba a API:
```bash
cd api-spring
PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" JAVA_HOME="/opt/homebrew/opt/openjdk@17" \
./mvnw spring-boot:run
```
Flyway aplica `V1__create_tasks_table.sql` no banco `todo_db`.

### Endpoints principais
- `POST /tasks` — cria tarefa (`title`, `description`, `status`)
- `GET /tasks` — lista tarefas
- `GET /tasks/{id}` — busca por id
- `PUT /tasks/{id}` — atualiza tarefa
- `DELETE /tasks/{id}` — remove tarefa

Status válidos: `PENDING`, `IN_PROGRESS`, `COMPLETED`.

### Swagger / OpenAPI
- UI: `http://localhost:8080/swagger-ui.html`
- Docs JSON: `http://localhost:8080/v3/api-docs`

### Deploy em ECS Fargate com ALB (Terraform)
Arquivos em `infra/terraform` criam:
- VPC pública, ALB, Target Group, SGs
- Repositório ECR
- Cluster ECS Fargate, Task Definition, Service com ALB

Passos rápidos:
1) Autentique na AWS (`aws configure`) e escolha a região (var `aws_region`, padrão `us-east-1`).
2) Build/push da imagem:
```bash
cd api-spring
IMAGE_TAG=latest
ECR_REPO=$(aws ecr describe-repositories --query 'repositories[?repositoryName==`todo-api`].repositoryUri' --output text || echo "")
if [ -z "$ECR_REPO" ]; then
  aws ecr create-repository --repository-name todo-api
  ECR_REPO=$(aws ecr describe-repositories --query 'repositories[?repositoryName==`todo-api`].repositoryUri' --output text)
fi
aws ecr get-login-password | docker login --username AWS --password-stdin "${ECR_REPO%/*}"
docker build -t "$ECR_REPO:$IMAGE_TAG" .
docker push "$ECR_REPO:$IMAGE_TAG"
```
3) Provisionar ECS/ALB via Terraform:
```bash
cd infra/terraform
terraform init
terraform apply -var="image_tag=$IMAGE_TAG" -auto-approve
```
4) Após aplicar, acesse o ALB em `alb_dns_name` de `terraform output`. Swagger: `http://<alb_dns_name>/swagger-ui.html`

Variáveis úteis (`infra/terraform/variables.tf`):
- `project_name`, `aws_region`, `container_port`, `desired_count`, `task_cpu`, `task_memory`
- DB envs: `db_host`, `db_port`, `db_name`, `db_username`, `db_password`

### Testes
```bash
cd api-spring
PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" JAVA_HOME="/opt/homebrew/opt/openjdk@17" \
./mvnw test
```
