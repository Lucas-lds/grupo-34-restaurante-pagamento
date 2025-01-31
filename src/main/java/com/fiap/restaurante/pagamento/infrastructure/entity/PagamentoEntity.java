package com.fiap.restaurante.pagamento.infrastructure.entity;

import com.fiap.restaurante.pagamento.core.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_pagamentos")
public class PagamentoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "id_pedido")
    private String idPedido;

    public PagamentoEntity(String idPedido, PaymentStatus status) {
        this.idPedido = idPedido;
        this.status = status;
    }
}
