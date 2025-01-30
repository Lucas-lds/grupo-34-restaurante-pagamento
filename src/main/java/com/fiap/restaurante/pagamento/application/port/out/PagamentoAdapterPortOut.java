package com.fiap.restaurante.pagamento.application.port.out;

import com.mercadopago.resources.Payment;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PagamentoAdapterPortOut {
    
    String consultarStatusPagamento(Long idPedido);

    String gerarQRCodePagamento(Double valor, String descricao);

    ResponseEntity<String> receberNotificacao(Map<String, Object> payload);

    Payment consultarPagamentoML(String paymentId);

}
