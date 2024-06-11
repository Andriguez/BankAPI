package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.AccountType;

public record NewAccountDTO(Long userId, Number absolute, Number daily, AccountType type) {
}
