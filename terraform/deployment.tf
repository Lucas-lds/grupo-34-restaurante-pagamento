### DEPLOYMENT PARA O SERVIÇO DE PAGAMENTO ###
resource "kubernetes_deployment" "pagamento-api" {
  depends_on = [aws_eks_cluster.eks-cluster, aws_eks_node_group.eks-cluster]

  metadata {
    name = "pagamento"
    labels = {
      app = "pagamento"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "pagamento"
      }
    }

    template {
      metadata {
        labels = {
          app = "pagamento"
        }
      }

      spec {
        service_account_name = kubernetes_service_account.db_pagamento_mysql_service_account.metadata[0].name

        container {
          name  = "pagamento"
          image = "717279688908.dkr.ecr.us-east-1.amazonaws.com/repositorio-pagamento:v1"

          env {
            name  = "RDS_ENDPOINT"
            value = aws_db_instance.mysql_rds_pagamento.endpoint # Referência ao endpoint
          }

          resources {
            limits = {
              cpu    = "1"
              memory = "1Gi"
            }
            requests = {
              cpu    = "500m"
              memory = "250Mi"
            }
          }

          port {
            container_port = 8080
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.db_pagamento_secret.metadata[0].name
            }
          }

          liveness_probe {
            http_get {
              path = "/api/v1/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 10
            timeout_seconds       = 5
            failure_threshold     = 3
          }
        }
      }
    }
  }
}