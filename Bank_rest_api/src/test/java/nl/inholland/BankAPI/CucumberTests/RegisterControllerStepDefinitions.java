package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
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

    protected ResponseEntity<String> registrationResponseEntity;
    private RegistrationDTO registrationDto;
    private UserOverviewDTO user;

    public RegisterControllerStepDefinitions() {
    }

    @Given("I have registration request with details name {string}, lastname {string}, email {string}, phone {int}, bsn {int}, password {string}")
    public void iSendARegistrationRequest(String firstName, String lastName, String email, int phone, int bsn, String password) throws PendingException {
       registrationDto = new RegistrationDTO(firstName, lastName, email, (long)phone, (long)bsn, password);
    }

    @Given("I have a failing registration request with details name {string}, lastname {string}, email {string}, phone {int}, bsn {int}")
    public void iSendAFailingRegistrationRequest(String firstName, String lastName, String email, int phone, int bsn) throws PendingException {
        registrationDto = new RegistrationDTO(firstName, lastName, email, (long)phone, bsn, null);
    }

    @When("I send registration request")
    public void i_send_registration_request() {
        String url = "http://localhost:" + port + "/register";
        registrationResponseEntity = restTemplate.postForEntity(url, registrationDto, String.class);
    }

    @Then("I receive registration response with status code {int}")
    public void i_receive_registration_response_with_status_code(int code) throws PendingException{
        assertEquals(HttpStatus.valueOf(code), registrationResponseEntity.getStatusCode());

    }

    @Then("I receive valid registration response")
    public void i_receive_valid_registration_response() throws PendingException, JsonProcessingException {
        user = objectMapper.readValue(registrationResponseEntity.getBody(), UserOverviewDTO.class);
        assertNotNull(user);
    }

    @And("The registration Response has message {string}")
    public void registrationResponseHasMessage(String message){
        assertEquals(message, registrationResponseEntity.getBody());
    }
}
