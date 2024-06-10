package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.User;

import java.util.Map;
import java.util.stream.Collectors;

public record UserDTO(Long Id, String firstName, String lastName, String email, long phoneNumber, long bsnNumber, Map<AccountType, NewAccountDTO> accountsInfo) {
    public UserDTO(User user){
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBsnNumber(),
                user.getAccounts().stream()
                        .collect(Collectors.toMap(
                                account -> account.getType(),
                                account -> new NewAccountDTO(
                                        user.getId(),
                                        account.getAbsoluteLimit(),
                                        account.getDailyLimit(),
                                        account.getType()
                                )
                        ))
        );
    }

}
