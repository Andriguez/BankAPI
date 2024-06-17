package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.CustomerTransactionsDTO;
import nl.inholland.BankAPI.Model.DTO.TransactionRequestDTO;
import nl.inholland.BankAPI.Model.DTO.TransactionResponseDTO;
import nl.inholland.BankAPI.Repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountService accountService, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.userService = userService;
    }

    // Sara's Code
    // getTransactions get all transactions that satisfy the given filters. Some of the inputs might be null in that
    // case they are ignored.
    public List<Transaction> getTransactionsByAccount(
            Account account, TransactionType transactionType,
            LocalDate startDate, LocalDate endDate,
            Float minAmount, Float maxAmount, Float exactAmount,
            String iban) {

        return getTransactionsByAccount(account, transactionType,
                startDate, endDate, minAmount, maxAmount, exactAmount, iban, null, null);
    }
    // Sara's Code
    public List<Transaction> getTransactionsByAccount(
            Account account, TransactionType transactionType,
            LocalDate startDate, LocalDate endDate,
            Float minAmount, Float maxAmount, Float exactAmount,
            String iban, Integer skip, Integer limit) {

        // JPA is set up to automatically set all transactions sent by account in sentTransactions array and similar
        // for received transactions.
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(account.getSentTransactions());
        transactions.addAll(account.getReceivedTransactions());
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(transaction -> transactionType == null || transaction.getTransactionType() == transactionType)
                .filter(transaction -> startDate == null || transaction.getDateTime().isAfter(startDate.atStartOfDay()))
                .filter(transaction -> endDate == null || transaction.getDateTime().isBefore(endDate.atStartOfDay()))
                .filter(transaction -> minAmount == null || transaction.getAmount() >= minAmount)
                .filter(transaction -> maxAmount == null || transaction.getAmount() <= maxAmount)
                .filter(transaction -> exactAmount == null || transaction.getAmount() == exactAmount)
                .filter(transaction -> iban == null ||
                        transaction.getSenderAccount().getIban().contains(iban) ||
                        transaction.getReceiverAccount().getIban().contains(iban))
                .skip(skip != null ? skip : 0)
                .limit(limit != null ? limit : Integer.MAX_VALUE)
                .collect(Collectors.toList());
        return filteredTransactions;
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionData, User initiator) throws Exception {

        try {
            Map<String, Account> accounts = getTransactionAccounts(transactionData.sender(), transactionData.receiver());
            Account sender = accounts.get("sender");
            Account receiver = accounts.get("receiver");

            TransactionType type = TransactionType.valueOf(transactionData.type());

            if (checkLimits(sender, transactionData.amount())) {
                System.out.println("limits are okay");

                Boolean updatedSenderBalance = setAccountBalance(type, "sender", sender, transactionData.amount());
                Boolean updatedReceiverBalance = setAccountBalance(type, "receiver", receiver, transactionData.amount());

                if (updatedSenderBalance && updatedReceiverBalance) {
                    Transaction transaction = new Transaction(sender, receiver, transactionData.amount(), LocalDateTime.now(), initiator, type);
                    transactionRepository.save(transaction);
                    System.out.println("balances are updated");

                    return new TransactionResponseDTO(transaction);
                }
            }

            throw new Exception("Transaction limits are being violated");

        } catch (Exception e) {
            throw e;
        }
    }

    public Boolean setAccountBalance(TransactionType transactionType, String transactionRole, Account account, double amount){
        if (account == null) {
            return true;
        }

        double balance = account.getBalance();
        if ("receiver".equals(transactionRole) || TransactionType.DEPOSIT.equals(transactionType)) {
            balance += amount;
        } else if ("sender".equals(transactionRole) || TransactionType.WITHDRAWAL.equals(transactionType)) {
            balance -= amount;
        }

        if (balance != account.getBalance()) {
            accountService.updateBalance(account, balance);
            System.out.println(account.getBalance());
            return true;
        }

        return false;

    }

    public Boolean checkLimits(Account account, double amount){

        if(account == null){
            return true;
        }

        //double dailyLimit = account.getDailyLimit();
        double totalTransactions = account.getSentTransactions().stream().filter(transaction -> transaction.getDateTime().toLocalDate().equals(LocalDate.now())).mapToDouble(Transaction::getAmount).sum();
        System.out.println("total transactions: "+totalTransactions + " limit: "+account.getDailyLimit());

        if (totalTransactions + amount > account.getDailyLimit()) {
            System.out.println("Daily limit exceeded.");
            return false; // Exceeds daily limit
        }

        double remainingBalance = account.getBalance() - amount;
        if (remainingBalance < account.getAbsoluteLimit()) {
            System.out.println("Absolute limit exceeded: " + account.getAbsoluteLimit() + " amount remaining: " + remainingBalance);
            return false; // Violates absolute limit
        }

        return true; // Within limits

    }

    private Map<String, Account> getTransactionAccounts(String requestSender, String requestReceiver){
        Map<String, Account> accounts = new HashMap<>();

        Account sender;
        Account receiver;

        if(isATM(requestSender)){
            sender = new ATMAccount();
            receiver = accountService.getAccountByIban(requestReceiver);
        } else if (isATM(requestReceiver)){
            sender = accountService.getAccountByIban(requestReceiver);
            receiver = new ATMAccount();
        } else {
            sender = accountService.getAccountByIban(requestSender);
            receiver = accountService.getAccountByIban(requestReceiver);
        }

        accounts.put("sender", sender);
        accounts.put("receiver", receiver);

        return accounts;

    }

    private Boolean isATM(String input){
        if (input == "NLXXINHOXXXXXXXXXX" || input == null){
            return true;
        }

        return false;
    }

    public List<Transaction> getTransactionByUserId(long id, Integer skip, Integer limit){

            List<Transaction> filteredTransactions = new ArrayList<>();
            User neededUser = userService.getUserById(id);
            for (Transaction t:transactionRepository.findAll()) {
                if(t.getUserInitiating() == neededUser){
                    filteredTransactions.add(t);
                }
            }
        return filteredTransactions.stream()
                .skip(skip != null ? skip : 0)
                .limit(limit != null ? limit : Integer.MAX_VALUE)
                .collect(Collectors.toList());
    }


    public List<Transaction> getAdminInitiatedTransactions() {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction t : getAllTransactions()) {
            if (t.getUserInitiating().getUserType().equals(UserType.ADMIN)) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    public List<Transaction> getUserInitiatedTransactions() {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction t : getAllTransactions()) {
            if (t.getUserInitiating().getUserType().equals(List.of(UserType.CUSTOMER))) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    public List<Transaction> getATMInitiatedTransactions(){

        //todo
        List<Transaction> filteredTransactions = new ArrayList<>();
        String initiatingIban;
        for (Transaction t:transactionRepository.findAll()) {
            if(t.getUserInitiating().getUserType().equals(UserType.ADMIN)){
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> filterTransactions(String condition,Integer skip, Integer limit){
        List<Transaction> filteredTransactions = new ArrayList<>();
        switch (condition){
            //case ALL = all transactions
            case "ALL": filteredTransactions = getAllTransactions();break;
            //case ATM = ATM transactions
            case "ATM": filteredTransactions = getATMInitiatedTransactions();break;
            //case ADMIN= get admin initiated transactions();
            case "ADMIN": filteredTransactions = getAdminInitiatedTransactions();break;
            //case CUSTOMER = user initiated transactions
            case "CUSTOMER": filteredTransactions = getUserInitiatedTransactions();break;
            default: throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        return filteredTransactions.stream()
                .skip(skip != null ? skip : 0)
                .limit(limit != null ? limit : Integer.MAX_VALUE)
                .collect(Collectors.toList());
    }

    // Sara's Code
    public CustomerTransactionsDTO getUserTransactions(User userToFindTransactions, String accountType,
                                                       TransactionType transactionType, LocalDate startDate,
                                                       LocalDate endDate, Float minAmount, Float maxAmount,
                                                       Float exactAmount, String iban, Integer skip, Integer limit) {
        Account customerAccount = null;
        List<Transaction> transactions = new ArrayList<Transaction>();
        // I want to send info about account and its transactions to the frontend. So, I created a new class that has
        // account and a list of transactions called CustomerTransactionsDTO
        CustomerTransactionsDTO customerTransactionsDTO;
        if (userToFindTransactions.getAccounts().size() == 0) {
            // if customer does not have any accounts, she does not have any transactions too. We return empty dto.
            customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
            return customerTransactionsDTO;
        }
        // fund the account with provided accountType
        for (Account account : userToFindTransactions.getAccounts()) {
            if (accountType.equals(account.getType().toString())) {
                customerAccount = account;
                break;
            }
        }
        if (customerAccount == null) {
            // if customer does not have any accounts, she does not have any transactions too. We return empty dto.
            customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
            return customerTransactionsDTO;
        }
        transactions = getTransactionsByAccount(customerAccount, transactionType, startDate, endDate,
                minAmount, maxAmount, exactAmount, iban, skip, limit);
        customerTransactionsDTO = new CustomerTransactionsDTO(customerAccount, transactions);
        return customerTransactionsDTO;
    }
}
