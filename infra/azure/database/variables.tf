variable "azure_region" {
  type        = string
  description = "Region of Resources"
}

variable "azure_resource_group" {
  type        = string
  description = "Name of resource group"
}

variable "vm_name" {
  type        = string
  description = "Name of virtual machine"
}

variable "admin_username" {
  type        = string
  description = "Admin user of VM"
}

variable "admin_password" {
  type        = string
  description = "Admin password of VM"
  sensitive   = true
}

variable "allowed_ip" {
  type        = string
  description = "IP Address that can access instance."
}

