package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccounts(String username) {
        List<Account> accounts;
        if(username != null && !username.isEmpty()) {
            accounts = accountRepository.findAccountsByUsername(username);
            System.out.println("found by username " + username );
            System.out.println(accounts.size());
        }
        else {
            accounts = accountRepository.findAll();
            System.out.println("found all" );
        }
        return accounts;
    }
}
