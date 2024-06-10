package nl.inholland.BankAPI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.DTO.RegistrationDTO;
import nl.inholland.BankAPI.Model.DTO.UserDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Repository.UserRepository;
import nl.inholland.BankAPI.Security.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public User createUser(User user) {
        if (existingEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        } else if (existingBSN(user.getBsnNumber())){
            throw new IllegalArgumentException("BSN number is already on our database");
        }


        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public Boolean existingEmail(String email){
        User existingUser = userRepository.findUserByEmail(email);

        if(existingUser != null) {
            return true;
        }

        return false;
    }

    public Boolean existingBSN(Long bsn){
        User existingUser = userRepository.findUserByBsnNumber(bsn);

        if(existingUser != null) {
            return true;
        }

        return false;
    }
    public User createUserDTO(RegistrationDTO user) {
        User existingUser = userRepository.findUserByEmail(user.email());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already taken");
        }
        User userToAdd = new User();
        userToAdd.setFirstName(user.firstName());
        userToAdd.setLastName(user.lastName());
        userToAdd.setEmail(user.email());
        userToAdd.setPhoneNumber(user.phoneNumber());
        userToAdd.setBsnNumber(user.bsnNumber());
        userToAdd.setUserType(List.of(UserType.GUEST));
        userToAdd.setPassword(bCryptPasswordEncoder.encode(user.password()));

        return userRepository.save(userToAdd);

    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getUsersByType(List<UserType> userType){
        return userRepository.findByUserTypeIn(userType);
    }
    public void changeGuestToUser(User guest){
        if (guest.getUserType().contains(UserType.GUEST)) {
            List<UserType> newUserTypes = new ArrayList<>(guest.getUserType());
            newUserTypes.remove(UserType.GUEST);
            newUserTypes.add(UserType.CUSTOMER);
            guest.setUserType(newUserTypes);
            userRepository.save(guest);
        }
    }

    public User getUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

     public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
        User user = userRepository.findUserByEmail(loginRequest.email());
        if (user != null && bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            String fullName = user.getFirstName() + ' '+ user.getLastName();
            String usertype = user.getUserType().toString();
            Long userId = user.getId();
            return new LoginResponseDTO(userId, fullName, usertype, jwtProvider.createToken(user.getEmail(), user.getUserType()));
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    public void deleteUser(long id){
        User user = this.getUserById(id);
        //instead of deleting user, we just close their accounts
        //user.getCheckingAccount().getIban();
        //user.getSavingAccount().getIban();
    }

    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public void AddAccountToUser(User user, Account account){
        user.addAccount(account);
        userRepository.save(user);
    }
    public User findUserByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findUserByFirstNameAndLastName(firstName, lastName);
    }
}
