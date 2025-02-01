### SERVICE PARA O SERVIÇO DE PAGAMENTO ###
resource "kubernetes_service" "pagamento_service" {
  metadata {
    name = "pagamento-service"
    labels = {
      app = "pagamento"
    }
  }

  spec {
    selector = {
      app = "pagamento"
    }

    port {
      port        = 80
      target_port = 8080
    }

    type = "ClusterIP"
  }
}