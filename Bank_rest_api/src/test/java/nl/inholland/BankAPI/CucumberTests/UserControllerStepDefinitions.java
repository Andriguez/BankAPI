package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.DTO.UserDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

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

    private LoginResponseDTO loginResponse;
    private LoginRequestDTO loginRequest;
    private ResponseEntity<LoginResponseDTO> loginResponseEntity;
    protected HttpHeaders httpHeaders = new HttpHeaders();
    private ResponseEntity<String> generalResponseEntity;
    public UserControllerStepDefinitions() {
    }

    @Given("I login with email {string} and password {string}")
    public void iLoginAsUser(String email, String password){
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

    @When("I request to read users of type {string}")
    public void iRequestToReadUsersOfType(String type) {
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        generalResponseEntity = restTemplate.exchange("/users?type="+type.toUpperCase(), HttpMethod.GET, entity, String.class);
    }

    @When("I request to read user with Id {int}")
    public void iRequestToReadUserWithId(long id) {
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        generalResponseEntity = restTemplate.exchange("/users?id="+id, HttpMethod.GET, entity, String.class);
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

    private List<UserOverviewDTO> userDTOS;
    @And("I receive a list of usersDTO")
    public void iReceiveAListOfUsersDTO() throws JsonProcessingException {
        userDTOS = objectMapper.readValue(generalResponseEntity.getBody(), new TypeReference<List<UserOverviewDTO>>() {});
        assertNotNull(generalResponseEntity.getBody());
        assertFalse(userDTOS.isEmpty());
    }

    @And("list has user of type {string}")
    public void listHasUserOfType(String type){
        assertTrue(users.stream().anyMatch(user -> user.getUserType().equals(List.of(UserType.valueOf(type)))));
    }

    @And("list has user with name {string}")
    public void listHasUserOfUserType(String name){
        assertTrue(userDTOS.stream().anyMatch(user -> user.firstName().equals(name)));
    }

    private UserDTO userDto;
    @And("I receive a single UserDTO")
    public void iReceiveASingleUser() throws JsonProcessingException {
        userDto = objectMapper.readValue(generalResponseEntity.getBody(), UserDTO.class);
        assertNotNull(generalResponseEntity.getBody());
        assertNotNull(userDto.Id());
    }

    @And("User has Id {int}")
    public void userHasType(int id){
        assertEquals((long) userDto.Id(), id);
    }

    @And("The user Response has message {string}")
    public void responseHasMessage(String message){
        assertEquals(message, generalResponseEntity.getBody());
    }

    @And("User has email {string}")
    public void userHasEmail(String email){
        assertEquals(userDto.email(), email);
    }

}
