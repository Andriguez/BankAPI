package nl.inholland.BankAPI;

import jakarta.transaction.Transactional;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.AccountService;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class ApplicationStarter implements ApplicationRunner {

    private UserService userService;
    private AccountService accountService;

    public ApplicationStarter (UserService uService, AccountService accountService){
        this.userService = uService;
        this.accountService = accountService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // createAccounts();


    }

    private void createUsers(Account current, Account savings){
        User admin = new User("employee", "Doe","admin@email.com", "password", 123567890, 123456, List.of(UserType.ADMIN));
        User guest = new User("Jane", "Doe","guest@email.com", "password", 123567890, 143576, List.of(UserType.GUEST));
        User customer2 = new User("Johny", "Doey","customer2@email.com", "password", 123567890, 163598, List.of(UserType.CUSTOMER));

        //userService.createUser(customer2);
        //userService.createUser(admin);
        //userService.createUser(guest);

        customer2.addAccount(current);
        customer2.addAccount(savings);


    }

    private void createAccounts(){
        //Account current = new Account("NL12INHO0123456789", 1200.00, 100.00, 125.00, AccountType.CURRENT);
        //Account savings = new Account("NL13INHO0123456789", 1500.00, 100.00, 125.00, AccountType.SAVINGS);

        //accountService.createAccount(current);
        //accountService.createAccount(savings);

        //createUsers(current,savings);
    }

}
