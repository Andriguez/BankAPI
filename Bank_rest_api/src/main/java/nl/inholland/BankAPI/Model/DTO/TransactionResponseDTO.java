package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import nl.inholland.BankAPI.Model.User;

import java.time.LocalDateTime;

public record TransactionResponseDTO(String sender, String receiver, double amount, LocalDateTime dateTime, User initiator, TransactionType type) {
    public TransactionResponseDTO (Transaction transaction){
        this(
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getIban() : "ATM",
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getIban() : "ATM",
                transaction.getAmount(),
                transaction.getDateTime(),
                transaction.getUserInitiating(),
                transaction.getTransactionType()
        );
    }
}
