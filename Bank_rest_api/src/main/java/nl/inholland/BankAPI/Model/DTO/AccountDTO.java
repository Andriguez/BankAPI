package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AccountDTO(Long id, String iban, AccountType type, double balance, double absolute, double daily,List<Transaction> receivedTransactions, List<Transaction> sentTransactions ) {
    public AccountDTO(Account account){
        this(
                account.getId(),
                account.getIban(),
                account.getType(),
                account.getBalance(),
                account.getAbsoluteLimit(),
                account.getDailyLimit(),
                account.getReceivedTransactions(),
                account.getSentTransactions()
        );

    }
}