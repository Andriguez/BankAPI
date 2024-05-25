package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;

import java.util.List;

public record CustomerTransactionsDTO (Account account, List<Transaction> transactions) {
}
