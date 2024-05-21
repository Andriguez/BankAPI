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

    @GetMapping("users/{type}")
    public ResponseEntity<List<User>> getUsersByType(@RequestParam(required = false) String type){

        List<UserType> usertype;

        if(type.equals("admin")){
            usertype = List.of(UserType.ADMIN);
        } else if (type.equals("customer")){
            usertype = List.of(UserType.CUSTOMER);
        } else if (type.equals("guest")){
            usertype = List.of(UserType.ADMIN);
        } else {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(userService.getUsersByType(usertype));
    }

}
