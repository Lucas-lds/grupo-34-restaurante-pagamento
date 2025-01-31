package com.fiap.restaurante.pagamento.core.domain;

import com.fiap.restaurante.pagamento.infrastructure.entity.PagamentoEntity;

public class Pagamento {

    private String idPedido;
    private PaymentStatus status;

    public Pagamento(String idPedido, PaymentStatus status) {
        this.idPedido = idPedido;
        this.status = status;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PagamentoEntity toEntity() {
        return new PagamentoEntity(idPedido, status);
    }
}
