package nl.inholland.BankAPI.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private long id;
    private long senderId;
    private long receiverId;
    private double amount;
    private LocalDateTime dateTime;
    private int userInitiating;

    private TransactionType transactionType;

    public long getSenderId(){
        return senderId;
    }
    public void setSenderId(int senderId){
        this.senderId = senderId;
    }

    public long getReceiverId(){
        return receiverId;
    }
    public void setReceiverId(int receiverId){
        this.receiverId = receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime(){
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getUserInitiating() {
        return userInitiating;
    }

    public void setUserInitiating(int userInitiating) {
        this.userInitiating = userInitiating;
    }

    public Transaction(int senderId, int receiverId, double amount, LocalDateTime dateTime, int userInitiating ){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.userInitiating = userInitiating;

    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }
}
