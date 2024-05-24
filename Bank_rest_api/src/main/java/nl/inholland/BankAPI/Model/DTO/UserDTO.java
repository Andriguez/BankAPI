package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
                                account -> account.getType(), // Key mapper function
                                account -> new NewAccountDTO( // Value mapper function
                                        user.getId(),
                                        account.getAbsoluteLimit(),
                                        account.getDailyLimit(),
                                        account.getType()
                                )
                        ))
        );
    }

}
