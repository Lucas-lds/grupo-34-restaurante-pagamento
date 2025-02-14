package bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "bdd",
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true,
        tags = ""
)
public class CucumberTest {

}
