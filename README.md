# ViaJava - Sistema de Gerenciamento de Viagens

## 📋 Visão do Projeto
O ViaJava é uma aplicação completa para gerenciamento de pacotes turísticos, reservas e viagens, desenvolvida com as melhores práticas de engenharia de software.

## 🛠️ Tecnologias

### Backend
- **Linguagem Principal**: Java 21
- **Framework**: Spring Boot 3.5
- **Segurança**: Spring Security
- **Template Engine**: Thymeleaf

### Banco de Dados
- **Sistema Gerenciador**: SQL Server
- **Cache**: *Próximos passos*: Redis

### Infraestrutura
- **Containerização**: Docker
- **IaC**: Terraform
- **Cloud Providers**:
  - **AWS**: S3 (Armazenamento de objetos)
  - **Azure**: 
    - Azure Container Apps
    - Azure MSSQL Server
    - Resource Group

### Padrões de Projeto
- Strategy
- Factory
- Ports and Adapters

### Pagamentos
- **Gateway de Pagamento**: Mercado Pago

## 🚀 Pré-requisitos

### Desenvolvimento Local
- Java 21 JDK
- SQL Server em execução localmente ou em container
- Maven para gerenciamento de dependências

### Cloud
- AWS CLI configurada
- Azure CLI instalada e configurada
- Acesso às contas AWS e Azure

## ⚙️ Configuração

### 1. Configuração do Banco de Dados
1. Acesse [SQL Scripts](https://github.com/VlaJava/sql-scripts)
2. Execute os scripts na seguinte ordem:
   - `01_create_tables.sql`
   - `02_create_payment_triggers.sql`
   - `03_create_admin_views.sql`
   - `04_inserts.sql`
   - `07_create_indexes.sql`

### 2. Configuração do Ambiente Cloud
### 2. Geração de Chaves RSA para Autenticação

A aplicação utiliza chaves RSA para assinar e verificar tokens JWT. Siga estes passos para gerar o par de chaves:

```bash
# Navegue até o diretório de recursos do projeto
cd src/main/resources/

# Gere a chave privada (manter em segredo!)
openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:4096

# Extraia a chave pública correspondente
openssl rsa -pubout -in app.key -out app.pub

# (Opcional) Converter para formato PKCS#8 (se necessário)
openssl pkcs8 -topk8 -in app.key -out app-pkcs8.key -nocrypt

# Ajuste as permissões para proteger as chaves
chmod 600 app.key app.pub
```

> **Importante**:
> - Mantenha o arquivo `app.key` em segredo e NÃO o compartilhe

### 3. Configuração do Ambiente Cloud
Crie um arquivo `terraform.tfvars` na pasta `infra` com as seguintes variáveis:

```hcl
# AWS
bucket_name     = ""
aws_region      = ""

# Azure
azure_resource_group  = ""
azure_region          = ""
admin_login           = ""
admin_password        = ""
allowed_ip            = ""
azure_subscription_id = ""

# Spring API
aws_access_key       = ""
aws_secret_key       = ""
gateway_access_token = ""
gemini_api_key       = ""
mail_password        = ""
mail_username        = ""
```

## 🚀 Executando o Projeto

### Localmente
1. Configure as seguintes variáveis de ambiente:
   ```bash
   # AWS Configuration
   export AWS_ACCESS_KEY=your_access_key
   export AWS_SECRET_KEY=your_secret_key
   export AWS_REGION=your_aws_region
   export BUCKET_NAME=your_bucket_name
   
   # Database Configuration
   export DATABASE_URL=jdbc:sqlserver://localhost:1433;databaseName=viajava
   export DB_USER=your_db_username
   export DB_PASSWORD=your_db_password
   
   # API Configuration
   export GATEWAY_ACCESS_TOKEN=your_gateway_token
   export GEMINI_API_KEY=your_gemini_key
   
   # Email Configuration
   export MAIL_USERNAME=your_email@example.com
   export MAIL_PASSWORD=your_email_password
   ```

2. Execute o comando:
   ```bash
   mvn spring-boot:run
   ```
   ou
   ```bash
   ./mvnw spring-boot:run
   ```

### Na Nuvem
1. Navegue até a pasta `infra`
2. Inicialize o Terraform:
   ```bash
   terraform init
   ```
3. Aplique a infraestrutura:
   ```bash
   terraform apply -auto-approve
   ```

## 🚧 Próximos Passos

### Melhorias de Infraestrutura
- [ ] Implementar Redis para cache
- [ ] Configurar CD/CI completo

### Melhorias de Código
- [ ] Refatorar para Arquitetura Hexagonal
- [ ] Implementar testes unitários
- [ ] Adicionar testes de integração
- [ ] Criar testes end-to-end
- [ ] Desenvolver testes de carga

### Novas Funcionalidades
- [ ] Endpoints para gerenciamento de vôos
- [ ] Endpoints para gerenciamento de hotéis
-

## 📄 Licença
Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e enviar pull requests.

## 📬 Contato
Para mais informações, entre em contato com a equipe de desenvolvimento.
