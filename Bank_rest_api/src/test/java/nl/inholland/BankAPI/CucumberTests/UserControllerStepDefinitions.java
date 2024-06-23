package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.DTO.*;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class UserControllerStepDefinitions extends CucumberSpringConfiguration{
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResponseEntity<List<UserOverviewDTO>> userResponseEntity;
    private RegistrationDTO registrationDto;
    private UserOverviewDTO user;

    private LoginResponseDTO loginResponse;
    private LoginRequestDTO loginRequest;
    private ResponseEntity<LoginResponseDTO> loginResponseEntity;
    protected HttpHeaders httpHeaders = new HttpHeaders();
    private ResponseEntity<String> generalResponseEntity;
    public UserControllerStepDefinitions() {
    }

    @Given("I login as an Admin with email {string} and password {string}")
    public void iLoginAsAdmin(String email, String password){
        loginRequest = new LoginRequestDTO(email, password);
        String url = "http://localhost:" + port + "/login";
        loginResponseEntity = restTemplate.postForEntity(url, loginRequest, LoginResponseDTO.class);
        loginResponse = loginResponseEntity.getBody();
        httpHeaders.setBearerAuth(loginResponse.token());
    }
    @When("I request to read all users")
    public void iRequestToReadAllUsers(){
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        generalResponseEntity = restTemplate.exchange("/users", HttpMethod.GET, entity, String.class);
    }
    @Then("I receive users response with status code {int}")
    public void iReceiveUserResponseWithStatusCode(int status){
        assertEquals(HttpStatus.valueOf(status), generalResponseEntity.getStatusCode());
    }

    private List<User> users;
    @And("I receive a list of users")
    public void iReceiveAListOfUsers() throws JsonProcessingException {
        users = objectMapper.readValue(generalResponseEntity.getBody(), new TypeReference<List<User>>() {});
        assertNotNull(generalResponseEntity.getBody());
        assertFalse(users.isEmpty());
    }

    @And("list has user of type {string}")
    public void listHasUserOfType(String type){
        assertEquals(true, users.stream().anyMatch(user -> user.getUserType().equals(List.of(UserType.valueOf(type)))));
    }
}
