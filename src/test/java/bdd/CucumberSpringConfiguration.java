package bdd;

import com.fiap.restaurante.pagamento.RestaurantePagamentoApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@CucumberContextConfiguration
@SpringBootTest(classes = {RestaurantePagamentoApplication.class, CucumberSpringConfiguration.TestConfig.class})
public class CucumberSpringConfiguration {

    @Configuration
    static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
