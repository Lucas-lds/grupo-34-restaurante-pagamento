package bdd;

import com.fiap.restaurante.pagamento.infrastructure.adapter.in.PagamentoController;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.request.PagamentoRequest;
import com.fiap.restaurante.pagamento.infrastructure.adapter.in.request.QrCodeRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CucumberSpringConfiguration.class)
public class PagamentoSteps {

    @Autowired
    private PagamentoController pagamentoController;

    private String idPedido;
    private QrCodeRequest qrCodeRequest;
    private PagamentoRequest pagamentoRequest;
    private Map<String, Object> payload;
    private ResponseEntity<?> responseEntity;
    private String status;

    @Given("que existe um pagamento no sistema com id {string}")
    public void queExisteUmPagamentoNoSistemaComId(String id) {
        this.idPedido = id;
        pagamentoRequest = new PagamentoRequest(id);
        pagamentoController.cadastrarNovoPagamento(pagamentoRequest);
    }

    @When("eu solicitar a consulta do status do pagamento com id {string}")
    public void euSolicitarAConsultaDoStatusDoPagamentoComId(String id) {
        status = pagamentoController.consultarStatusPagamento(id);
    }

    @Then("eu devo receber o status {string}")
    public void euDevoReceberOStatus(String esperadoStatus) {
        assertEquals(esperadoStatus, status);
    }

    @Given("que existe uma notificação de pagamento")
    public void queExisteUmaNotificacaoDePagamento() {
        payload = new HashMap<>();
        payload.put("id", "12345");
        payload.put("status", "PENDING");
    }

    @When("eu enviar a notificação para o sistema")
    public void euEnviarANotificacaoParaOSistema() {
        responseEntity = pagamentoController.receberNotificacao(payload);
    }

    @Then("eu devo receber uma resposta de sucesso")
    public void euDevoReceberUmaRespostaDeSucesso() {
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Notificação recebida com sucesso", responseEntity.getBody());
    }

    @Given("que eu tenho os dados de um novo pagamento para cadastrar")
    public void queEuTenhoOsDadosDeUmNovoPagamentoParaCadastrar() {
        pagamentoRequest = new PagamentoRequest("12345");
    }

    @When("eu solicitar o cadastro do novo pagamento")
    public void euSolicitarOCadastroDoNovoPagamento() {
        responseEntity = pagamentoController.cadastrarNovoPagamento(pagamentoRequest);
    }

    @Then("o pagamento deve ser cadastrado com sucesso")
    public void oPagamentoDeveSerCadastradoComSucesso() {
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Pagamento cadastrado com sucesso", responseEntity.getBody());
    }
}
