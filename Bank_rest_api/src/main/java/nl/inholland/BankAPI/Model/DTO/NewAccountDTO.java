package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.AccountType;

public record NewAccountDTO(Long userId, double absolute, double daily, String type) {

}
