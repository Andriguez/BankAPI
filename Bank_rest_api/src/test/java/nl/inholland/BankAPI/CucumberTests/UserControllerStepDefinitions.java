package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class UserControllerStepDefinitions extends CucumberSpringConfiguration{
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResponseEntity<UserOverviewDTO> userResponseEntity;
    private RegistrationDTO registrationDto;
    private UserOverviewDTO user;

    public UserControllerStepDefinitions() {
    }


}
