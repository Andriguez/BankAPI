package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) throws AuthenticationException {
        return userService.login(request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
