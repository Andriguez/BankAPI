package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Security.JwtProvider;
import nl.inholland.BankAPI.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @InjectMocks
    private RegisterController registerController;
    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        // Setup mock registration data
        RegistrationDTO registrationDTO = new RegistrationDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                123456789L,
                987654321L,
                "password"
        );

        // Setup mock user
        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPhoneNumber(123456789L);
        mockUser.setBsnNumber(987654321L);
        mockUser.setPassword("password");

        // Mock the service call
        when(userService.createUserDTO(any(RegistrationDTO.class))).thenReturn(mockUser);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":123456789,\"bsnNumber\":987654321,\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":123456789,\"bsnNumber\":987654321}"));
    }

    @Test
    public void testRegisterUserMissingFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":123456789,\"bsnNumber\":987654321,\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("All fields are required"));
    }

    @Test
    public void testRegisterUserInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@\",\"phoneNumber\":123456789,\"bsnNumber\":987654321,\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email format"));
    }

    @Test
    public void testRegisterUserInvalidBSN() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":123456789,\"bsnNumber\":123,\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid BSN number"));
    }
}
