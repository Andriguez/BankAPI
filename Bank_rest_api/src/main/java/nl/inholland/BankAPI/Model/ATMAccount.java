package nl.inholland.BankAPI.Model;

import lombok.Data;

@Data
public class ATMAccount extends Account{
    private final long Id = 0000;
    private final String iban = "NLXXINHOXXXXXXXXXX";

    public ATMAccount() {
        super("NLXXINHOXXXXXXXXXX", 0.0, 0.0, 0.0, AccountType.ATM);
    }
}
