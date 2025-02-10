package com.fiap.restaurante.pagamento.infrastructure.adapter.configuration;

import com.fiap.restaurante.pagamento.infrastructure.adapter.out.PagamentoAdapterOut;
import com.fiap.restaurante.pagamento.infrastructure.configuration.MercadoPagoConfig;
import com.fiap.restaurante.pagamento.infrastructure.repository.PagamentoRepository;
import com.mercadopago.exceptions.MPConfException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MercadoPagoConfigTest {

    @InjectMocks
    private MercadoPagoConfig mercadoPagoConfig;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mercadoPagoConfig, "accessToken", "test-access-token");
        ReflectionTestUtils.setField(mercadoPagoConfig, "ngrokURL", "test-ngrok-url");
        ReflectionTestUtils.setField(mercadoPagoConfig, "apiQRs", "test-api-qrs");
    }

    @Test
    void testPagamentoAdapterOut() throws MPConfException {
        PagamentoAdapterOut pagamentoAdapterOut = mercadoPagoConfig.pagamentoAdapterOut();
        assertNotNull(pagamentoAdapterOut);
    }
}
