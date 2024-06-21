package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationDTO user) throws IllegalArgumentException{

            if(authenticateInput(user)){
                return ResponseEntity.ok().body(userService.createUserDTO(user));
            } else {
                throw new IllegalArgumentException("User information was not saved, please try again");
            }
    }

    private Boolean authenticateInput(RegistrationDTO user){
        if (user.firstName() == null || user.firstName().isEmpty() ||
                user.lastName() == null || user.lastName().isEmpty() ||
                user.email() == null || user.email().isEmpty() ||
                user.phoneNumber() <= 0 ||
                user.bsnNumber() <= 0 ||
                user.password() == null || user.password().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(user.email()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String bsnNumberString = String.valueOf(user.bsnNumber());
        if (bsnNumberString.length() != 9 || !bsnNumberString.matches("\\d{9}")) {
            throw new IllegalArgumentException("Invalid BSN number");
        }

        return true;
    }

}
