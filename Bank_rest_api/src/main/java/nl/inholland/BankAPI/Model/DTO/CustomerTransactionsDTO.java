package nl.inholland.BankAPI.Model.DTO;

import io.cucumber.java.bs.A;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;

import java.util.List;

public record CustomerTransactionsDTO (AccountDTO accountDTO, List<Transaction> transactions) {
    public CustomerTransactionsDTO (Account account, List<Transaction> transactions) {
        this(
                new AccountDTO(account),
                transactions
        );
    }
}
