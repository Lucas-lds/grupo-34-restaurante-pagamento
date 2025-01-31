package com.fiap.restaurante.pagamento.infrastructure.adapter.in.request;

import com.fiap.restaurante.pagamento.core.domain.Pagamento;
import com.fiap.restaurante.pagamento.core.domain.PaymentStatus;

public record PagamentoRequest(String idPedido) {

    public Pagamento toDomain() {
        return new Pagamento(idPedido, PaymentStatus.PENDING);
    }
}



