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
    /*public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public long getBsnNumber() {
        return bsnNumber;
    }

    public void setBsnNumber(long bsnNumber) {
        this.bsnNumber = bsnNumber;
        Why a BSN number for all users?
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }*/

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
