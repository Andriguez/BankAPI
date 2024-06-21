package nl.inholland.BankAPI.CucumberTests;

import io.cucumber.spring.CucumberContextConfiguration;
import nl.inholland.BankAPI.BankAPIApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


//@RunWith(Cucumber.class)
//@CucumberOptions(
  //      glue = { "nl.inholland.BankAPI.CucumberTests" },
    //    features = "src/test/resources/features"
//)
@ActiveProfiles("test")
@SpringBootTest(classes = BankAPIApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}
