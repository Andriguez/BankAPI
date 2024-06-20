package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.DTO.AccountsDTO;
import nl.inholland.BankAPI.Model.DTO.NewAccountDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// RequestMapping determines which API calls are handled by this class. For this file, it is APIs which start with
// /accounts.
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping // route: /accounts
    public ResponseEntity<Object> getAccountsByCustomer(){
        // the following line extracts the email of customer from jwt token.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // getUserByEmail reads user information from database.
        User loggedUser = userService.getUserByEmail(email);

        try{
            List<Account> accounts = loggedUser.getAccounts();

            if (!accounts.isEmpty()) {
                if(loggedUser.getUserType().equals(List.of(UserType.ADMIN)) || loggedUser.getId() == accounts.get(0).getUser().getId()) {
                    AccountsDTO accountsDTO = new AccountsDTO(loggedUser.getAccounts());
                    return ResponseEntity.ok().body(accountsDTO);
                } else {
                    throw new IllegalArgumentException("User is not allowed to access this data!");
                }
            } throw new IllegalArgumentException ("this user has no accounts");
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(params = "userid")
    public ResponseEntity<Object> getAccountsById(@RequestParam Long userid) {
        try {
            User neededUser = userService.getUserById(userid);
            List<Account> neededAccounts = neededUser.getAccounts();
            return ResponseEntity.ok().body(neededAccounts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving accounts");
        }
    }

    @PostMapping(params="userid")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> OpenAccounts(@RequestParam Long userid, @RequestBody Map<String, Object> requestData) {

        User user;

        try {

            user = userService.getUserById(userid);
            if(user == null){
                throw new IllegalArgumentException("Incorrect user Id");
            }

            if(requestData.isEmpty()){
                throw new IllegalArgumentException("No input has been received");
            }

            List<NewAccountDTO> accountsData = List.of(
                    new NewAccountDTO(
                        user.getId(),
                        (Number) requestData.get("absolute1"),
                        (Number) requestData.get("daily1"),
                        AccountType.valueOf((String)requestData.get("type1"))),
                    new NewAccountDTO(
                        user.getId(),
                        (Number) requestData.get("absolute2"),
                        (Number) requestData.get("daily2"),
                        AccountType.valueOf((String)requestData.get("type2"))
            ));

            List<Account> accounts = accountService.createAccounts(accountsData);
            accounts.forEach(account -> userService.AddAccountToUser(user, account));

            userService.changeGuestToUser(user);

            return ResponseEntity.ok().body(new AccountsDTO(accounts));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @PutMapping(params = "userid")
    public ResponseEntity<Object> updateAccounts(@RequestParam Long userid, @RequestBody Map<String, Object> requestData) {
        try {

            User user = accountService.updateAccounts(userService.getUserById(userid), requestData);
            return ResponseEntity.ok().body(new AccountsDTO(user.getAccounts()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating accounts");
        }
    }
    @DeleteMapping(params = "userid")
    public ResponseEntity<Object> closeAccounts(@RequestParam Long userid) {
        try {

            List<Account> accounts = accountService.closeUserAccounts(userService.getUserById(userid));
            return ResponseEntity.ok().body(new AccountsDTO(accounts));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while closing accounts");
        }
    }
}
