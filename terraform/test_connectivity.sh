#!/bin/bash

# Pods
pods=("pagamento-54647b8dc4-k784m" "pagamento-54647b8dc4-r7scv" "pedido-7dc77b8d5d-mj6bc" "produto-5d8cccbc58-qnbwd" "restaurante-67b9d66847-k9vlf" "restaurante-67b9d66847-x7n5s")

# Serviços e seus endpoints de saúde
declare -A services
services=(
  ["pagamento-service"]="http://pagamento-service:80/api/v1/actuator/health"
  ["pedido-service"]="http://pedido-service:80/api/v1/actuator/health"
  ["produto-service"]="http://produto-service:80/api/v1/actuator/health"
  ["restaurante-service"]="http://restaurante-service:80/api/v1/actuator/health"
)

# Testar conectividade e saúde de todos os serviços contra todos os outros serviços
for pod in "${pods[@]}"; do
  for service in "${!services[@]}"; do
    echo "Testando $pod contra $service"
    kubectl exec -it "$pod" -- /bin/sh -c "curl -s ${services[$service]}"
    echo ""
  done
done