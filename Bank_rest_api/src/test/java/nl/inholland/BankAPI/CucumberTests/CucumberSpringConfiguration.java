package nl.inholland.BankAPI.CucumberTests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@RunWith(Cucumber.class)
@CucumberOptions(
        glue = { "nl.inholland.BankAPI.CucumberTests" },
        features = "src/test/resources/features"
)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}
