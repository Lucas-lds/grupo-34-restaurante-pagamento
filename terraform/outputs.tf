### OUTPUT PARA O SERVIÇO DE PAGAMENTO ###
output "pagamento_service_url" {
  value = kubernetes_service.pagamento_service.metadata[0].name
}
