package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.CustomerTransactionsDTO;
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
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    // Sara's Code
    @Test
    public void testGetTransactionsByCustomerNoAuthenticationFails() throws Exception {
        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions")).andDo(print())
                .andExpect(status().is(400));
    }

    // Sara's Code
    public User createUserWithAccounts() {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        Account account1 = new Account();
        List<Transaction> listTransactions = new ArrayList<>();
        account1.setReceivedTransactions(listTransactions);
        account1.setSentTransactions(listTransactions);
        account1.setId(1L);
        account1.setType(AccountType.CURRENT);
        Account account2 = new Account();
        account2.setId(2L);
        account2.setReceivedTransactions(listTransactions);
        account2.setSentTransactions(listTransactions);
        account2.setType(AccountType.SAVINGS);
        List<Account> mockAccounts = Arrays.asList(account1, account2);
        mockUser.setAccounts(mockAccounts);
        mockUser.setUserType(Arrays.asList(UserType.CUSTOMER));
        return mockUser;
    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountButAccountTypeIsNotPresent() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = createUserWithAccounts();

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions")).andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string("accountType should be present"));
    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsCustomerLoggedUserWithNoAccount() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = new User();
        mockUser.setEmail("customer@email.com");
        mockUser.setUserType(List.of(UserType.CUSTOMER));
        List<Account> mockAccounts = new ArrayList<>();
        mockUser.setAccounts(mockAccounts);
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        List<Transaction> transactions = new ArrayList<>();
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO((Account) null,
                transactions);
        when(transactionService.getUserTransactions(mockUser, "CURRENT", null,
                null, null, null, null,
                null, null, null, null)).thenReturn(customerTransactionsDTO);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions?accountType=current")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account.id").isEmpty())
                .andExpect(jsonPath("$.transactions").isEmpty());
    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrentNoTransaction() throws Exception {
        // Mock the user and accounts returned by the userService and accountService
        User mockUser = createUserWithAccounts();

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        List<Transaction> transactions = new ArrayList<>();
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO(mockUser.getAccounts().get(0),
                transactions);
        when(transactionService.getUserTransactions(mockUser, "CURRENT", null,
                null, null, null, null,
                null, null, null, null)).thenReturn(customerTransactionsDTO);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions?accountType=current")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions").isEmpty());
    }

    // Sara's Code
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

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrent() throws Exception {

        User mockUser = createUserWithAccounts();
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);

        List<Transaction> transactions = createTransactions();
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO(mockUser.getAccounts().get(0),
                transactions);
        when(transactionService.getUserTransactions(mockUser, "CURRENT", null,
                null, null, null, null,
                null, null, null, null)).thenReturn(customerTransactionsDTO);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/transactions?accountType=current")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions", hasSize(3)));
    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrentWithFiltersForAmount() throws Exception {
        User mockUser = createUserWithAccounts();
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        List<Transaction> transactions = createTransactions();
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO(mockUser.getAccounts().get(0),
                transactions);
        when(transactionService.getUserTransactions(mockUser, "CURRENT", null,
                null, null, 50f, 350f,
                null, null, null, null)).thenReturn(customerTransactionsDTO);

        mockMvc.perform(get("/transactions?accountType=current&minAmount=50&maxAmount=350")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions", hasSize(3)));
    }

    // Sara's Code
    @Test
    @WithMockUser(username = "customer@email.com", roles = {"CUSTOMER"})
    public void testGetTransactionsByCustomerLoggedUserWithAccountCurrentWithFiltersForDates() throws Exception {
        User mockUser = createUserWithAccounts();
        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        List<Transaction> transactions = createTransactions();
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO(mockUser.getAccounts().get(0),
                transactions);
        when(transactionService.getUserTransactions(mockUser, "CURRENT", null,
                LocalDate.of(2022, 1, 1),LocalDate.of(2024,11,11), null, null,
                null, null, null, null)).thenReturn(customerTransactionsDTO);

        mockMvc.perform(get("/transactions?accountType=current&startDate=2022-01-01&endDate=2024-11-11")).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.account").isNotEmpty())
                .andExpect(jsonPath("$.account.type").value("CURRENT"))
                .andExpect(jsonPath("$.transactions", hasSize(3)));
    }



    ///ANDY's Tests

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
        senderAccount.setIban("senderAccount");

        Account receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(200.0);
        receiverAccount.setDailyLimit(1000.0);
        receiverAccount.setAbsoluteLimit(10.0);
        receiverAccount.setIban("receiverAccount");


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
        when(accountService.hasAccess(mockUser, List.of(senderAccount.getIban(), receiverAccount.getIban()))).thenReturn(true);


        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sender\":\"senderAccount\",\"receiver\":\"receiverAccount\",\"amount\":100.0,\"type\":\"WITHDRAWAL\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100));
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
        when(accountService.hasAccess(mockUser, List.of(senderAccount.getIban(), receiverAccount.getIban()))).thenReturn(true);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sender\":\"senderAccount\",\"receiver\":\"receiverAccount\",\"amount\":100.0,\"type\":\"TRANSFER\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    @WithMockUser(username = "customer@example.com", authorities = {"CUSTOMER"})
    public void testCreateTransactionWithInvalidAccess() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("customer@example.com");
        mockUser.setUserType(List.of(UserType.CUSTOMER));

        // Setup mock accounts
        Account unauthorizedSenderAccount = new Account();
        unauthorizedSenderAccount.setId(1L);
        unauthorizedSenderAccount.setBalance(500.0);
        unauthorizedSenderAccount.setDailyLimit(1000.0);
        unauthorizedSenderAccount.setAbsoluteLimit(0.0);
        unauthorizedSenderAccount.setUser(mockUser);

        Account unauthorizedReceiverAccount = new Account();
        unauthorizedReceiverAccount.setId(2L);
        unauthorizedReceiverAccount.setBalance(200.0);
        unauthorizedReceiverAccount.setDailyLimit(1000.0);
        unauthorizedReceiverAccount.setAbsoluteLimit(0.0);

        // Setup mock transaction request
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO("unauthorizedSender", "unauthorizedReceiver", 100, "TRANSFER");

        // Mock the service calls
        when(userService.getUserByEmail("customer@example.com")).thenReturn(mockUser);
        when(accountService.getAccountByIban("unauthorizedSender")).thenReturn(unauthorizedSenderAccount);
        when(accountService.getAccountByIban("unauthorizedReceiver")).thenReturn(unauthorizedReceiverAccount);
        doThrow(new AuthorizationServiceException("User is not allowed to make this transaction"))
                .when(transactionService).createTransaction(any(TransactionRequestDTO.class), eq(mockUser));

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sender\":\"unauthorizedSender\",\"receiver\":\"unauthorizedReceiver\",\"amount\":100.0,\"type\":\"TRANSFER\"}"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("User is not allowed to make this transaction"));
    }
}

