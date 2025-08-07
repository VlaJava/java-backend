output "app_url" {
  description = "The FQDN of the Container App."
  value       = azurerm_container_app.ca.latest_revision_fqdn
}

