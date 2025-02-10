Feature: Operações de pagamento

  Scenario: Consultar status do pagamento
    Given que existe um pagamento no sistema com id "12345"
    When eu solicitar a consulta do status do pagamento com id "12345"
    Then eu devo receber o status "PENDING"

  Scenario: Receber notificação de pagamento
    Given que existe uma notificação de pagamento
    When eu enviar a notificação para o sistema
    Then eu devo receber uma resposta de sucesso

  Scenario: Cadastrar um novo pagamento
    Given que eu tenho os dados de um novo pagamento para cadastrar
    When eu solicitar o cadastro do novo pagamento
    Then o pagamento deve ser cadastrado com sucesso
