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

    public List<Transaction> getTransactions(
            String toFromIban, String toFromIban2, TransactionType transactionType,
            LocalDate startDate, LocalDate endDate,
            Float minAmount, Float maxAmount, Float exactAmount) {
        List<Transaction> transactions = transactionRepository.findAll();
        if(toFromIban != null && !toFromIban.isEmpty()) {
            Account toFromAccount = accountService.getAccountByIban(toFromIban);
            if (toFromAccount == null) {
                // if not found, return empty list
                return new ArrayList<Transaction>();
            }
            transactions = transactions.stream()
                    .filter(transaction -> {
                        if (transaction.getSenderId() == toFromAccount.getId()
                                || transaction.getReceiverId() == toFromAccount.getId())
                        {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            System.out.println("found by to from iban " + toFromIban );
            System.out.println(transactions.size());
        }
        if(toFromIban2 != null && !toFromIban2.isEmpty()) {
            Account toFromAccount = accountService.getAccountByIban(toFromIban2);
            if (toFromAccount == null) {
                // if not found, return empty list
                return new ArrayList<Transaction>();
            }
            transactions = transactions.stream()
                    .filter(transaction -> {
                        if (transaction.getSenderId() == toFromAccount.getId()
                                || transaction.getReceiverId() == toFromAccount.getId())
                        {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            System.out.println("found by to from iban 2 " + toFromIban2 );
            System.out.println(transactions.size());
        }
        if(transactionType != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getTransactionType() == transactionType)
                    .collect(Collectors.toList());
            System.out.println("found by transaction type " + transactionType );
            System.out.println(transactions.size());
        }
        if(startDate != null) {
            transactions = transactions.stream()
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
                    .filter(transaction -> transaction.getAmount() > minAmount)
                    .collect(Collectors.toList());
            System.out.println("found by bigger than " + minAmount.toString() );
            System.out.println(transactions.size());
        }
        if(maxAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getAmount() < maxAmount)
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
        return transactions;
    }
}
