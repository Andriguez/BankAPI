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
    //private long senderId;
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    @JsonBackReference
    private Account senderAccount;
    //private long receiverId;
    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    @JsonBackReference
    private Account receiverAccount;
    private double amount;
    private LocalDateTime dateTime;
    @OneToOne
    @JoinColumn(name = "initiator_id")
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

    public LocalDateTime getDateTime(){
        return dateTime;
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
}
