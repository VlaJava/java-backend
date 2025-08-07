terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      owner      = "emanuelernesto"
      managed-by = "terraform"
    }
  }
}

provider "azurerm" {
  features {
    resource_group {
      prevent_deletion_if_contains_resources = false
    }
  }
  subscription_id = var.azure_subscription_id
}

module "s3_bucket" {
  source      = "./aws/s3"
  bucket_name = var.bucket_name
  aws_region  = var.aws_region
}


module "resource_group" {
  source              = "./azure/modules/resource_group"
  resource_group_name = var.azure_resource_group
  location            = var.azure_region
}

module "sql_server" {
  source              = "./azure/modules/sql_server"
  resource_group_name = module.resource_group.name
  location            = module.resource_group.location
  sql_server_name     = "sql-${var.azure_resource_group}"
  sql_database_name   = "DB_VIAJAVA"
  admin_login         = var.admin_login
  admin_password      = var.admin_password
  allowed_ip          = var.allowed_ip
}

module "container_app" {
  source                   = "./azure/modules/container_app"
  resource_group_name      = module.resource_group.name
  location                 = module.resource_group.location
  container_registry_name  = "acrviajavacontainer"
  container_app_env_name   = "cae-${var.azure_resource_group}"
  container_app_name       = "aca-${var.azure_resource_group}"
  docker_hub_image         = var.docker_hub_image
  docker_image_name        = "viajava-app"
  sql_server_fqdn          = module.sql_server.fqdn
  sql_db_name              = module.sql_server.db_name
  db_admin_login           = var.admin_login
  db_admin_password        = var.admin_password
  aws_access_key           = var.aws_access_key
  aws_secret_key           = var.aws_secret_key
  aws_region               = var.aws_region
  bucket_name              = var.bucket_name
  gateway_access_token     = var.gateway_access_token
  gemini_api_key           = var.gemini_api_key
  mail_password            = var.mail_password
  mail_username            = var.mail_username
  spring_profiles_active   = var.spring_profiles_active
  server_base_url          = var.server_base_url
  redirect_url             = var.redirect_url
  frontend_url             = var.frontend_url
  frontend_bookings_url    = var.frontend_bookings_url
  gateway_notification_url = var.gateway_notification_url
  frontend_success_url     = var.frontend_success_url
  frontend_fail_url        = var.frontend_fail_url
  frontend_pending_url     = var.frontend_pending_url
}
