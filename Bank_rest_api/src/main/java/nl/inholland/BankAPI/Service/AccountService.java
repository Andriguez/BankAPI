package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountStatus;
import nl.inholland.BankAPI.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // getAccountByIban filter account matching the given iban. If the iban does not exist, it will return null. If
    // the iban is correct, only one account should be returned, so we return the only element if there is one
    // account.
    public Account getAccountByIban(String iban) {
        List<Account> accounts = accountRepository.findAccountsByIban(iban);
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

    public List<Account> getAccountsByUserId(long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts;
    }

    public void createAccount(Account account){
        accountRepository.save(account);
    }
    public void updateAccount(Account account){
            Account accountToChange = this.getAccountByIban(account.getIban());

            accountToChange.setAbsoluteLimit(account.getAbsoluteLimit());
            accountToChange.setDailyLimit(account.getDailyLimit());

        accountRepository.save(accountToChange);
    }
    public String generateIBAN() {
        String countryCode = "NL";
        String bankCode = "INHO0";
        // Generate the check digits (xx) between 00 and 99
        Random random = new Random();
        int checkDigits = random.nextInt(100);
        // Generate the account number (9 digits)
        long accountNumber = (long) (random.nextDouble() * 1_000_000_000L);
        // Format the check digits and account number
        String checkDigitsFormatted = String.format("%02d", checkDigits);
        String accountNumberFormatted = String.format("%09d", accountNumber);
        // Combine all parts to create the IBAN
        return countryCode + checkDigitsFormatted + bankCode + accountNumberFormatted;
    }

    public void closeAccount(Account account){
        Account accountToClose = this.getAccountByIban(account.getIban());
        accountToClose.setAccountStatus(AccountStatus.valueOf("INACTIVE"));
    }
}

