package com.fiap.restaurante.pagamento.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.fiap.restaurante.pagamento.application.port.out.PagamentoServicePortOut;
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
public class PagamentoUseCaseTest {

    @Mock
    private PagamentoServicePortOut pagamentoServicePortOut;

    @InjectMocks
    private PagamentoUseCase pagamentoUseCase;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento("123456", PaymentStatus.PENDING);
    }

    @Test
    void cadastrarNovoPagamentoDeveSalvarPagamento() {
        doNothing().when(pagamentoServicePortOut).cadastrarNovoPagamento(any(Pagamento.class));

        pagamentoUseCase.cadastrarNovoPagamento(pagamento);

        verify(pagamentoServicePortOut, times(1)).cadastrarNovoPagamento(any(Pagamento.class));
    }

    @Test
    void consultarStatusPagamentoDeveRetornarStatus() {
        when(pagamentoServicePortOut.consultarStatusPagamento(anyString())).thenReturn("PENDING");

        String status = pagamentoUseCase.consultarStatusPagamento("123456");

        assertNotNull(status);
        assertEquals("PENDING", status);
        verify(pagamentoServicePortOut, times(1)).consultarStatusPagamento(anyString());
    }

    @Test
    void gerarQRCodePagamentoDeveRetornarQRCode() {
        String expectedQRCode = "https://qr.data";
        when(pagamentoServicePortOut.gerarQRCodePagamento(anyDouble(), anyString())).thenReturn(expectedQRCode);

        String qrCode = pagamentoUseCase.gerarQRCodePagamento(100.0, "descricao");

        assertNotNull(qrCode);
        assertEquals(expectedQRCode, qrCode);
        verify(pagamentoServicePortOut, times(1)).gerarQRCodePagamento(anyDouble(), anyString());
    }

    @Test
    void receberNotificacaoDeveRetornarResposta() {
        Map<String, Object> payload = Map.of(
                "action", "payment.created",
                "data", Map.of("id", "123456789")
        );
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Notificação recebida com sucesso");
        when(pagamentoServicePortOut.receberNotificacao(anyMap())).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = pagamentoUseCase.receberNotificacao(payload);

        assertNotNull(responseEntity);
        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(pagamentoServicePortOut, times(1)).receberNotificacao(anyMap());
    }

    @Test
    void consultarPagamentoMLDeveRetornarPayment() throws Exception {
        Payment payment = mock(Payment.class);
        when(pagamentoServicePortOut.consultarPagamentoML(anyString())).thenReturn(payment);

        Payment result = pagamentoUseCase.consultarPagamentoML("123456");

        assertNotNull(result);
        assertEquals(payment, result);
        verify(pagamentoServicePortOut, times(1)).consultarPagamentoML(anyString());
    }
}
