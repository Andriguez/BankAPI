package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccounts(String username, String iban) {
        List<Account> accounts = accountRepository.findAll();
        if(username != null && !username.isEmpty()) {
            accounts = accounts.stream()
                    .filter(account -> username.equals(account.getUsername()))
                    .collect(Collectors.toList());
            System.out.println("found by username " + username );
            System.out.println(accounts.size());
        }
        if(iban != null && !iban.isEmpty()) {
            accounts = accounts.stream()
                    .filter(account -> iban.equals(account.getIban()))
                    .collect(Collectors.toList());
        }
        return accounts;
    }

    public boolean isUserAccount(String j_username, String toFromIban) {
        List<Account> accounts = getAccounts(j_username, toFromIban);
        if (accounts.size() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public Account getAccountByIban(String toFromIban) {
        List<Account> accounts = getAccounts(null, toFromIban);
        if(accounts.size() == 1) {
            return accounts.get(0);
        }
        else {
            return null;
        }
    }

    public void editDailyLimit(String accountIban, double newLimit){
        Account account = this.getAccountByIban(accountIban);
        account.setDailyLimit(newLimit);
    }

    public void editAbsoluteLimit(String accountIban, double newLimit){
        Account account = this.getAccountByIban(accountIban);
        account.setAbsoluteLimit(newLimit);
    }
}
