package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import nl.inholland.BankAPI.Model.User;

import java.time.LocalDateTime;

public record TransactionResponseDTO(Account sender, Account receiver, double amount, LocalDateTime dateTime, User initiator, TransactionType type) {
    public TransactionResponseDTO (Transaction transaction){
        this(
                transaction.getSenderAccount(),
                transaction.getReceiverAccount(),
                transaction.getAmount(),
                transaction.getDateTime(),
                transaction.getUserInitiating(),
                transaction.getTransactionType()
        );
    }
}
