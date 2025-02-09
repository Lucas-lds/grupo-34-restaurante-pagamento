package com.fiap.restaurante.pagamento.infrastructure.adapter.out;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.fiap.restaurante.pagamento.core.domain.Pagamento;
import com.fiap.restaurante.pagamento.core.domain.PaymentStatus;
import com.fiap.restaurante.pagamento.infrastructure.repository.PagamentoRepository;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PagamentoAdapterOutTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PagamentoAdapterOut pagamentoAdapterOut;

    private Pagamento pagamento;

    private final String accessToken = "mockAccessToken"; // Exemplo de token mockado
    private final String ngrokURL = "http://localhost"; // Exemplo de URL
    private final String apiQRs = "http://api.qr.example";

    @BeforeEach
    void setUp() throws Exception {
        pagamento = new Pagamento("123456", PaymentStatus.PENDING);
        pagamentoAdapterOut = new PagamentoAdapterOut(accessToken, ngrokURL, apiQRs, pagamentoRepository, restTemplate);
    }

    @Test
    void consultarStatusPagamentoDeveRetornarStatus() {
        when(pagamentoRepository.findByIdPedido(anyString())).thenReturn(pagamento.toEntity());

        String status = pagamentoAdapterOut.consultarStatusPagamento("123456");

        assertNotNull(status);
        assertEquals("PENDING", status);
        verify(pagamentoRepository, times(1)).findByIdPedido(anyString());
    }

    @Test
    void consultarStatusPagamentoDeveLancarExcecaoQuandoPedidoNaoEncontrado() {
        when(pagamentoRepository.findByIdPedido(anyString())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                pagamentoAdapterOut.consultarStatusPagamento("123456"));

        assertEquals("Pedido não encontrado!", exception.getMessage());
        verify(pagamentoRepository, times(1)).findByIdPedido(anyString());
    }

    @Test
    void cadastrarNovoPagamentoDeveSalvarPagamento() {
        when(pagamentoRepository.save(any())).thenReturn(null);
        pagamentoAdapterOut.cadastrarNovoPagamento(pagamento);
        verify(pagamentoRepository, times(1)).save(any());
    }

    @Test
    void gerarQRCodePagamentoDeveRetornarQRCode() throws Exception {
        // URL que será mockada
        String apiQRs = "http://api.qr.example"; // Pode ser qualquer URL fictícia para o teste

        // Simulando a resposta do RestTemplate
        String qrData = "https://qr.data";

        // Configuração dos headers para a requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        // Criando o corpo da requisição como um JSON
        JSONObject body = new JSONObject();
        body.put("external_reference", "123456"); // ID do pedido mockado
        body.put("notification_url", ngrokURL + "/pagamento/webhook");
        body.put("expiration_date", "2025-02-08T00:00:00.000-03:00"); // Data de expiração mockada
        body.put("title", "descrição do pagamento");
        body.put("description", "descrição do pagamento");
        body.put("total_amount", 100.0);
        body.put("items", new JSONObject[] {
                new JSONObject().put("title", "descrição do pagamento")
                        .put("quantity", 1)
                        .put("unit_price", 100.0)
                        .put("unit_measure", "unit")
                        .put("total_amount", 100.0)
        });

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        // Mockando a chamada ao RestTemplate para simular uma resposta de sucesso
        when(restTemplate.postForEntity(eq(apiQRs), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{\"qr_data\":\"" + qrData + "\"}", HttpStatus.CREATED));

        // Chamando o método do adapter
        String qrCode = pagamentoAdapterOut.gerarQRCodePagamento(100.0, "descrição do pagamento");

        // Verificações
        assertNotNull(qrCode); // Garantir que o QR Code não seja nulo
        assertEquals(qrData, qrCode); // Garantir que o QR code retornado é o esperado

        // Verificar que a chamada foi feita com o corpo correto
        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate, times(1)).postForEntity(eq(apiQRs), captor.capture(), eq(String.class));

        HttpEntity capturedRequest = captor.getValue();
        String capturedBody = capturedRequest.getBody().toString(); // Corpo da requisição
        assertTrue(capturedBody.contains("external_reference")); // Verifica se o corpo contém o valor esperado
        assertTrue(capturedBody.contains("123456")); // Verifica se o corpo contém o ID esperado
    }


    @Test
    void receberNotificacaoDeveRetornarRespostaQuandoPayloadEhNulo() {
        ResponseEntity<String> responseEntity = pagamentoAdapterOut.receberNotificacao(null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Payload vazio", responseEntity.getBody());
    }

    @Test
    void receberNotificacaoDeveRetornarRespostaQuandoNotificacaoRecebida() throws MPException {
        Map<String, Object> payload = Map.of(
                "action", "payment.created",
                "data", Map.of("id", "123456789")
        );

        Payment payment = mock(Payment.class);
        when(payment.getExternalReference()).thenReturn("123456");
        when(payment.getStatus()).thenReturn(Payment.Status.approved);

        PagamentoAdapterOut spyPagamentoAdapterOut = spy(pagamentoAdapterOut);
        doReturn(payment).when(spyPagamentoAdapterOut).consultarPagamentoML(anyString());

        ResponseEntity<String> responseEntity = spyPagamentoAdapterOut.receberNotificacao(payload);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Notificação recebida com sucesso", responseEntity.getBody());
        verify(spyPagamentoAdapterOut, times(1)).consultarPagamentoML(anyString());
    }

    @Test
    void consultarPagamentoMLDeveRetornarPagamento() throws MPException {
        // Arrange
        String paymentId = "12345"; // ID fictício do pagamento
        Payment expectedPayment = new Payment();
        expectedPayment.setStatus(Payment.Status.approved); // Status mockado

        Payment payment = pagamentoAdapterOut.consultarPagamentoML(paymentId);

        // Verificações
        assertNotNull(payment); // Verificar se o pagamento não é nulo
        assertNull(payment.getStatus()); // Verificar se o status está correto
    }


}
