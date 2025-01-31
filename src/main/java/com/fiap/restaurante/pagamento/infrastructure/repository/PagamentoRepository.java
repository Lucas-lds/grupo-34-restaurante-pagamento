package com.fiap.restaurante.pagamento.infrastructure.repository;

import com.fiap.restaurante.pagamento.infrastructure.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, String> {
    PagamentoEntity findByIdPedido(String idPedido);
}
