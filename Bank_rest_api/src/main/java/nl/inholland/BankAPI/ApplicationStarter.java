package nl.inholland.BankAPI;

import jakarta.transaction.Transactional;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class ApplicationStarter implements ApplicationRunner {

    private UserService userService;

    public ApplicationStarter (UserService uService){
        this.userService = uService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        createUsers();
        userService.getAllUsers().forEach(user -> System.out.println(user.getEmail()));
    }

    private void createUsers(){
        User admin = new User("employee", "Doe","admin@email.com", "password", 123567890, 123456, List.of(UserType.ADMIN));
        User guest = new User("Jane", "Doe","guest@email.com", "password", 123567890, 143576, List.of(UserType.GUEST));
        User customer = new User("Jhon", "Doe","customer@email.com", "password", 123567890, 163558, List.of(UserType.CUSTOMER));

        userService.createUser(customer);
        userService.createUser(admin);
        userService.createUser(guest);

    }
}
