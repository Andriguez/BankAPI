package nl.inholland.BankAPI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.BankAPI.Model.*;
import nl.inholland.BankAPI.Model.DTO.NewAccountDTO;
import nl.inholland.BankAPI.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if (accounts.size() == 1) {
            return accounts.get(0);
        } else {
            return null;
        }
    }
    
    public List<Account> getAccountsByUserId(long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts;
    }

    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    public List<Account> updateAccounts(final User user,final Map<String, Object> requestData) {

        List<Account> accounts = user.getAccounts();
        Account currentAccount = null;
        Account savingsAccount = null;
    try {

        for (Account account : accounts) {
            if (account.getType() == AccountType.CURRENT) {
                currentAccount = account;
                currentAccount.setAbsoluteLimit(((Number) requestData.get("absolute1")).doubleValue());
                currentAccount.setDailyLimit(((Number) requestData.get("daily1")).doubleValue());
            } else if (account.getType() == AccountType.SAVINGS) {
                savingsAccount = account;
                savingsAccount.setAbsoluteLimit(((Number) requestData.get("absolute2")).doubleValue());
                savingsAccount.setDailyLimit(((Number) requestData.get("daily2")).doubleValue());
            }
        }

        // Save the updated accounts
        accountRepository.save(currentAccount);
        accountRepository.save(savingsAccount);
    } catch (Exception e){
        System.err.println("An error occurred: " + e.getMessage());
    }
        return accounts;
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

    public List<Account> closeUserAccounts(final User user) {

        List<Account> accounts = user.getAccounts();
        try {
            for (Account account : accounts) {
                account.setAccountStatus(AccountStatus.INACTIVE);
                accountRepository.save(account);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return accounts;
    }

    public Optional<Account> updateBalance(Account account, double balance) {
        return accountRepository.findById(account.getId()).map(a -> {
            a.setBalance(balance);
            return accountRepository.save(a);
        });
    }

    public Map<AccountType, NewAccountDTO> getNewAccountInfo(List<Account> accounts, Long userId) {

        //returns the accounts as a NewAccountDTO which contains only necessary information without transactions or balance
        Map<AccountType, NewAccountDTO> accountsMap = new HashMap<>();
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                NewAccountDTO dto = new NewAccountDTO(
                        userId,
                        account.getAbsoluteLimit(),
                        account.getDailyLimit(),
                        account.getType());

                accountsMap.put(account.getType(), dto);
            }
        } else {
            NewAccountDTO dto1 = new NewAccountDTO(userId, 0, 0, AccountType.CURRENT);
            NewAccountDTO dto2 = new NewAccountDTO(userId, 0, 0, AccountType.SAVINGS);
            accountsMap.putAll(Map.of(dto1.type(), dto1, dto2.type(), dto2));
        }

        return accountsMap;
    }

    public List<Account> createAccounts(List<NewAccountDTO> accountsData) {

        List<Account> createdAccounts = new ArrayList<>();
        accountsData.forEach(
                accountDTO -> {

                    Account account = new Account(
                            generateIBAN(),
                            0,
                            accountDTO.absolute().doubleValue(),
                            accountDTO.daily().doubleValue(),
                            accountDTO.type());

                    createdAccounts.add(accountRepository.save(account));
                }
        );

        return createdAccounts;
    }

    public Boolean hasAccess(User initiator, List<String> accounts) throws EntityNotFoundException {
        // Admins always have access
        if (initiator.getUserType().contains(UserType.ADMIN)) {
            return true;
        }

        // Check if the user has access to any of the provided accounts
        if (initiator.getUserType().contains(UserType.CUSTOMER)) {
            return accounts.stream().anyMatch(iban -> {
                try {
                    Account account = getAccountByIban(iban);
                    return initiator.getId() == account.getUser().getId();

                } catch (Exception e) {
                    return false;
                }
            });
        }

        // If none of the accounts are accessible, return false
        return false;
    }
}

