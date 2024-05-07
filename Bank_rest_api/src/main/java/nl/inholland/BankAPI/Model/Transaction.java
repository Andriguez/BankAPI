package nl.inholland.BankAPI.Model;

import java.time.LocalDateTime;

public class Transaction {

    private User sender;
    private User receiver;
    private double amount;
    private LocalDateTime dateTime;
    private UserType userInitiating;
}
