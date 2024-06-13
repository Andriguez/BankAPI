package nl.inholland.BankAPI.Model.DTO;

public record TransactionRequestDTO (String sender, String receiver, double amount, String type){

}
