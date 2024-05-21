package nl.inholland.BankAPI.Controller;
import nl.inholland.BankAPI.Model.*;
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
// RequestMapping determines with API calls are handled by this controller.
@RequestMapping("/transactions")
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService){
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    // GetMapping without any inputs means the base API. So, APIs calling /transactions will be handled by the
    // following method.
    @GetMapping("/myTransactions")
    // getTransactions can have different Request Params, all of them are optional.
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            // optional filters to filter transactions
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Float minAmount,
            @RequestParam(required = false) Float maxAmount,
            @RequestParam(required = false) Float exactAmount
    ) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = loggedInUser.getId();
        // getTransactions method in transactionService gets inputs (some of them might be null) and return
        // transactions that match those filters.
        transactions = transactionService.getTransactionsByUserId(userId, transactionType, startDate, endDate,
                minAmount, maxAmount, exactAmount);
        return ResponseEntity.status(200).body(transactions);
    }
}
