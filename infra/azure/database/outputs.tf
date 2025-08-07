output "azure_sql_admin_user" {
  description = "Admin user of database instance."
  value       = var.admin_username
}

output "azure_sql_admin_password" {
  description = "Password of admin user"
  value       = var.admin_password
  sensitive   = true
}

output "sql_connection_string" {
  description = "Database Connection string."
  value = "jdbc:sqlserver://${azurerm_mssql_server.database_server.fully_qualified_domain_name}:1433;databaseName=${azurerm_mssql_database.database_instance.name};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
  sensitive   = true
}

output "vm_public_ip" {
  description = "Public IP of VM"
  value       = azurerm_public_ip.vm_public_ip.ip_address
}
