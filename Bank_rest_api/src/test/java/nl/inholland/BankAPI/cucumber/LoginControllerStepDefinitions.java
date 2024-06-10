package nl.inholland.BankAPI.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginControllerStepDefinitions{

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerStepDefinitions.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private LoginResponseDTO loginResponse;
    private ResponseEntity<UserDTO> registerResponse;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResponseEntity<String> response;
    protected HttpHeaders httpHeaders = new HttpHeaders();

    public LoginControllerStepDefinitions() {
        // objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule());
    }

    @Given("I have login request with email {string} and password {string}")
    public void iHaveLoginRequestWithEmailAndPassword(String email, String password) throws JsonProcessingException {
        LoginRequestDTO loginDTO = new LoginRequestDTO(email, password);
        String url = "https://localhost:" + port + "/login";
        logger.error(url+8);
        String test = "1254";
        logger.error(email + password);
        ResponseEntity<String> res = restTemplate.postForEntity(url, objectMapper.writeValueAsString(loginDTO), String.class);
        // loginResponse
        logger.error(String.valueOf(res));
        loginResponse = objectMapper.readValue(response.getBody(), LoginResponseDTO.class);

        /*
        * response = restTemplate
        .exchange("/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(objectMapper.writeValueAsString(loginDTO), httpHeaders), String.class);
    LoginResponseDTO tokenDTO = objectMapper.readValue(response.getBody(), LoginResponseDTO.class);
    return tokenDTO.jwt();
        * */
    }


    @When("I send login request")
    public void iSendLoginRequest() {
        // The request is already sent in the previous step
    }

    @Then("I receive login response with status code {int}")
    public void iReceiveLoginResponseWithStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), 200);
    }

    @Then("I receive valid login response")
    public void iReceiveValidLoginResponse() {
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.token());
        assertNotNull(loginResponse.name());
    }
}
