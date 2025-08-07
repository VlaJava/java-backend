output "fqdn" {
  description = "The fully qualified domain name of the SQL Server."
  value       = azurerm_mssql_server.sql_server.fully_qualified_domain_name
}

output "db_name" {
  description = "The name of the SQL database."
  value       = azurerm_mssql_database.sql_db.name
}
