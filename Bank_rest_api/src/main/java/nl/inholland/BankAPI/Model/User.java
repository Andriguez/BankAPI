package nl.inholland.BankAPI.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@NoArgsConstructor
@Table(name="users")
@Data
@SequenceGenerator(name = "USER_SEQ", initialValue = 100001, allocationSize = 10)
public class User {

    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long phoneNumber;
    private long bsnNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Account> accounts;

    public void addAccount(Account account) {
        if(accounts == null){
            accounts = new ArrayList<>();
        }
        accounts.add(account);
        account.setUser(this);
    }


    @ElementCollection(fetch = EAGER)
    private List<UserType> userType;


    public User(String firstName, String lastName, String email, String password, long phoneNumber, long bsnNumber, List<UserType> userType){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.bsnNumber = bsnNumber;
        this.userType = userType;
    }
}
