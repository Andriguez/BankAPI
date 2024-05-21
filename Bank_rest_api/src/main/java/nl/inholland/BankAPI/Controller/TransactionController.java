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
    @GetMapping
    // getTransactions can have different Request Params, all of them are optional.
    // The user can filter transactions based on 2 toFromIban (if a customer is calling this API, they should provide
    // their target account iban as a query input. If a customer does not provide the toFromIban, then they don't
    // have permission to view other's transactions.
    public ResponseEntity<List<Transaction>> getTransactions(
            @RequestParam(required = false) String toFromIban, // first to from iban, used for ensuring a customer
            // only sees her own transactions. A customer transaction is a transaction that she is at least on one
            // side of the transaction. Transactions can be filtered based on 2 ibans (one to determine the
            // customer's account iban and the other one to actually seach for another iban.
            @RequestParam(required = false) String toFromIban2, // to search the other iban
            @RequestParam(required = false) TransactionType transactionType,
            // the rest are rather obvious. Each transaction has a dateTime that it occurred. We want to filter
            // transactoins that are after startDate and before endDate.
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            // the rest are rather obvious. Each transaction has an amount. We want to filter transactions that are
            // bigger or smaller than one amount or has exact given amount.
            @RequestParam(required = false) Float minAmount,
            @RequestParam(required = false) Float maxAmount,
            @RequestParam(required = false) Float exactAmount
    ) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        // the following 2 lines read the logged in user email.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String j_email = authentication.getName();
        System.out.println("email:");
        System.out.println(authentication.getName());
        // to complete: get user type from authentication later
        UserType userType = UserType.CUSTOMER;
        // a customer can only see transactions of her own accounts, however, an admin can see all transactions.
        if (userType == UserType.CUSTOMER) {`
            // isUserAccount checks to see if the requested iban is for the logged in user. We will not show
            // transactions of another account to the user.
            if (!accountService.isUserAccount(j_email, toFromIban)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactions);
            }
        }
        // getTransactions method in transactionService gets inputs (some of them might be null) and return
        // transactions that match those filters.
        transactions = transactionService.getTransactions(toFromIban, toFromIban2, transactionType, startDate,
                endDate, minAmount, maxAmount, exactAmount);
        return ResponseEntity.status(200).body(transactions);
    }
}
