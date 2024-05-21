package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;

import java.util.List;

public record UserDTO(String firstName, String lastName, String email, long phoneNumber, long bsnNumber, List<UserType> userType) {

}
