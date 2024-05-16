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

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Account>> getCustomerAccounts(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String iban
    ) {
        List<Account> accounts = new ArrayList<Account>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // to complete: get user type from authentication later
        UserType userType = UserType.CUSTOMER;
        System.out.println("username:");
        System.out.println(authentication.getName());
        String j_username = authentication.getName();
        System.out.println("q_username:");
        System.out.println(username);

        // A customer user can only see her own account.
        if (userType == UserType.CUSTOMER && !j_username.equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accounts);
        }
        accounts = accountService.getAccounts(username, iban);
        return ResponseEntity.status(200).body(accounts);
    }
}
