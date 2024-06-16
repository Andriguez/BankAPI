package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.DTO.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerStepDefinitions extends CucumberSpringConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AccountControllerStepDefinitions.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private LoginResponseDTO loginResponse;
    private AccountsDTO accountResponse;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected ResponseEntity<LoginResponseDTO> loginResponseEntity;
    protected ResponseEntity<String> accountResponseEntity;
    protected HttpHeaders httpHeaders = new HttpHeaders();

    public AccountControllerStepDefinitions() {
        
    }
    private LoginRequestDTO loginDTO;


    @Given("I login as as Guest with email {string} and password {string}")
    public void iLoginAsAsGuestWithEmailAndPassword(String email, String password) {
        logger.info(email + " " + password);
        loginDTO = new LoginRequestDTO(email, password);
        String url = "http://localhost:" + port + "/login";
        loginResponseEntity = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);
        // Store login response
        loginResponse = loginResponseEntity.getBody();
        // Set JWT token in headers for future requests
        httpHeaders.setBearerAuth(loginResponse.getToken());
        // httpHeaders.add("Authorization", "Bearer " + loginResponse.getToken());
    }


    @When("I request to read accounts as guest")
    public void itFailsRequestToReadAccounts() {
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        accountResponseEntity = restTemplate.exchange("/accounts", HttpMethod.GET, entity, String.class);
    }
    @Then("I receive error response with status code {int}")
    public void iReceiveErrorResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), accountResponseEntity.getStatusCode());
    }

    @And("I receive an error message {string}")
    public void iReceiveAnErrorMesage(String message) {
        assertEquals(message, accountResponseEntity.getBody());
    }

    @Given("To see my accounts, I login as as Customer with email {string} and password {string}")
    public void toSeeMyAccountsILoginAsAsCustomerWithEmailAndPassword(String email, String password) {
        logger.info(email + " " + password);
        loginDTO = new LoginRequestDTO(email, password);
        String url = "http://localhost:" + port + "/login";
        loginResponseEntity = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);
        // Store login response
        loginResponse = loginResponseEntity.getBody();
        // Set JWT token in headers for future requests
        httpHeaders.setBearerAuth(loginResponse.getToken());
        // httpHeaders.add("Authorization", "Bearer " + loginResponse.getToken());
    }
    @When("I request to read accounts as customer")
    public void iRequestToReadAccountsAsCustomer() {
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        accountResponseEntity = restTemplate.exchange("/accounts", HttpMethod.GET, entity, String.class);
    }
    @Then("I receive accounts response with status code {int}")
    public void iReceiveAccountsResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), accountResponseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, accountResponseEntity.getStatusCode(), "Response status is not OK");
    }

    @And("I receive accounts array of length {int}")
    public void iReceiveAccountsArrayOfLength(int numberOfAccounts) throws IOException {

        logger.info(accountResponseEntity.toString());
        logger.info(accountResponseEntity.getBody());
        accountResponse = objectMapper.readValue(accountResponseEntity.getBody(), AccountsDTO.class);
        assertNotNull(accountResponse);
        assertEquals(numberOfAccounts, accountResponse.accounts().size());
    }

    @And("I receive account of type {string}")
    public void iReceiveAccountOfType(String accountType) {
        Boolean  accountPresent= accountResponse.accounts().containsKey(AccountType.valueOf(accountType));
        assertEquals(true, accountPresent);
    }

}
