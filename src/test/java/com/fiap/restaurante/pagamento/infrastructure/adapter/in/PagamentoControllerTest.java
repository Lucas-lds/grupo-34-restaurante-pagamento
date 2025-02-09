package com.fiap.restaurante.pagamento.infrastructure.adapter.in;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.fiap.restaurante.pagamento.application.port.out.usecase.PagamentoUseCasePortOut;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.request.PagamentoRequest;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.request.QrCodeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PagamentoControllerTest {

    @Mock
    private PagamentoUseCasePortOut pagamentoUseCasePortOut;

    @InjectMocks
    private PagamentoController pagamentoController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void consultarStatusPagamentoDeveRetornarStatus() {
        when(pagamentoUseCasePortOut.consultarStatusPagamento(anyString())).thenReturn("PENDING");

        String status = pagamentoController.consultarStatusPagamento("123456");

        assertNotNull(status);
        assertEquals("PENDING", status);
        verify(pagamentoUseCasePortOut, times(1)).consultarStatusPagamento(anyString());
    }

    @Test
    void gerarQrCodeDeveRetornarQRCode() {
        String expectedQRCode = "https://qr.data";
        QrCodeRequest qrCodeRequest = new QrCodeRequest(100.0, "descricao");
        when(pagamentoUseCasePortOut.gerarQRCodePagamento(anyDouble(), anyString())).thenReturn(expectedQRCode);

        String qrCode = pagamentoController.gerarQrCode(qrCodeRequest);

        assertNotNull(qrCode);
        assertEquals(expectedQRCode, qrCode);
        verify(pagamentoUseCasePortOut, times(1)).gerarQRCodePagamento(anyDouble(), anyString());
    }

    @Test
    void receberNotificacaoDeveRetornarResposta() {
        Map<String, Object> payload = Map.of(
                "action", "payment.created",
                "data", Map.of("id", "123456789")
        );
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Notificação recebida com sucesso");
        when(pagamentoUseCasePortOut.receberNotificacao(anyMap())).thenReturn(expectedResponse);

        ResponseEntity<String> responseEntity = pagamentoController.receberNotificacao(payload);

        assertNotNull(responseEntity);
        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(pagamentoUseCasePortOut, times(1)).receberNotificacao(anyMap());
    }

    @Test
    void cadastrarNovoPagamentoDeveCadastrarPagamento() {
        PagamentoRequest pagamentoRequest = new PagamentoRequest("123456");
        doNothing().when(pagamentoUseCasePortOut).cadastrarNovoPagamento(any());

        ResponseEntity<String> responseEntity = pagamentoController.cadastrarNovoPagamento(pagamentoRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Pagamento cadastrado com sucesso", responseEntity.getBody());
        verify(pagamentoUseCasePortOut, times(1)).cadastrarNovoPagamento(any());
    }
}
