output "app_url" {
  description = "The FQDN of the Container App."
  value       = azurerm_container_app.ca.latest_revision_fqdn
}

output "registry_login_server" {
  description = "The login server for the Container Registry."
  value       = azurerm_container_registry.acr.login_server
}
