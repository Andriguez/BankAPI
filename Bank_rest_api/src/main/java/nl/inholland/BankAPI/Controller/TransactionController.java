package nl.inholland.BankAPI.Controller;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.TransactionService;
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
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionService transactionService;
    private AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService){
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(required = false) String toFromIban, // first to from iban, used for ensuring a customer
            // only sees her own transactions
            @RequestParam(required = false) String toFromIban2, // to search the other iban
            @RequestParam(required = false)TransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Float minAmount,
            @RequestParam(required = false) Float maxAmount,
            @RequestParam(required = false) Float exactAmount
    ) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // to complete: get user type from authentication later
        UserType userType = UserType.CUSTOMER;
        System.out.println("username:");
        System.out.println(authentication.getName());
        String j_username = authentication.getName();
        // a customer can only see transactions of her own acounts
        if (userType == UserType.CUSTOMER) {
            if (!accountService.isUserAccount(j_username, toFromIban)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactions);
            }
        }
        transactions = transactionService.getTransactions(toFromIban, toFromIban2, transactionType, startDate,
                endDate, minAmount, maxAmount, exactAmount);
        return ResponseEntity.status(200).body(transactions);
    }
}
