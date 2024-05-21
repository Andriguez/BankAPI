package nl.inholland.BankAPI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Repository.UserRepository;
import nl.inholland.BankAPI.Security.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
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
        User existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already taken");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getUsersByType(List<UserType> userType){
        return userRepository.findByUserTypeIn(userType);
    }

    public User getUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

     public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
        User user = userRepository.findUserByEmail(loginRequest.email());
        if (user != null && bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            String fullName = user.getFirstName() + ' '+ user.getLastName();
            String usertype = user.getUserType().toString();
            return new LoginResponseDTO(fullName, usertype, jwtProvider.createToken(user.getEmail(), user.getUserType()));
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    public void deleteUser(long id){
        User user = this.getUserById(id);
        userRepository.deleteById(id);
    }


}
