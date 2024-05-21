package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.User;

public record UserOverviewDTO(Long Id, String firstName, String lastName) {
    public UserOverviewDTO(User user){
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }

}

