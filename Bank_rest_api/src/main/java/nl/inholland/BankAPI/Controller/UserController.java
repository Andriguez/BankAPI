package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Model.DTO.UserDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<UserOverviewDTO>> getUsersByType(@PathVariable String type){

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

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){

        User user;
        try{
            user = userService.getUserById(id);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getBsnNumber()));
    }

}
