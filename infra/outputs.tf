output "bucket_name" {
  description = "Name of the s3 bucket"
  value       = module.s3_bucket.bucket_name
}

output "bucket_arn" {
  description = "Arn of s3 bucket"
  value       = module.s3_bucket.bucket_arn
}

output "bucket_region" {
  description = "Region of s3 bucket"
  value       = module.s3_bucket.bucket_region
}

output "application_url" {
  description = "The URL of the deployed application."
  value       = module.container_app.app_url
}

output "container_registry_login_server" {
  description = "The login server for the Azure Container Registry."
  value       = module.container_app.registry_login_server
}