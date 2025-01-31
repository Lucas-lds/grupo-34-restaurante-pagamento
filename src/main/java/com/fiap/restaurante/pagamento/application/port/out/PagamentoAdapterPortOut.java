package com.fiap.restaurante.pagamento.application.port.out;

import com.fiap.restaurante.pagamento.core.domain.Pagamento;
import com.mercadopago.resources.Payment;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PagamentoAdapterPortOut {
    
    String consultarStatusPagamento(String idPedido);

    String gerarQRCodePagamento(Double valor, String descricao);

    ResponseEntity<String> receberNotificacao(Map<String, Object> payload);

    Payment consultarPagamentoML(String paymentId);

    void cadastrarNovoPagamento(Pagamento pagamento);

}
