variable "resource_group_name" {
  type        = string
  description = "The name of the resource group."
}

variable "location" {
  type        = string
  description = "The Azure region."
}

variable "sql_server_name" {
  type        = string
  description = "The name of the SQL Server."
}

variable "sql_database_name" {
  type        = string
  description = "The name of the SQL database."
}

variable "admin_login" {
  type        = string
  description = "The administrator login for the SQL Server."
}

variable "admin_password" {
  type        = string
  description = "The administrator password for the SQL Server."
  sensitive   = true
}

variable "allowed_ip" {
  type        = string
  description = "The IP address to allow through the firewall."
}
