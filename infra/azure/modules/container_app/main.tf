resource "azurerm_log_analytics_workspace" "law" {
  name                = "logs-viajava"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = "PerGB2018"
  retention_in_days   = 30
}

resource "azurerm_container_app_environment" "cae" {
  name                       = var.container_app_env_name
  location                   = var.location
  resource_group_name        = var.resource_group_name
  log_analytics_workspace_id = azurerm_log_analytics_workspace.law.id
}

resource "azurerm_container_app" "ca" {
  name                         = var.container_app_name
  container_app_environment_id = azurerm_container_app_environment.cae.id
  resource_group_name          = var.resource_group_name
  revision_mode                = "Single"

  identity {
    type = "SystemAssigned"
  }

  secret {
    name  = "db-password"
    value = var.db_admin_password
  }
  secret {
    name  = "aws-access-key"
    value = var.aws_access_key
  }
  secret {
    name  = "aws-secret-key"
    value = var.aws_secret_key
  }
  secret {
    name  = "gateway-access-token"
    value = var.gateway_access_token
  }
  secret {
    name  = "gemini-api-key"
    value = var.gemini_api_key
  }
  secret {
    name  = "mail-password"
    value = var.mail_password
  }

  ingress {
    external_enabled = true
    target_port      = 8080
    transport        = "http"

    traffic_weight {
      percentage      = 100
      latest_revision = true
    }
  }

  template {
    container {
      name   = var.docker_image_name
      image  = var.docker_hub_image
      cpu    = 0.25
      memory = "0.5Gi"

      env {
        name  = "DATABASE_URL"
        value = "${var.sql_server_fqdn}:1433;databaseName=${var.sql_db_name};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
      }
      env {
        name  = "DB_USER"
        value = var.db_admin_login
      }
      env {
        name        = "DB_PASSWORD"
        secret_name = "db-password"
      }
      env {
        name  = "AWS_REGION"
        value = var.aws_region
      }
      env {
        name        = "AWS_ACCESS_KEY"
        secret_name = "aws-access-key"
      }
      env {
        name        = "AWS_SECRET_KEY"
        secret_name = "aws-secret-key"
      }
      env {
        name  = "BUCKET_NAME"
        value = var.bucket_name
      }
      env {
        name        = "GATEWAY_ACCESS_TOKEN"
        secret_name = "gateway-access-token"
      }
      env {
        name        = "GEMINI_API_KEY"
        secret_name = "gemini-api-key"
      }
      env {
        name  = "MAIL_USERNAME"
        value = var.mail_username
      }
      env {
        name        = "MAIL_PASSWORD"
        secret_name = "mail-password"
      }
      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = var.spring_profiles_active
      }
      env {
        name  = "SERVER_BASE_URL"
        value = var.server_base_url
      }
      env {
        name  = "REDIRECT_URL"
        value = var.redirect_url
      }
      env {
        name  = "FRONTEND_URL"
        value = var.frontend_url
      }
      env {
        name  = "FRONTEND_BOOKINGS_URL"
        value = var.frontend_bookings_url
      }
      env {
        name  = "GATEWAY_NOTIFICATION_URL"
        value = var.gateway_notification_url
      }
      env {
        name  = "FRONTEND_SUCCESS_URL"
        value = var.frontend_success_url
      }
      env {
        name  = "FRONTEND_FAIL_URL"
        value = var.frontend_fail_url
      }
      env {
        name  = "FRONTEND_PENDING_URL"
        value = var.frontend_pending_url
      }
    }
  }
}