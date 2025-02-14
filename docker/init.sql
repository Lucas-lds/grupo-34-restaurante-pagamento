CREATE TABLE IF NOT EXISTS tb_pagamentos (
    id_pagamento BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_pedido CHAR(36) NOT NULL,
    status VARCHAR(50)
);