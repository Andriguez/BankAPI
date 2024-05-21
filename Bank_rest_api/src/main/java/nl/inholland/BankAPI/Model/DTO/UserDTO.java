package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;

import java.util.List;

public record UserDTO(Long Id, String firstName, String lastName, String email, long phoneNumber, long bsnNumber, List<UserType> userType) {
    public UserDTO(User user){
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBsnNumber(),
                user.getUserType()
            );
    }

}
