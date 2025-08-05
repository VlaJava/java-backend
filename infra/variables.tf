variable "aws_region" {
  type        = string
  default     = "us-east-1"
  description = "The region where the infrastructure will be created"
}

variable "bucket_name" {
  type = string
}

variable "vm_admin_username" {
  type        = string
  description = "Admin username for Azure VM"
}

variable "vm_admin_password" {
  type        = string
  description = "Admin password for Azure VM"
  sensitive   = true
}

variable "azure_region" {
  type    = string
  default = "Region where Azure resources will be created"
}


variable "azure_resource_group" {
  type        = string
  description = "Name of the Azure resource group."
}


variable "allowed_ip" {
  type        = string
  description = "IP that can access the database instance"
}

variable "vm_name" {
  type        = string
  description = "Name of the VM that hosts the database."
}