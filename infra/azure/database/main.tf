resource "azurerm_resource_group" "db_resource_group" {
  name     = var.azure_resource_group
  location = var.azure_region
  tags = {
    owner      = "emanuelernesto"
    managed-by = "terraform"
  }
}

resource "azurerm_mssql_server" "database_server" {
  name                         = "sqlserver-viajava-emanuel"
  resource_group_name          = azurerm_resource_group.db_resource_group.name
  location                     = azurerm_resource_group.db_resource_group.location
  version                      = "12.0"
  administrator_login          = var.admin_username
  administrator_login_password = var.admin_password
}

resource "azurerm_mssql_database" "database_instance" {
  name           = "DB_VIAJAVA"
  server_id      = azurerm_mssql_server.database_server.id
  collation      = "SQL_Latin1_General_CP1_CI_AS"
  sku_name       = "S0"
  auto_pause_delay_in_minutes = 60
  min_capacity   = 0.5
  tags = {
    owner      = "emanuelernesto"
    managed-by = "terraform"
  }
}

resource "azurerm_mssql_firewall_rule" "allow_my_ip" {
  name             = "AllowMyPublicIP"
  server_id        = azurerm_mssql_server.database_server.id
  start_ip_address = var.allowed_ip
  end_ip_address   = var.allowed_ip
}

resource "azurerm_mssql_firewall_rule" "allow_azure_services" {
  name             = "AllowAllWindowsAzureIps"
  server_id        = azurerm_mssql_server.database_server.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}

resource "azurerm_virtual_network" "vm_vnet" {
  name                = "vnet-viajava"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name
}

resource "azurerm_subnet" "vm_subnet" {
  name                 = "subnet-default"
  resource_group_name  = azurerm_resource_group.db_resource_group.name
  virtual_network_name = azurerm_virtual_network.vm_vnet.name
  address_prefixes     = ["10.0.1.0/24"]
}

resource "azurerm_public_ip" "vm_public_ip" {
  name                = "pip-vm-viajava"
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name
  allocation_method   = "Static"
  sku                 = "Standard"
}

resource "azurerm_network_interface" "vm_nic" {
  name                = "nic-vm-viajava"
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.vm_subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.vm_public_ip.id
  }
}

resource "azurerm_linux_virtual_machine" "main_vm" {
  name                  = "vm-viajava-emanuel"
  resource_group_name   = azurerm_resource_group.db_resource_group.name
  location              = azurerm_resource_group.db_resource_group.location
  size                  = "Standard_D2ls_v5"
  admin_username        = var.admin_username
  network_interface_ids = [azurerm_network_interface.vm_nic.id]
  admin_password = var.admin_password
  disable_password_authentication = false


  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts-gen2"
    version   = "latest"
  }

  tags = {
    owner      = "emanuelernesto"
    managed-by = "terraform"
  }
}

resource "azurerm_network_security_group" "vm_nsg" {
  name                = "nsg-viajava"
  location            = var.azure_region
  resource_group_name = var.azure_resource_group

  security_rule {
    name                       = "AllowHTTP8080"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8080"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}


resource "azurerm_network_interface_security_group_association" "vm_nic_nsg" {
  network_interface_id      = azurerm_network_interface.vm_nic.id
  network_security_group_id = azurerm_network_security_group.vm_nsg.id
}
