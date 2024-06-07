package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Security.JwtProvider;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAccountsByCustomerNoAuthenticationFails() throws Exception {
        // Perform the GET request and verify the response
        mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetAccountsByCustomerLoggedUserWithNoAccount() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("test1@example.com");
        List<Account> mockAccounts = new ArrayList<>();
        mockUser.setAccounts(mockAccounts);
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));

    }
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetAccountsByCustomerLoggedUserWithAccount() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("test1@example.com");
        Account account1 = new Account();
        account1.setId(1L);
        account1.setType(AccountType.CURRENT);
        Account account2 = new Account();
        account2.setId(2L);
        account2.setType(AccountType.SAVINGS);
        List<Account> mockAccounts = Arrays.asList(account1, account2);
        mockUser.setAccounts(mockAccounts);

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value(account1.getType().name()))
                .andExpect(jsonPath("$[1].type").value(account2.getType().name()));
    }
}
