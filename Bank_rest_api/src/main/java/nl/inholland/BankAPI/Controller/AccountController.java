package nl.inholland.BankAPI.Controller;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.DTO.NewAccountDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
// RequestMapping determines which API calls are handled by this class. For this file, it is APIs which start with
// /accounts.
@RequestMapping("/accounts")
public class AccountController {
    private AccountService accountService;
    private UserService userService;

    public AccountController(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping // route: /accounts
    public ResponseEntity<List<Account>> getAccountsByCustomer(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // getUserByEmail reads user information from database.
        User loggedUser = userService.getUserByEmail(email);
        List<Account> accounts = loggedUser.getAccounts();
        return ResponseEntity.ok().body(accounts);
    }

    @PostMapping(params="userid")
    public ResponseEntity<List<Account>> OpenAccounts(@RequestParam Long userid, @RequestBody Map<String, Object> requestData) {

        User user;

        try {
            user = userService.getUserById(userid);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        //AccountType account1_type = AccountType.valueOf(account1.type());
        //AccountType account2_type = AccountType.valueOf(account2.type().name());

        NewAccountDTO account1 = new NewAccountDTO(
                user.getId(),
                (Number) requestData.get("absolute1"),
                (Number) requestData.get("daily1"),
                AccountType.valueOf((String)requestData.get("type1"))
        );

        NewAccountDTO account2 = new NewAccountDTO(
                user.getId(),
                (Number) requestData.get("absolute2"),
                (Number) requestData.get("daily2"),
                AccountType.valueOf((String)requestData.get("type2"))
        );

        Account account_1 = new Account(accountService.generateIBAN(), 0, account1.absolute().doubleValue(), account1.daily().doubleValue(), account1.type());
        Account account_2 = new Account(accountService.generateIBAN(), 0, account2.absolute().doubleValue(), account2.daily().doubleValue(), account2.type());

        accountService.createAccount(account_1);
        accountService.createAccount(account_2);

        userService.AddAccountToUser(user, account_1);
        userService.AddAccountToUser(user, account_2);

        //user.addAccount(account_1);
        //user.addAccount(account_2);

        //user.setUserType(List.of(UserType.CUSTOMER));
        userService.changeGuestToUser(user);


        return ResponseEntity.ok().body(user.getAccounts());

    }
}
