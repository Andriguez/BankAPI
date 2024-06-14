package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.DTO.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.cucumber.java.en.Given;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerStepDefinitions extends CucumberSpringConfiguration{

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerStepDefinitions.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private LoginResponseDTO loginResponse;
    private ResponseEntity<UserDTO> registerResponse;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResponseEntity<LoginResponseDTO> loginResponseEntity;
    protected HttpHeaders httpHeaders = new HttpHeaders();

    public LoginControllerStepDefinitions() {

    }
    private LoginRequestDTO loginDTO;

    @Given("I have login request with email {string} and password {string}")
    public void iHaveLoginRequestWithEmailAndPassword(String email, String password) throws JsonProcessingException {
        logger.info(email + " " + password);
        loginDTO = new LoginRequestDTO(email, password);
    }


    @When("I send login request")
    public void iSendLoginRequest() {
        // Send login request
        String url = "http://localhost:" + port + "/login";
        logger.info("Request URL: 2 " + url);

        loginResponseEntity = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);

        // Store login response
        loginResponse = loginResponseEntity.getBody();

        // Set JWT token in headers for future requests
        httpHeaders.setBearerAuth(loginResponse.getToken());
    }

    @Then("I receive login response with status code {int}")
    public void iReceiveLoginResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), loginResponseEntity.getStatusCode());
    }

    @Then("I receive valid login response")
    public void iReceiveValidLoginResponse() {
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.token());
        assertNotNull(loginResponse.name());
    }
}