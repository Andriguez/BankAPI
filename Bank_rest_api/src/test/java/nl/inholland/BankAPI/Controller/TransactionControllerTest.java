package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.TransactionRequestDTO;
import nl.inholland.BankAPI.Model.DTO.TransactionResponseDTO;
import nl.inholland.BankAPI.Security.JwtProvider;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.TransactionService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;
    @MockBean
    private UserService userService;
    @MockBean
    private TransactionService transactionService;

    @MockBean
    private JwtProvider jwtProvider;

    @InjectMocks
    private TransactionController transactionController;

    private List<Transaction> transactions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTransactionsByCustomerNoAuthenticationFails() throws Exception {
        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions")).andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsCustomerLoggedUserWithNoAccount() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        List<Account> mockAccounts = new ArrayList<>();
        mockUser.setAccounts(mockAccounts);
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isEmpty())
                .andExpect(jsonPath("$.transactions").isEmpty());
    }
    public User createUserWithAccounts() {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        Account account1 = new Account();
        account1.setId(1L);
        account1.setType(AccountType.CURRENT);
        Account account2 = new Account();
        account2.setId(2L);
        account2.setType(AccountType.SAVINGS);
        List<Account> mockAccounts = Arrays.asList(account1, account2);
        mockUser.setAccounts(mockAccounts);
        return mockUser;
    }
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountButAccountTypeIsNotPresent() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = createUserWithAccounts();

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isEmpty())
                .andExpect(jsonPath("$.transactions").isEmpty());
    }
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrentNoTransaction() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = createUserWithAccounts();

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions?accountType=current")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions").isEmpty());
    }
    public List<Transaction> createTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction t1 = new Transaction();
        t1.setAmount(100.5);
        t1.setTransactionType(TransactionType.DEPOSIT);
        LocalDateTime dateTime = LocalDateTime.of(2024, 6, 7, 10, 30, 0);
        t1.setDateTime(dateTime);
        transactions.add(t1);
        t1 = new Transaction();
        t1.setAmount(200.5);
        t1.setTransactionType(TransactionType.TRANSFER);
        dateTime = LocalDateTime.of(2024, 7, 7, 10, 30, 0);
        t1.setDateTime(dateTime);
        transactions.add(t1);
        t1 = new Transaction();
        t1.setAmount(300.5);
        t1.setTransactionType(TransactionType.WITHDRAWAL);
        dateTime = LocalDateTime.of(2024, 8, 7, 10, 30, 0);
        t1.setDateTime(dateTime);
        transactions.add(t1);
        return transactions;
    }
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrent() throws Exception {

        User mockUser = createUserWithAccounts();
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        List<Transaction> transactions = createTransactions();
        when(transactionService.getTransactionsByAccount(
                mockUser.getAccounts().get(0),null,null,null,null,null,null,null)).thenReturn(transactions);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions?accountType=current")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions", hasSize(3)));
    }
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrentWithFiltersForAmount() throws Exception {
        User mockUser = createUserWithAccounts();
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        List<Transaction> transactions = createTransactions();
        when(transactionService.getTransactionsByAccount(
                mockUser.getAccounts().get(0),null,
                null,null, Float.valueOf(50), Float.valueOf(350),null,null)).thenReturn(transactions);
        mockMvc.perform(get("/transactions?accountType=current&minAmount=50&maxAmount=350")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions", hasSize(3)));
    }
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrentWithFiltersForDates() throws Exception {
        User mockUser = createUserWithAccounts();
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        List<Transaction> transactions = createTransactions();
        when(transactionService.getTransactionsByAccount(
                mockUser.getAccounts().get(0),null,
                LocalDate.of(2022, 1, 1),LocalDate.of(2024,11,11), null, null,null,null)).thenReturn(transactions);
        mockMvc.perform(get("/transactions?accountType=current&startDate=2022-01-01&endDate=2024-11-11")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions", hasSize(3)));
    }



    ///ANDY's Tests
/*
    @Test
    @WithMockUser(username = "customer@email.com", authorities = {"CUSTOMER"})
    public void testCreateTransactionAsCustomer() throws Exception {
        // Setup mock user
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        mockUser.setUserType(List.of(UserType.CUSTOMER));

        // Setup mock accounts
        Account senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setBalance(500.0);
        senderAccount.setDailyLimit(1000.0);
        senderAccount.setAbsoluteLimit(10.0);
        senderAccount.setUser(mockUser);

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(200.0);
        receiverAccount.setDailyLimit(1000.0);
        receiverAccount.setAbsoluteLimit(10.0);


        // Setup mock transaction request
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO("senderAccount", "receiverAccount", 100, "WITHDRAWAL");
        Transaction transaction = new Transaction(senderAccount, receiverAccount, transactionRequest.amount(), LocalDateTime.now(), mockUser, TransactionType.WITHDRAWAL);

        // Setup mock transaction response
        TransactionResponseDTO transactionResponse = new TransactionResponseDTO(transaction);

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        when(accountService.getAccountByIban("senderAccount")).thenReturn(senderAccount);
        when(accountService.getAccountByIban("receiverAccount")).thenReturn(null);
        when(transactionService.createTransaction(transactionRequest, mockUser))
                .thenReturn(transactionResponse);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sender\":\"senderAccount\",\"receiver\":\"receiverAccount\",\"amount\":100.0,\"type\":\"WITHDRAWAL\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }


    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ADMIN", "CUSTOMER"})
    public void testCreateTransactionAsAdmin() throws Exception {
        // Setup mock user
        User mockUser = new User();
        mockUser.setEmail("admin@example.com");
        mockUser.setUserType(List.of(UserType.ADMIN));

        // Setup mock accounts
        Account senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setBalance(500.0);
        senderAccount.setDailyLimit(1000.0);
        senderAccount.setAbsoluteLimit(0.0);
        senderAccount.setIban("senderAccount");

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(200.0);
        receiverAccount.setDailyLimit(1000.0);
        receiverAccount.setAbsoluteLimit(0.0);
        receiverAccount.setIban("receiverAccount");

        // Setup mock transaction request
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO("senderAccount", "receiverAccount", 100, "TRANSFER");

        // Setup mock transaction response
        Transaction transaction = new Transaction(senderAccount, receiverAccount, transactionRequest.amount(), LocalDateTime.now(), mockUser, TransactionType.TRANSFER);
        TransactionResponseDTO transactionResponse = new TransactionResponseDTO(transaction);

        // Ensure the mock user is returned
        when(userService.getUserByEmail("admin@example.com")).thenReturn(mockUser);

        // Ensure the mock accounts are returned
        when(accountService.getAccountByIban("senderAccount")).thenReturn(senderAccount);
        when(accountService.getAccountByIban("receiverAccount")).thenReturn(receiverAccount);

        // Ensure the transaction creation returns the expected response
        when(transactionService.createTransaction(any(TransactionRequestDTO.class), eq(mockUser))).thenReturn(transactionResponse);

        // Mock hasAccess method in TransactionService
        when(accountService.getAccountByIban("senderAccount")).thenReturn(senderAccount);
        when(accountService.getAccountByIban("receiverAccount")).thenReturn(receiverAccount);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sender\":\"senderAccount\",\"receiver\":\"receiverAccount\",\"amount\":100.0,\"type\":\"TRANSFER\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }*/
}
