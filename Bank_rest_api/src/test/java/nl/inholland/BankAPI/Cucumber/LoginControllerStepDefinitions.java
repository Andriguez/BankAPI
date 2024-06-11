package nl.inholland.BankAPI.Cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginControllerStepDefinitions{
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private ResponseEntity<LoginResponseDTO> loginResponse;
    private ResponseEntity<UserDTO> registerResponse;

    private ObjectMapper objectMapper;


    public LoginControllerStepDefinitions() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Given("I have login request with email {string} and password {string}")
    public void iHaveLoginRequestWithEmailAndPassword(String email, String password) {
        LoginRequestDTO loginDTO = new LoginRequestDTO(email, password);
        String url = "http://localhost:" + port + "/auth/login";
        loginResponse = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);
    }


    @When("I send login request")
    public void iSendLoginRequest() {
        // The request is already sent in the previous step
    }

    @Then("I receive login response with status code {int}")
    public void iReceiveLoginResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), loginResponse.getStatusCode());
    }

    @Then("I receive valid login response")
    public void iReceiveValidLoginResponse() {
        assertNotNull(loginResponse.getBody());
        assertNotNull(loginResponse.getBody().token());
        assertNotNull(loginResponse.getBody().name());
    }
}
