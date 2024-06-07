package nl.inholland.BankAPI.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private long id;
    private long senderId;
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    @JsonBackReference
    private Account senderAccount;
    private long receiverId;
    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    @JsonBackReference
    private Account receiverAccount;
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
    public Account getSenderAccount() {return senderAccount;}
    public void setSenderAccount(Account senderAccount) {this.senderAccount = senderAccount;}
    public Account getReceiverAccount() {return receiverAccount;}
    public void setReceiverAccount(Account receiverAccount) {this.receiverAccount = receiverAccount;}

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

    public TransactionType getTrancationType() {return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

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
