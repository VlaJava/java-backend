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
}

module "s3_bucket" {
  source      = "./aws/s3"
  bucket_name = var.bucket_name
  aws_region  = var.aws_region
}


module "azure_sql_vm" {
  source               = "./azure/database"
  azure_region         = var.azure_region
  azure_resource_group = var.azure_resource_group
  vm_name              = var.vm_name
  admin_username       = var.vm_admin_username
  admin_password       = var.vm_admin_password
  allowed_ip           = var.allowed_ip
}
