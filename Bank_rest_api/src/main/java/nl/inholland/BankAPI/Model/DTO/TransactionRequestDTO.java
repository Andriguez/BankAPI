package nl.inholland.BankAPI.Model.DTO;

import java.time.LocalDateTime;

public record TransactionRequestDTO (String sender, String receiver, double amount, String type){

}
