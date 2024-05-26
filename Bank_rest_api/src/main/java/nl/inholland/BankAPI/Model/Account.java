package nl.inholland.BankAPI.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name="accounts")
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private long id;
    private String iban;
    private double balance;
    private double absoluteLimit;
    private double dailyLimit;
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Transaction> sentTransactions;

   @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL)
   @JsonManagedReference
    private List<Transaction> receivedTransactions;

    public long getId() {return id;}
    //public long getUserId() {return userId;}

    public String getIban(){
        return iban;
    }
    public void setIban(String iban){
        this.iban = iban;
    }

    public double getBalance(){
        return balance;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public double getAbsoluteLimit(){
        return absoluteLimit;
    }
    public void setAbsoluteLimit(double absoluteLimit){
        this.absoluteLimit = absoluteLimit;
    }
    public double getDailyLimit(){
        return dailyLimit;
    }
    public void setDailyLimit(double dailyLimit){
        this.dailyLimit = dailyLimit;
    }

    public Account(String iban, double balance, double absoluteLimit, double dailyLimit, AccountType type){
        this.iban = iban;
        this.balance = balance;
        this.dailyLimit = dailyLimit;
        this.absoluteLimit = absoluteLimit;
        this.type = type;
    }

    public void setUser(User user) { this.user = user; }
}
