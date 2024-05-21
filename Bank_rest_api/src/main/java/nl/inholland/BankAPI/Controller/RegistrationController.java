package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private UserService uService;

    public RegistrationController(UserService userService){
        this.uService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsersByType(){

        List<UserType> usertype = List.of(UserType.ADMIN);

        return ResponseEntity.ok(uService.getUsersByType(usertype));
    }



}
