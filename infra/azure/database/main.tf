resource "azurerm_resource_group" "db_resource_group" {
  name     = var.azure_resource_group
  location = var.azure_region
  tags = {
    owner: "emanuelernesto"
    managed-by: "terraform"
  }
}

resource "azurerm_virtual_network" "db_virtual_network" {
  name                = "${var.vm_name}-vnet"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name
}

resource "azurerm_subnet" "db_subnet" {
  name                 = "${var.vm_name}-subnet"
  resource_group_name  = azurerm_resource_group.db_resource_group.name
  virtual_network_name = azurerm_virtual_network.db_virtual_network.name
  address_prefixes     = ["10.0.1.0/24"]
}

resource "azurerm_network_security_group" "db_security_group" {
  name                = "${var.vm_name}-nsg"
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name

  security_rule {
    name                       = "AllowSQL"
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "1433"
    source_address_prefix      = var.allowed_ip
    destination_address_prefix = "*"
  }
}

resource "azurerm_public_ip" "db_public_ip" {
  name                = "${var.vm_name}-ip"
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name
  allocation_method   = "Dynamic"
}

resource "azurerm_network_interface" "db_network_interface" {
  name                = "${var.vm_name}-nic"
  location            = azurerm_resource_group.db_resource_group.location
  resource_group_name = azurerm_resource_group.db_resource_group.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.db_subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.db_public_ip.id
  }
}

resource "azurerm_network_interface_security_group_association" "db_network_group_association" {
  network_interface_id      = azurerm_network_interface.db_network_interface.id
  network_security_group_id = azurerm_network_security_group.db_security_group.id
}

resource "azurerm_windows_virtual_machine" "this" {
  name                = var.vm_name
  resource_group_name = azurerm_resource_group.db_resource_group.name
  location            = azurerm_resource_group.db_resource_group.location
  size                = "Standard_B1s"
  admin_username      = var.admin_username
  admin_password      = var.admin_password
  network_interface_ids = [
    azurerm_network_interface.db_network_interface.id
  ]

  os_disk {
    name                 = "${var.vm_name}-osdisk"
    caching              = "ReadWrite"
    storage_account_type = "StandardSSD_LRS"
  }

  source_image_reference {
    publisher = "microsoftsqlserver"
    offer     = "sql2022-dev"
    sku       = "sql2022-dev-ws2022"
    version   = "latest"
  }
}

resource "azurerm_dev_test_schedule" "auto_shutdown" {
  name                 = "shutdown-computevm"
  location             = azurerm_resource_group.db_resource_group.location
  resource_group_name  = azurerm_resource_group.db_resource_group.name
  lab_name             = "AutoShutdownLab"
  status               = "Enabled"

  daily_recurrence {
    time = "2000"
  }
  time_zone_id = "E. South America Standard Time"
  notification_settings {
    status = "Disabled"
  }
  task_type = "LabVmsShutdownTask"
}