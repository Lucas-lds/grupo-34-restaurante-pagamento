package com.fiap.restaurante.pagamento.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.fiap.restaurante.pagamento.application.port.out.PagamentoAdapterPortOut;
import com.fiap.restaurante.pagamento.core.domain.Pagamento;
import com.fiap.restaurante.pagamento.core.domain.PaymentStatus;
import com.mercadopago.resources.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoAdapterPortOut pagamentoAdapterPortOut;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento("123456", PaymentStatus.PENDING);
    }

    @Test
    void consultarStatusPagamentoDeveRetornarStatus() {
        when(pagamentoAdapterPortOut.consultarStatusPagamento(anyString())).thenReturn("PENDING");

        String status = pagamentoService.consultarStatusPagamento("123456");

        assertNotNull(status);
        assertEquals("PENDING", status);
        verify(pagamentoAdapterPortOut, times(1)).consultarStatusPagamento(anyString());
    }

    @Test
    void gerarQRCodePagamentoDeveRetornarQRCode() {
        String expectedQRCode = "https://qr.data";
        when(pagamentoAdapterPortOut.gerarQRCodePagamento(anyDouble(), anyString())).thenReturn(expectedQRCode);

        String qrCode = pagamentoService.gerarQRCodePagamento(100.0, "descricao");

        assertNotNull(qrCode);
        assertEquals(expectedQRCode, qrCode);
        verify(pagamentoAdapterPortOut, times(1)).gerarQRCodePagamento(anyDouble(), anyString());
    }

    @Test
    void receberNotificacaoDeveRetornarResposta() {
        Map<String, Object> payload = Map.of(
                "action", "payment.created",
                "data", Map.of("id", "123456789")
        );
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Notificação recebida com sucesso");
        when(pagamentoAdapterPortOut.receberNotificacao(anyMap())).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = pagamentoService.receberNotificacao(payload);

        assertNotNull(responseEntity);
        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(pagamentoAdapterPortOut, times(1)).receberNotificacao(anyMap());
    }

    @Test
    void consultarPagamentoMLDeveRetornarPayment() throws Exception {
        Payment payment = mock(Payment.class);
        when(pagamentoAdapterPortOut.consultarPagamentoML(anyString())).thenReturn(payment);

        Payment result = pagamentoService.consultarPagamentoML("123456");

        assertNotNull(result);
        assertEquals(payment, result);
        verify(pagamentoAdapterPortOut, times(1)).consultarPagamentoML(anyString());
    }

    @Test
    void cadastrarNovoPagamentoDeveSalvarPagamento() {
        doNothing().when(pagamentoAdapterPortOut).cadastrarNovoPagamento(any(Pagamento.class));

        pagamentoService.cadastrarNovoPagamento(pagamento);

        verify(pagamentoAdapterPortOut, times(1)).cadastrarNovoPagamento(any(Pagamento.class));
    }
}
