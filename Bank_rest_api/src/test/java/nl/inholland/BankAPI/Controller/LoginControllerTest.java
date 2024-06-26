package nl.inholland.BankAPI.Controller;

import lombok.SneakyThrows;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.UserType;
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
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.AuthenticationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private JwtProvider jwtProvider;

        @InjectMocks
        private LoginController loginController;

        @Autowired
        WebApplicationContext webApplicationContext;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);
            this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        }


    @SneakyThrows
    @Test
        public void testLoginSuccess() throws AuthenticationException {
            // Setup mock login request
            LoginRequestDTO loginRequest = new LoginRequestDTO("john.doe@example.com", "password");

            // Setup mock login response
            LoginResponseDTO loginResponse = new LoginResponseDTO(1L, "CustomerName", "CustomerLastname", UserType.CUSTOMER.name(), "token123");

            // Mock the service call
            when(userService.login(any(LoginRequestDTO.class))).thenReturn(loginResponse);

            // Perform the POST request
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"john.doe@example.com\",\"password\":\"password\"}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token123"));
        }

        @Test
        public void testLoginFailure() throws Exception {
           LoginRequestDTO loginRequest = new LoginRequestDTO("john.doe@example.com", "wrongpassword");

          when(userService.login(any(LoginRequestDTO.class))).thenThrow(new AuthorizationServiceException("The provided email and/or username are incorrect"));

            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"john.doe@example.com\",\"password\":\"wrongpassword\"}"))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andExpect(content().string("The provided email and/or username are incorrect"));
        }
    }

