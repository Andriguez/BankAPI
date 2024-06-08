package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import nl.inholland.BankAPI.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
}
