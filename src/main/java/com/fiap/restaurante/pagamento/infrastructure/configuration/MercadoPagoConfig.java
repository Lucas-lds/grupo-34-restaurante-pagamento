package com.fiap.restaurante.pagamento.infrastructure.configuration;

import com.fiap.restaurante.pagamento.infrastructure.adapter.out.PagamentoAdapterOut;
import com.fiap.restaurante.pagamento.infrastructure.repository.PagamentoRepository;
import com.mercadopago.exceptions.MPConfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MercadoPagoConfig {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.ngrok-url}")
    private String ngrokURL;

    @Value("${mercadopago.api-qrs}")
    private String apiQRs;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Bean
    public PagamentoAdapterOut pagamentoAdapterOut() throws MPConfException {
        return new PagamentoAdapterOut(accessToken, ngrokURL, apiQRs, pagamentoRepository, restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}