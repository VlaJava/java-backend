output "azure_vm_public_ip" {
  description = "Public IP of VM"
  value       = azurerm_public_ip.db_public_ip.ip_address
}

output "azure_sql_admin_user" {
  description = "Admin user of database instance."
  value       = var.admin_username
}

output "azure_sql_admin_password" {
  description = "Password of admin user"
  value       = var.admin_password
  sensitive   = true
}
