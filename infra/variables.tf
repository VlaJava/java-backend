variable "aws_region" {
  type        = string
  default     = "us-east-1"
  description = "The region where the infrastructure will be created"
}

variable "bucket_name" {
  type = string
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

variable "azure_subscription_id" {
  type = string
}

variable "aws_access_key" {
  description = "AWS Access Key for the application."
  type        = string
  sensitive   = true
}

variable "aws_secret_key" {
  description = "AWS Secret Key for the application."
  type        = string
  sensitive   = true
}

variable "gateway_access_token" {
  description = "Gateway Access Token for the application."
  type        = string
  sensitive   = true
}

variable "gemini_api_key" {
  description = "Gemini API Key for the application."
  type        = string
  sensitive   = true
}

variable "mail_password" {
  description = "Mail Password for the application."
  type        = string
  sensitive   = true
}

variable "mail_username" {
  description = "Mail Username for the application."
  type        = string
}

variable "spring_profiles_active" {
  description = "Spring profiles to activate for the application."
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
  type        = string
  description = "Name of the Docker image in docker hub."
}