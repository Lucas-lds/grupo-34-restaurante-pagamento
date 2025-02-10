package bdd;

import com.fiap.restaurante.pagamento.application.port.out.usecase.PagamentoUseCasePortOut;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.PagamentoController;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.request.PagamentoRequest;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.request.QrCodeRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class PagamentoSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PagamentoUseCasePortOut pagamentoUseCasePortOut;

    @Autowired
    private PagamentoController pagamentoController;

    private String idPedido;
    private Map<String, Object> payload;

    // Step 1: Consultar status do pagamento
    @Given("que existe um pagamento no sistema com id {string}")
    public void queExisteUmPagamentoNoSistemaComId(String id) {
        this.idPedido = id;
        when(pagamentoUseCasePortOut.consultarStatusPagamento(id)).thenReturn("PENDING");
    }

    @When("eu solicitar a consulta do status do pagamento com id {string}")
    public void euSolicitarAConsultaDoStatusDoPagamentoComId(String id) throws Exception {
        MvcResult result = mockMvc.perform(get("/pagamento/status/{idPedido}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsString("PENDING")))
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Then("eu devo receber o status {string}")
    public void euDevoReceberOStatus(String statusEsperado) {
        assertEquals(statusEsperado, "PENDING");
    }

    // Step 2: Receber notificação de pagamento
    @Given("que existe uma notificação de pagamento")
    public void queExisteUmaNotificacaoDePagamento() {
        payload = new HashMap<>();
        payload.put("id", "12345");
        payload.put("status", "PENDING");
        when(pagamentoUseCasePortOut.receberNotificacao(payload)).thenReturn(ResponseEntity.ok("Notificação recebida com sucesso"));
    }

    @When("eu enviar a notificação para o sistema")
    public void euEnviarANotificacaoParaOSistema() throws Exception {
        MvcResult result = mockMvc.perform(post("/pagamento/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"12345\", \"status\":\"PENDING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsString("Notificação recebida com sucesso")))
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Then("eu devo receber uma resposta de sucesso")
    public void euDevoReceberUmaRespostaDeSucesso() {
        // O retorno esperado já é verificado no passo anterior
    }

    @Given("que eu tenho os dados de um novo pagamento para cadastrar")
    public void queEuTenhoOsDadosDeUmNovoPagamentoParaCadastrar() {
        PagamentoRequest pagamentoRequest = new PagamentoRequest("12345");
        doNothing().when(pagamentoUseCasePortOut).cadastrarNovoPagamento(any());

    }

    @When("eu solicitar o cadastro do novo pagamento")
    public void euSolicitarOCadastroDoNovoPagamento() throws Exception {
        PagamentoRequest pagamentoRequest = new PagamentoRequest("12345");

        MvcResult result = mockMvc.perform(post("/pagamento/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idPedido\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsString("Pagamento cadastrado com sucesso")))
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(200, result.getResponse().getStatus());
    }

    @Then("o pagamento deve ser cadastrado com sucesso")
    public void oPagamentoDeveSerCadastradoComSucesso() {
        // O retorno esperado já é verificado no passo anterior
    }
}
