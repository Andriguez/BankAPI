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
    public ResponseEntity<CustomerTransactionsDTO> getCustomerTransactions(
            // optional filters to filter transactions
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Float minAmount,
            @RequestParam(required = false) Float exactAmount,
            @RequestParam(required = false) Float maxAmount,
            @RequestParam(required = false) String iban
    ) {
        System.out.println("Transaction controller called ");
        Account customerAccount = null;
        List<Transaction> transactions = new ArrayList<Transaction>();
        // find logged in user from her JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByEmail(email);
        // I want to send info about account and its transactions to the frontend. So, I created a new class that has
        // account and a list of transactions called CustomerTransactionsDTO
        CustomerTransactionsDTO customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
        if (loggedInUser.getAccounts().size() == 0) {
            // if customer does not have any accounts, she does not have any transactions too
            return ResponseEntity.status(200).body(customerTransactionsDTO);
        }
        if ("current".equals(accountType)) {
            // if accountType is current, find the "current" account of user.
            for (Account account : loggedInUser.getAccounts()) {
                if (account.getType() == AccountType.CURRENT) {
                    System.out.println("iban: " + account.getIban() + " - " + account.getType());
                    customerAccount = account;
                    break;
                }
            }
        }
        else if ("savings".equals(accountType)) {
            for (Account account : loggedInUser.getAccounts()) {
                if (account.getType() == AccountType.SAVINGS) {
                    System.out.println("iban: " + account.getIban() + " - " + account.getType());
                    customerAccount = account;
                    break;
                }
            }
        }
        else {
            return ResponseEntity.status(200).body(customerTransactionsDTO);
        }
        // getTransactionsByAccount method in transactionService gets inputs from frontend (some of them might be
        // null) and pass it to service and return transactions that match those filters.
        transactions = transactionService.getTransactionsByAccount(customerAccount, transactionType, startDate, endDate,
                minAmount, maxAmount, exactAmount, iban);
        customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
        return ResponseEntity.status(200).body(customerTransactionsDTO);
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


}
