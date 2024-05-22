package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
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

    // filter accounts based on given filters. Some of the filters can be null. In that case, they are not applied.
    //public List<Account> getAccounts(String email, String iban) {
        // we first read all accounts and then filter the ones we don't want.
      //  List<Account> accounts = accountRepository.findAll();
        // if email is provided in input, we should use it for filtering.
        //if(email != null && !email.isEmpty()) {
          //  accounts = accounts.stream()
                    // filter method only keeps accounts that meet the following criteria. Here the criteria is the
                    // input email is equal to the account's email.
            //        .filter(account -> email.equals(account.getEmail()))
              //      .collect(Collectors.toList());
            //System.out.println("found by email " + email );
            //System.out.println(accounts.size());
        //}
        //if(iban != null && !iban.isEmpty()) {
          //  accounts = accounts.stream()
                    // if iban is provided, filter accounts that have the same iban.
            //        .filter(account -> iban.equals(account.getIban()))
              //      .collect(Collectors.toList());
        //}
        //return accounts;
    //}

    //public boolean isUserAccount(String j_email, String toFromIban) {
      //  List<Account> accounts = getAccounts(j_email, toFromIban);
        //if (accounts.size() == 1) {
          //  return true;
        //}
        //else {
          //  return false;
        //}
    //}

    // getAccountByIban filter account matching the given iban. If the iban does not exist, it will return null. If
    // the iban is correct, only one account should be returned, so we return the only element if there is one
    // account.
    public Account getAccountByIban(String toFromIban) {
        List<Account> accounts = accountRepository.findAccountsByIban(toFromIban);
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

}

