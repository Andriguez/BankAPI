package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.TransactionRequestDTO;
import nl.inholland.BankAPI.Model.DTO.TransactionResponseDTO;
import nl.inholland.BankAPI.Repository.TransactionRepository;
import org.springframework.cglib.core.Local;
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

    // getTransactions get all transactions that satisfy the given filters. Some of the inputs might be null in that
    // case they are ignored.
    public List<Transaction> getTransactionsByAccount(
            Account account, TransactionType transactionType,
            LocalDate startDate, LocalDate endDate,
            Float minAmount, Float maxAmount, Float exactAmount,
            String iban) {
        List<Transaction> transactionsSent = account.getSentTransactions();
        List<Transaction> transactionsReceived = account.getReceivedTransactions();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(transactionsSent);
        transactions.addAll(transactionsReceived);
        System.out.println("Transaction service called ");
        System.out.println(transactions.size());
        if(transactionType != null) {
            // filter transactions to keep transactions with transactionType (got from method inputs that came from
            // API call)
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getTransactionType() == transactionType)
                    .collect(Collectors.toList());
            System.out.println("found by transaction type " + transactionType );
            System.out.println(transactions.size());
        }
        if(startDate != null) {
            transactions = transactions.stream()
                    // when filtering by time, we should convert the startDate (which is date) to dateTime and we
                    // should .asStartOfDay to convert date (received from frontend) to dateTime.
                    .filter(transaction -> transaction.getDateTime().isAfter(startDate.atStartOfDay()))
                    .collect(Collectors.toList());
            System.out.println("found by after startDate " + startDate.toString() );
            System.out.println(transactions.size());
        }
        if(endDate != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getDateTime().isBefore(endDate.atStartOfDay()))
                    .collect(Collectors.toList());
            System.out.println("found by before endDate " + endDate.toString() );
            System.out.println(transactions.size());
        }
        if(minAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() >= minAmount)
                    .collect(Collectors.toList());
            System.out.println("found by bigger than " + minAmount.toString() );
            System.out.println(transactions.size());
        }
        if(maxAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() <= maxAmount)
                    .collect(Collectors.toList());
            System.out.println("found by smaller than " + maxAmount.toString() );
            System.out.println(transactions.size());
        }
        if(exactAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() == exactAmount)
                    .collect(Collectors.toList());
            System.out.println("found by equal to " + exactAmount.toString() );
            System.out.println(transactions.size());
        }
        if(iban != null) {
            transactions = transactions.stream()
                    .filter(transaction -> {
                        // I check to see if the provided iban filter is in sender account or receiver account iban
                        if (transaction.getSenderAccount().getIban().contains(iban)) {
                            return true;
                        }
                        else if(transaction.getReceiverAccount().getIban().contains(iban)) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
            System.out.println("found by iban to " + iban );
            System.out.println(transactions.size());
        }
        return transactions;
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
}
