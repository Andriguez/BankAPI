package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class LoginControllerStepDefinitions extends CucumberSpringConfiguration{

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerStepDefinitions.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private LoginResponseDTO loginResponse;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResponseEntity<String> loginResponseEntity;
    protected HttpHeaders httpHeaders = new HttpHeaders();

    public LoginControllerStepDefinitions() {

    }
    private LoginRequestDTO loginDTO;

    // Sara's Code
    @Given("I have login request with email {string} and password {string}")
    public void iHaveLoginRequestWithEmailAndPassword(String email, String password) throws JsonProcessingException {
        logger.info(email + " " + password);
        loginDTO = new LoginRequestDTO(email, password);
    }

    // Sara's Code
    @When("I send login request")
    public void iSendLoginRequest() throws RuntimeException{
        // Send login request
        String url = "http://localhost:" + port + "/login";
        logger.info("Request URL: 2 " + url);

        loginResponseEntity = restTemplate.postForEntity(url, loginDTO, String.class);
    }

    @Then("I set login response and token")
    public void iSetLoginResponse() throws JsonProcessingException {
        //Store login response
        loginResponse = objectMapper.readValue(loginResponseEntity.getBody(), LoginResponseDTO.class);

        //Set JWT token in headers for future requests
        httpHeaders.setBearerAuth(loginResponse.token());
    }

    // Sara's Code
    @Then("I receive login response with status code {int}")
    public void iReceiveLoginResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), loginResponseEntity.getStatusCode());
    }

    // Sara's Code
    @Then("I receive valid login response")
    public void iReceiveValidLoginResponse() {
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.token());
        assertNotNull(loginResponse.firstName());
    }

    @And("The login Response has message {string}")
    public void loginResponseHasMessage(String message){
        assertEquals(message, loginResponseEntity.getBody());
    }
}
