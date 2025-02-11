package bdd;

import com.fiap.restaurante.pagamento.RestaurantePagamentoApplication;
import com.fiap.restaurante.pagamento.application.port.out.usecase.PagamentoUseCasePortOut;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = RestaurantePagamentoApplication.class)
@MockitoBean(types = {PagamentoUseCasePortOut.class})
public class SpringGlue {


}
