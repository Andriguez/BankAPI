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
    @ManyToOne
    @JoinColumn(name = "sender_account_id", nullable = true)
    @JsonBackReference(value = "sentTransactions")
    private Account senderAccount;
    @ManyToOne
    @JoinColumn(name = "receiver_account_id", nullable = true)
    @JsonBackReference(value = "receivedTransactions")

    private Account receiverAccount;
    private double amount;
    private LocalDateTime dateTime;
    @OneToOne
    @JoinColumn(name = "initiator_id", unique = false)
    @JsonBackReference
    private User userInitiating;
    private TransactionType transactionType;

    //public long getSenderId(){
       // return senderId;
    //}

    public Account getSenderAccount() {return senderAccount;}
    public Account getReceiverAccount() {return receiverAccount;}

    //public long getReceiverId(){
      //  return receiverId;
    //}


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
    public User getUserInitiating() {
        return userInitiating;
    }

    public Transaction(Account senderAccount, Account receiverAccount, double amount, LocalDateTime dateTime, User userInitiating, TransactionType type ){
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
        this.dateTime = dateTime;
        this.userInitiating = userInitiating;
        this.transactionType = type;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public String getSenderAccountIban() {
        if(this.senderAccount == null) {
            return "";
        }
        else {
            return this.senderAccount.getIban();
        }
    }
    public String getReceiverAccountIban() {
        if(this.receiverAccount == null) {
            return "";
        }
        else {
            return this.receiverAccount.getIban();
        }
    }
}
