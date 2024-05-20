package nl.inholland.BankAPI.Model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;


@Entity
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private long id;
    private String iban;
    private double balance;
    private double absoluteLimit;
    private double dailyLimit;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private String email;
    public long getId() {return id;}
    public String getEmail() {return email;}
    public void setEmail(String email){
        this.email = email;
    }

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

    public Account(String iban, double balance, double absoluteLimit, double dailyLimit){
        this.iban = iban;
        this.balance = balance;
        this.dailyLimit = dailyLimit;
        this.absoluteLimit = absoluteLimit;
    }

}
