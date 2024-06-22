package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RegisterControllerStepDefinitions extends CucumberSpringConfiguration{

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResponseEntity<UserOverviewDTO> userResponseEntity;
    private RegistrationDTO registrationDto;
    private UserOverviewDTO user;

    public RegisterControllerStepDefinitions() {
    }

    @Given("I have registration request with details {string} {string} {string} {int} {int} {string}")
    public void i_have_registration_request_with_details(String firstName, String lastName, String email, int phone, int bsn, String password) throws PendingException {
       registrationDto = new RegistrationDTO(firstName, lastName, email, (long)phone, (long)bsn, password);
    }

    @When("I send registration request")
    public void i_send_registration_request() throws PendingException{
        String url = "http://localhost:" + port + "/register";
        userResponseEntity = restTemplate.postForEntity(url, registrationDto, UserOverviewDTO.class);

        user = userResponseEntity.getBody();
    }

    @Then("I receive registration response with status code {int}")
    public void i_receive_registration_response_with_status_code(int code) throws PendingException{
        assertEquals(HttpStatus.valueOf(code), userResponseEntity.getStatusCode());

    }

    @Then("I receive valid registration response")
    public void i_receive_valid_registration_response() throws PendingException{
        assertNotNull(user);
    }
}
