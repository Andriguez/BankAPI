package nl.inholland.BankAPI.Controller;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
// RequestMapping determines which API calls are handled by this class. For this file, it is APIs which start with
// /accounts.
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    // /accounts can have a main route /accounts and many sub-routes such as /accounts/myAccount GetMapping
    // determines which one the following method is for.
    // GetMapping without nothig corresponds to the main route /accounts
    @GetMapping
    // when calling an API, we can pass different queries. For example: /accounts?email=test@gmail.com. It can habe
    // multiple queries. For example /accounts?email=test@gmail.com&iban=someIban
    // Setting queries are optional (since required = false) and if it is not provided, it will be null.
    public ResponseEntity<List<Account>> getCustomerAccounts(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String iban
    ) {
        List<Account> accounts = new ArrayList<Account>();
        // read email of the user from JWT. A customer should only be able to see her own accounts. We read the email
        // of the logged in user from jWT information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String j_email = authentication.getName();
        // to complete: get user type from authentication later
        UserType userType = UserType.CUSTOMER;

        // A customer user can only see her own account. If the user type is customer but she is tring to read
        // another user's accounts we reutrn an error of UNAUTHORIZED
        if (userType == UserType.CUSTOMER && !j_email.equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accounts);
        }
        // the getAccounts method in the accountService is responsible for returning the accounts based on the input
        // filters. We will add all the filters we want. Some of them are null in most of cases.
        accounts = accountService.getAccounts(email, iban);
        return ResponseEntity.status(200).body(accounts);
    }
}
