package nl.inholland.BankAPI.Model.DTO;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.AccountStatus;
import nl.inholland.BankAPI.Model.AccountType;

// public record AccountDTO(Long id, String iban, AccountType type, double balance, double absolute, double daily,
// List<Transaction> receivedTransactions, List<Transaction> sentTransactions, AccountStatus status) {
public class AccountDTO {
    private Long id;
    private String iban;
    private AccountType type;
    private double balance;
    private double absolute;
    private double daily;
    private AccountStatus status;
    public AccountDTO(Long id, String iban, AccountType type, double balance, double absolute, double daily,
    AccountStatus status) {
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.balance = balance;
        this.absolute = absolute;
        this.daily = daily;
        this.status = status;
    }
    public AccountDTO(Account account){
        if (account != null) {
            this.id = account.getId();
            this.iban = account.getIban();
            this.type = account.getType();
            this.balance = account.getBalance();
            this.absolute = account.getAbsoluteLimit();
            this.daily = account.getDailyLimit();
            this.status = account.getAccountStatus();
        }
    }
    public Long getId() { return this.id; }
    public String getIban() { return this.iban; }
    public AccountType getType() {
        return this.type;
    }
    public double getBalance() { return this.balance; }
    public double getAbsolute() { return this.absolute; }
    public double getDaily() { return this.daily; }
    public AccountStatus getStatus() { return  this.status; }
}
