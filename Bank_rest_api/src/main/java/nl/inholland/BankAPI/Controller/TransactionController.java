package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.CustomerTransactionsDTO;
import nl.inholland.BankAPI.Model.DTO.TransactionRequestDTO;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.TransactionService;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    private String ATMIban = "NLXXINHOXXXXXXXXXX";
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
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CUSTOMER')")
    public ResponseEntity<Object> getCustomerTransactions(
            // optional filters to filter transactions
            @RequestParam(required = false) Long userId, // for admin to read a userId.
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Float minAmount,
            @RequestParam(required = false) Float exactAmount,
            @RequestParam(required = false) Float maxAmount,
            @RequestParam(required = false) String iban,
            @RequestParam(required = false) Integer skip,
            @RequestParam(required = false) Integer limit) {

        try{
            // find logged in user from her JWT
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User loggedInUser = userService.getUserByEmail(email);
            // this method can be called by a user or by admin. If it is called by admin, userId should be present.
            User userToFindTransactions;
            if (accountType == null) {
                return ResponseEntity.badRequest().body("accountType should be present");
            }
            accountType = accountType.toUpperCase();
            if (!accountType.equals(AccountType.CURRENT.toString()) && !accountType.equals(AccountType.SAVINGS.toString())) {
                return ResponseEntity.badRequest().body("accountType should be either CURRENT or SAVINGS");
            }
            if (loggedInUser.getUserType().contains(UserType.ADMIN)) {
                if(userId == null) {
                    return ResponseEntity.badRequest().body("userId should be present for admin");
                }
                userToFindTransactions = userService.getUserById(userId);
            }
            else {
                userToFindTransactions = loggedInUser;
            }

            // getUserTransactions method in transactionService gets transactions of the user based on the given
            // filters.
            // I want to send info about account and its transactions to the frontend. So, I created a new class that has
            // account and a list of transactions called CustomerTransactionsDTO
            CustomerTransactionsDTO customerTransactionsDTO =
              transactionService.getUserTransactions(userToFindTransactions, accountType, transactionType, startDate,
                      endDate, minAmount, maxAmount, exactAmount, iban, skip, limit);
            return ResponseEntity.status(200).body(customerTransactionsDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CUSTOMER')")
    public ResponseEntity<Object> CreateTransaction (@RequestBody TransactionRequestDTO transactionData){

        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User loggedUser = userService.getUserByEmail(email);

            if(hasAccess(loggedUser, transactionData.sender()) || hasAccess(loggedUser, transactionData.receiver())){
                return ResponseEntity.ok().body(transactionService.createTransaction(transactionData, loggedUser));
            }
             else {
                 throw new Exception("user is not allowed to make this transaction");
            }

        } catch (IllegalArgumentException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private Boolean hasAccess(User initiator, String iban){

        if(!initiator.getUserType().equals(List.of(UserType.GUEST))){
            if(initiator.getUserType().equals(List.of(UserType.ADMIN))){
                return true;
            }

            if(!ATMIban.equals(iban)){
                if(initiator.getId() == accountService.getAccountByIban(iban).getUser().getId()){
                    return true;
                }
            }
        }


        return false;
    }

    @GetMapping ("/history")//route: /transactions
    // getTransactions can have different Request Params, all of them are optional.
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> filterTransactions(
            // optional filters to filter transactions
            @RequestParam int condition,
            @RequestParam(required = false) Long userId, // for admin to read a userId.
            @RequestParam(required = false) Integer skip,
            @RequestParam(required = false) Integer limit) {

        try{
            return ResponseEntity.ok().body(transactionService.filterTransactions(condition,userId));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

}
