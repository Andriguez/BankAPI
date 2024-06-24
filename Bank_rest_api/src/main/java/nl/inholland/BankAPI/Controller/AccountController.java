package nl.inholland.BankAPI.Controller;

import jakarta.persistence.EntityNotFoundException;
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
                    throw new Exception("User is not allowed to access this data!");
                }
            } throw new Exception ("this user has no accounts");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(params = "userid")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAccountsById(@RequestParam final Long userid) {
        try {
            List<Account> neededAccounts = userService.getUserById(userid).getAccounts();
            return ResponseEntity.ok().body(neededAccounts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving accounts");
        }
    }

    @PostMapping(params="userid")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountsDTO> OpenAccounts(@RequestParam Long userid, @RequestBody Map<String, Object> requestData) throws EntityNotFoundException, IllegalArgumentException, RuntimeException {
        validateRequestData(requestData);
        User user;
        user = userService.getUserById(userid);
        if (user == null) {
            throw new EntityNotFoundException("Incorrect user Id");
        }

        if (requestData.isEmpty()) {
            throw new IllegalArgumentException("No input has been received");
        }

        if(!user.getAccounts().isEmpty()){
            throw new RuntimeException("User already has accounts open");
        }

        List<Account> accounts = accountService.createAccounts(
                List.of(
                        new NewAccountDTO(user.getId(), (Number) requestData.get("absolute1"), (Number) requestData.get("daily1"), AccountType.valueOf((String) requestData.get("type1"))),
                        new NewAccountDTO(user.getId(), (Number) requestData.get("absolute2"), (Number) requestData.get("daily2"), AccountType.valueOf((String) requestData.get("type2")))
                ));

        accounts.forEach(account -> userService.AddAccountToUser(user, account));
        userService.changeGuestToUser(user);

        return ResponseEntity.ok().body(new AccountsDTO(accounts));
    }
    @PutMapping(params = "userid")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateAccounts(@RequestParam final Long userid, @RequestBody final Map<String, Object> requestData) {
        try {
            validateRequestData(requestData);
            List<Account> accounts = accountService.updateAccounts(userService.getUserById(userid), requestData);
            return ResponseEntity.ok().body(new AccountsDTO(accounts));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private void validateRequestData(Map<String, Object> requestData) throws IllegalArgumentException {
        double absolute1 = ((Number) requestData.get("absolute1")).doubleValue();
        double daily1 = ((Number) requestData.get("daily1")).doubleValue();
        double absolute2 = ((Number) requestData.get("absolute2")).doubleValue();
        double daily2 = ((Number) requestData.get("daily2")).doubleValue();

        if (absolute1 < 0 || daily1 <= 0 || absolute2 < 0 || daily2 <= 0) {
            throw new IllegalArgumentException("Daily and absolute limits cannot be negative");
        }
    }
    @DeleteMapping(params = "userid")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> closeAccounts(@RequestParam final Long userid) {
        try {
            List<Account> accounts = accountService.closeUserAccounts(userService.getUserById(userid));
            return ResponseEntity.ok().body(new AccountsDTO(accounts));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
    }
}
