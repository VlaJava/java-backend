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

output "sqlserver_vm_ip" {
  value = module.azure_sql_vm.azure_vm_public_ip
}

output "sqlserver_vm_admin_username" {
  value = var.vm_admin_username
}