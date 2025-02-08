variable "region" {
  description = "Região da AWS"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "Nome do cluster EKS"
  type        = string
  default = "restaurante-cluster2"
}

variable "mysql_user_pagamento" {
  description = "MySQL User para o serviço de pagamento"
  type        = string
  default     = "pagamento_user"
}

variable "mysql_password_pagamento" {
  description = "MySQL Password para o serviço de pagamento"
  type        = string
  default     = "pagamento_user_pass"
}

variable "mysql_database_pagamento" {
  description = "MySQL Database para o serviço de pagamento"
  type        = string
  default     = "pagamento_db"
}