package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import nl.inholland.BankAPI.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository, accountService, userService);
        account = mock(Account.class);
        List<Transaction> sentTransactions = new ArrayList<>();
        List<Transaction> receivedTransactions = new ArrayList<>();

        Transaction t1 = new Transaction();
        t1.setAmount(100.5);
        t1.setTransactionType(TransactionType.DEPOSIT);
        LocalDateTime dateTime = LocalDateTime.of(2024, 6, 7, 10, 30, 0);
        t1.setDateTime(dateTime);
        receivedTransactions.add(t1);

        t1 = new Transaction();
        t1.setAmount(200.5);
        t1.setTransactionType(TransactionType.TRANSFER);
        dateTime = LocalDateTime.of(2024, 7, 7, 10, 30, 0);
        t1.setDateTime(dateTime);
        receivedTransactions.add(t1);

        t1 = new Transaction();
        t1.setAmount(300.5);
        t1.setTransactionType(TransactionType.WITHDRAWAL);
        dateTime = LocalDateTime.of(2024, 8, 7, 10, 30, 0);
        t1.setDateTime(dateTime);
        receivedTransactions.add(t1);

        when(account.getSentTransactions()).thenReturn(sentTransactions);
        when(account.getReceivedTransactions()).thenReturn(receivedTransactions);
    }

    @Test
    void testGetTransactionsByAccountWhenNoTransactionIsAvailable() {
        Account emptyAccount = mock(Account.class);
        when(emptyAccount.getSentTransactions()).thenReturn(new ArrayList<>());
        when(emptyAccount.getReceivedTransactions()).thenReturn(new ArrayList<>());
        List<Transaction> result = transactionService.getTransactionsByAccount(
                emptyAccount, null, null, null, null, null, null, null);

        assertEquals(0, result.size());
    }
    @Test
    void testGetTransactionsByAccountGetAll() {
        // Test
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, null, null, null, null);

        // Assertions
        assertEquals(3, result.size());
    }
    @Test
    void testGetTransactionsByAccountFilterTypeDeposit() {
        // Test
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, TransactionType.DEPOSIT, null, null, null, null, null, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getTransactionType(), TransactionType.DEPOSIT);
    }
    @Test
    void testGetTransactionsByAccountFilterTypeWithdraw() {
        // Test
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, TransactionType.WITHDRAWAL, null, null, null, null, null, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getTransactionType(), TransactionType.WITHDRAWAL);
    }
    @Test
    void testGetTransactionsByAccountFilterStartDate() {
        // Test
        LocalDate localDate = LocalDate.of(2024,6,10);
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, localDate, null, null, null, null, null);

        // Assertions
        assertEquals(2, result.size());
        assertEquals(result.get(0).getDateTime().isAfter(localDate.atStartOfDay()), true);
        assertEquals(result.get(1).getDateTime().isAfter(localDate.atStartOfDay()), true);
    }
    @Test
    void testGetTransactionsByAccountFilterStartDate2() {
        // Test
        LocalDate localDate = LocalDate.of(2024,9,10);
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, localDate, null, null, null, null, null);

        // Assertions
        assertEquals(0, result.size());
    }
    @Test
    void testGetTransactionsByAccountFilterEndDate() {
        // Test
        LocalDate localDate = LocalDate.of(2024,6,10);
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, localDate, null, null, null, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getDateTime().isBefore(localDate.atStartOfDay()), true);
    }
    @Test
    void testGetTransactionsByAccountFilterEndDate2() {
        // Test
        LocalDate localDate = LocalDate.of(2024,5,10);
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, localDate, null, null, null, null);

        // Assertions
        assertEquals(0, result.size());
    }
    @Test
    void testGetTransactionsByAccountFilterStartEndDate() {
        // Test
        LocalDate localDateStart = LocalDate.of(2024,6,10);
        LocalDate localDateEnd = LocalDate.of(2024,7,10);
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, localDateStart, localDateEnd, null, null, null, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getDateTime().isBefore(localDateEnd.atStartOfDay()) && result.get(0).getDateTime().isAfter(localDateStart.atStartOfDay()), true);
    }
    @Test
    void testGetTransactionsByAccountFilterMinAmount() {
        // Test
        Float min = 200.5f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, min, null, null, null);

        // Assertions
        assertEquals(2, result.size());
        assertEquals(result.get(0).getAmount() >= min, true);
    }
    @Test
    void testGetTransactionsByAccountFilterMinAmount2() {
        // Test
        Float min = 1200.5f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, min, null, null, null);

        // Assertions
        assertEquals(0, result.size());
    }
    @Test
    void testGetTransactionsByAccountFilterMaxAmount() {
        // Test
        Float max = 190.5f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, null, max, null, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getAmount() <= max, true);
    }
    @Test
    void testGetTransactionsByAccountFilterMaxAmount2() {
        // Test
        Float max = 90.5f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, null, max, null, null);

        // Assertions
        assertEquals(0, result.size());
    }
    @Test
    void testGetTransactionsByAccountFilterMinMaxAmount() {
        // Test
        Float min = 190.5f;
        Float max = 210.0f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, min, max, null, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getAmount() >= min && result.get(0).getAmount() <= max, true);
    }
    @Test
    void testGetTransactionsByAccountFilterEqualAmount() {
        // Test
        Float equal = 200.5f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, null, null, equal, null);

        // Assertions
        assertEquals(1, result.size());
        assertEquals(result.get(0).getAmount() == equal, true);
    }
    @Test
    void testGetTransactionsByAccountFilterEqualAmount2() {
        // Test
        Float equal = 199.5f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, null, null, null, null, equal, null);

        // Assertions
        assertEquals(0, result.size());
    }
    @Test
    void testGetTransactionsByAccountFilterStartEndDateAndMinMaxAmount() {
        // Test
        LocalDate localDateStart = LocalDate.of(2024,6,10);
        LocalDate localDateEnd = LocalDate.of(2024,7,10);
        // Test
        Float min = 190.5f;
        Float max = 210.0f;
        List<Transaction> result = transactionService.getTransactionsByAccount(
                account, null, localDateStart, localDateEnd, min, max, null, null);
        // Assertions
        assertEquals(1, result.size());
        assertEquals(
                result.get(0).getDateTime().isBefore(localDateEnd.atStartOfDay())
                        && result.get(0).getDateTime().isAfter(localDateStart.atStartOfDay())
                        && result.get(0).getAmount() >= min && result.get(0).getAmount() <= max,
                true);
    }
}
