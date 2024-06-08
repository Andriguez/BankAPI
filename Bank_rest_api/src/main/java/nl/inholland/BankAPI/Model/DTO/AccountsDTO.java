package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AccountsDTO(Map<AccountType, AccountDTO> accounts) {
    public AccountsDTO(List<Account> accountList){
        this(
                accountList.stream()
                        .collect(Collectors.toMap(
                                Account::getType,
                                account -> new AccountDTO(account)
                        ))
        );

    }
}
