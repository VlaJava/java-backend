variable "resource_group_name" {
  type        = string
  description = "The name of the resource group."
}

variable "location" {
  type        = string
  description = "The Azure region."
}

variable "container_registry_name" {
  type        = string
  description = "Name for the Azure Container Registry."
}

variable "container_app_env_name" {
  type        = string
  description = "Name for the Azure Container App Environment."
}

variable "container_app_name" {
  type        = string
  description = "Name for the Azure Container App."
}

variable "docker_image_name" {
  type        = string
}

variable "sql_server_fqdn" {
  type        = string
  description = "The FQDN of the SQL server."
}

variable "sql_db_name" {
  type        = string
  description = "The name of the SQL database."
}

variable "db_admin_login" {
  type        = string
  description = "The admin login for the database."
}

variable "db_admin_password" {
  description = "The administrator password for the database."
  type        = string
  sensitive   = true
}

variable "aws_access_key" {
  description = "AWS Access Key."
  type        = string
  sensitive   = true
}

variable "aws_secret_key" {
  description = "AWS Secret Key."
  type        = string
  sensitive   = true
}

variable "aws_region" {
  description = "AWS Region."
  type        = string
}

variable "bucket_name" {
  description = "S3 Bucket Name."
  type        = string
}

variable "gateway_access_token" {
  description = "Gateway Access Token."
  type        = string
  sensitive   = true
}

variable "gemini_api_key" {
  description = "Gemini API Key."
  type        = string
  sensitive   = true
}

variable "mail_password" {
  description = "Mail Password."
  type        = string
  sensitive   = true
}

variable "mail_username" {
  description = "Mail Username."
  type        = string
}

variable "spring_profiles_active" {
  description = "Spring profiles to activate."
  type        = string
  default     = "prod"
}

variable "server_base_url" {
  description = "Base URL for the server"
  type        = string
}

variable "redirect_url" {
  description = "Redirect URL for OAuth or other flows"
  type        = string
}

variable "frontend_url" {
  description = "URL of the frontend application"
  type        = string
}

variable "frontend_bookings_url" {
  description = "URL for the bookings page on the frontend"
  type        = string
}

variable "gateway_notification_url" {
  description = "Notification URL for the payment gateway"
  type        = string
}

variable "frontend_success_url" {
  description = "Success URL for the frontend after payment"
  type        = string
}

variable "frontend_fail_url" {
  description = "Fail URL for the frontend after payment"
  type        = string
}

variable "frontend_pending_url" {
  description = "Pending URL for the frontend after payment"
  type        = string
}

variable "docker_hub_image" {
  type = string
}