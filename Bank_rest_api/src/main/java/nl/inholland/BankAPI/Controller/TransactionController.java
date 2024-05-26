package nl.inholland.BankAPI.Controller;
import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.CustomerTransactionsDTO;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.TransactionService;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
// RequestMapping determines with API calls are handled by this controller.
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;
    private UserService userService;

    public TransactionController(TransactionService transactionService, AccountService accountService,
                                 UserService userService){
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
    }

    // GetMapping without any inputs means the base API. So, APIs calling /transactions will be handled by the
    // following method.
    @GetMapping //route: /transactions
    // getTransactions can have different Request Params, all of them are optional.
    public ResponseEntity<CustomerTransactionsDTO> getCustomerTransactions(
            // optional filters to filter transactions
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Float minAmount,
            @RequestParam(required = false) Float exactAmount,
            @RequestParam(required = false) Float maxAmount,
            @RequestParam(required = false) String iban
    ) {
        Account customerAccount = null;
        List<Transaction> transactions = new ArrayList<Transaction>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByEmail(email);
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
        if (loggedInUser.getAccounts().size() == 0) {
            // if customer does not have any accounts, she does not have any transactions too
            return ResponseEntity.status(200).body(customerTransactionsDTO);
        }
        if (accountType.equals("current")) {
            for (Account account : loggedInUser.getAccounts()) {
                if (account.getType() == AccountType.CURRENT) {
                    System.out.println("iban: " + account.getIban() + " - " + account.getType());
                    customerAccount = account;
                    break;
                }
            }
        }
        else if (accountType.equals("savings")) {
            for (Account account : loggedInUser.getAccounts()) {
                if (account.getType() == AccountType.SAVINGS) {
                    System.out.println("iban: " + account.getIban() + " - " + account.getType());
                    customerAccount = account;
                    break;
                }
            }
        }
        // getTransactions method in transactionService gets inputs (some of them might be null) and return
        // transactions that match those filters.
        transactions = transactionService.getTransactionsByAccountId(customerAccount, transactionType, startDate, endDate,
                minAmount, maxAmount, exactAmount, iban);
        customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
        return ResponseEntity.status(200).body(customerTransactionsDTO);
    }
}
