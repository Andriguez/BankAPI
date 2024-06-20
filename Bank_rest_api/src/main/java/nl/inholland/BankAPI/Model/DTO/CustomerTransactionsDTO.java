package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;

import java.util.List;

public record CustomerTransactionsDTO (AccountDTO account, List<Transaction> transactions) {
    public CustomerTransactionsDTO (Account account, List<Transaction> transactions) {
        this(
                new AccountDTO(account),
                transactions
        );
    }
}
