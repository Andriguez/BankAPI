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

    // /accounts can have a main route /accounts and many sub-routes such as /accounts/myAccount GetMapping
    // determines which one the following method is for.
    // GetMapping without nothing corresponds to the main route /accounts
    //@GetMapping("/myAccounts")
    // when calling an API, we can pass different queries. For example: /accounts?email=test@gmail.com. It can habe
    // multiple queries. For example /accounts?email=test@gmail.com&iban=someIban
    // Setting queries are optional (since required = false) and if it is not provided, it will be null.
    //public ResponseEntity<List<Account>> getCustomerAccounts() {
      //  List<Account> accounts = new ArrayList<Account>();
        // read email of the user from JWT. A customer should only be able to see her own accounts. We read the email
        // of the logged in user from jWT information
        //User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //long userId = loggedInUser.getId();
        // the getAccounts method in the accountService is responsible for returning the accounts based on the input
        // filters. We will add all the filters we want. Some of them are null in most of cases.
        //accounts = accountService.getAccountsByUserId(userId);
        //return ResponseEntity.status(200).body(accounts);
    //}

    @GetMapping // route: /accounts
    public ResponseEntity<Object> getAccountsByCustomer(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
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
    @PutMapping(params="userid")
    public ResponseEntity<?> updateAccounts(@RequestParam Long userid, @RequestBody Map<String, Object> requestData) {
        User user;
        try {
            user = userService.getUserById(userid);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        // Get the accounts of the user
        List<Account> accounts = user.getAccounts();
        Account currentAccount = null;
        Account savingsAccount = null;
        // Find the CURRENT and SAVINGS accounts
        for (Account account : accounts) {
            if (account.getType() == AccountType.CURRENT) {
                currentAccount = account;
            } else if (account.getType() == AccountType.SAVINGS) {
                savingsAccount = account;
            }
        }
        // If either account is missing, return an error
        if (currentAccount == null || savingsAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Both CURRENT and SAVINGS accounts must exist for the user");
        }
        // Update the accounts with the new limits
        currentAccount.setAbsoluteLimit(((Number) requestData.get("absolute1")).doubleValue());
        currentAccount.setDailyLimit(((Number) requestData.get("daily1")).doubleValue());
        savingsAccount.setAbsoluteLimit(((Number) requestData.get("absolute2")).doubleValue());
        savingsAccount.setDailyLimit(((Number) requestData.get("daily2")).doubleValue());
        // Save the updated accounts
        accountService.updateAccount(currentAccount);
        accountService.updateAccount(savingsAccount);
        return ResponseEntity.ok().body(user.getAccounts());
    }
}
