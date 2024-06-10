package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;
import nl.inholland.BankAPI.Model.DTO.*;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(params = "type")
    public ResponseEntity<List<UserOverviewDTO>> getUsersByType(String type){

        UserType userType;

        try{
            userType = UserType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<UserType> usertype = List.of(userType);
        List<User> registrations = userService.getUsersByType(usertype);

        List<UserOverviewDTO> userDtos = registrations.stream()
                .map(user -> new UserOverviewDTO(user.getId(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    @GetMapping(params = "id")
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long id){

        User user;
        try{
            if (id == 0){
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                user = userService.getUserByEmail(email);
            } else {
                user = userService.getUserById(id);
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBsnNumber(),
                getAccountInfo(user)));
    }

    private Map<AccountType, NewAccountDTO> getAccountInfo(User user){
        double account1Absolute = 0;
        double account2Absolute = 0;
        double account1Daily = 0;
        double account2Daily = 0;

        AccountType account1Type = AccountType.CURRENT;
        AccountType account2Type = AccountType.SAVINGS;

        if(!user.getAccounts().isEmpty()){
            account1Absolute = user.getAccounts().get(0).getAbsoluteLimit();
            account2Absolute = user.getAccounts().get(1).getAbsoluteLimit();
            account1Daily = user.getAccounts().get(0).getDailyLimit();
            account2Daily = user.getAccounts().get(1).getDailyLimit();

            account1Type = user.getAccounts().get(0).getType();
            account2Type = user.getAccounts().get(1).getType();

        }

        NewAccountDTO account1 = new NewAccountDTO(user.getId(), account1Absolute, account1Daily, account1Type);
        NewAccountDTO account2 = new NewAccountDTO(user.getId(), account2Absolute, account2Daily, account2Type);


        Map<AccountType, NewAccountDTO> accounts = new HashMap<>();
        accounts.put(account1Type, account1);
        accounts.put(account2Type, account2);

        return accounts;
    }
    // Endpoint to search user by first and last name
    @GetMapping("/search")
    public User searchUserByName(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {
        return userService.findUserByFirstNameAndLastName(firstName, lastName);
    }

}
