package nl.inholland.BankAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Security.JwtProvider;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    // Sara's Code
    @Test
    public void testGetAccountsByCustomerNoAuthenticationFails() throws Exception {
        // Perform the GET request and verify the response
        mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().is(401));
    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetAccountsByCustomerLoggedUserWithNoAccount() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        mockUser.setUserType(List.of(UserType.CUSTOMER));
        List<Account> mockAccounts = new ArrayList<>();
        mockUser.setAccounts(mockAccounts);
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("this user has no accounts"));

    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetAccountsByCustomerLoggedUserWithAccount() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        Account account1 = new Account();
        account1.setId(1L);
        mockUser.setUserType(List.of(UserType.CUSTOMER));
        account1.setType(AccountType.CURRENT);
        account1.setUser(mockUser);
        Account account2 = new Account();
        account2.setId(2L);
        account2.setType(AccountType.SAVINGS);
        account2.setUser(mockUser);
        List<Account> mockAccounts = Arrays.asList(account1, account2);
        mockUser.setAccounts(mockAccounts);

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.accounts.CURRENT.type").value("CURRENT"))
                .andExpect(jsonPath("$.accounts.SAVINGS.type").value("SAVINGS"));
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAccountsById_InternalServerError() throws Exception {
        Long userId = 1L;
        Mockito.when(userService.getUserById(userId)).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get("/accounts")
                        .param("userid", userId.toString()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while retrieving accounts"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserAccountsByUserId() throws Exception {
        // Create a mock user with an account
        User mockUser = new User();
        Account mockCurrent = new Account("NL12INHO3456789012", 1000.0, 5000.0, 1000.0, AccountType.CURRENT);
        Account mockSavings = new Account("NL12INHO3456789011", 1000.0, 5000.0, 1000.0, AccountType.SAVINGS);
        mockCurrent.setUser(mockUser);
        mockSavings.setUser(mockUser);
        mockUser.setAccounts(List.of(mockCurrent,mockSavings));

        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts")
                        .param("userid", "152"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].iban").value("NL12INHO3456789012"))
                .andExpect(jsonPath("$[1].iban").value("NL12INHO3456789011"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testDeleteUserAccount() throws Exception {
        // Create a mock user with an account
        User mockUser = new User();
        Account mockCurrent = new Account("NL12INHO3456789012", 1000.0, 5000.0, 1000.0, AccountType.CURRENT);
        Account mockSavings = new Account("NL12INHO3456789011", 1000.0, 5000.0, 1000.0, AccountType.SAVINGS);
        mockCurrent.setUser(mockUser);
        mockSavings.setUser(mockUser);
        mockUser.setAccounts(List.of(mockCurrent,mockSavings));

        Mockito.when(accountService.closeUserAccounts(Mockito.any(User.class))).thenReturn(List.of(mockSavings,mockCurrent));

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts")
                        .param("userid", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].iban").value("NL12INHO3456789012"))
                .andExpect(jsonPath("$[1].iban").value("NL12INHO3456789011"));
    }


}

