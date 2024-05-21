package nl.inholland.BankAPI;

import jakarta.transaction.Transactional;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountType;
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

        createAccounts();
        userService.getAllUsers().forEach(user -> System.out.println(user.getEmail()));
    }

    private void createUsers(Account current, Account saving){
        User admin = new User("employee", "Doe","admin@email.com", "password", 123567890, 123456, List.of(UserType.ADMIN));
        User guest = new User("Jane", "Doe","guest@email.com", "password", 123567890, 143576, List.of(UserType.GUEST));
        User customer = new User("John", "Doe","customer@email.com", "password", 123567890, 163558, List.of(UserType.CUSTOMER));

        userService.createUser(customer);
        userService.createUser(admin);
        userService.createUser(guest);

        customer.addAccountToUser(current,saving);

    }

    private void createAccounts(){
        Account current = new Account("NL12INHO0123456789", 1200.00, 100.00, 125.00, List.of(AccountType.CURRENT));
        Account savings = new Account("NL13INHO0123456789", 1500.00, 100.00, 125.00, List.of(AccountType.SAVINGS));
        Account account3 = new Account("NL14INHO0123456789", 750.00, 10.00, 125.00, List.of(AccountType.CURRENT));

        accountService.createAccount(current);
        accountService.createAccount(savings);
        accountService.createAccount(account3);

        createUsers(current,savings);
    }
}
