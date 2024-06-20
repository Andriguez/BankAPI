package nl.inholland.BankAPI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.DTO.*;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Repository.UserRepository;
import nl.inholland.BankAPI.Security.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AccountService accountService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider, AccountService accountService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
        this.accountService = accountService;
    }

    public User createUser(User user) {
        if (existingEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        } else if (existingBSN(user.getBsnNumber())) {
            throw new IllegalArgumentException("BSN number is already on our database");
        }


        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public Boolean existingEmail(String email) {
        User existingUser = userRepository.findUserByEmail(email);

        if (existingUser != null) {
            return true;
        }

        return false;
    }

    public Boolean existingBSN(Long bsn) {
        User existingUser = userRepository.findUserByBsnNumber(bsn);

        if (existingUser != null) {
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


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByType(List<UserType> userType) {
        return userRepository.findByUserTypeIn(userType);
    }

    public void changeGuestToUser(User guest) {

        if (guest.getUserType().contains(UserType.GUEST)) {
            List<UserType> newUserTypes = new ArrayList<>(guest.getUserType());
            newUserTypes.remove(UserType.GUEST);
            newUserTypes.add(UserType.CUSTOMER);
            guest.setUserType(newUserTypes);
            userRepository.save(guest);
        }

    }

    public User getUserById(long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
        User user = getUserByEmail(loginRequest.email());

        if (user == null) {
            throw new AuthenticationException("No user found with this email");
        }

        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthenticationException("password is incorrect");
        }

        String usertype = user.getUserType().toString();
        Long userId = user.getId();
        return new LoginResponseDTO(userId, user.getFirstName(), user.getLastName(), usertype, jwtProvider.createToken(user.getEmail(), user.getUserType()));
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void AddAccountToUser(User user, Account account) {
        user.addAccount(account);
        userRepository.save(user);
    }

    public UserDTO getUserDTO(User requestedUser) {
        return new UserDTO(
                requestedUser.getId(),
                requestedUser.getFirstName(),
                requestedUser.getLastName(),
                requestedUser.getEmail(),
                requestedUser.getPhoneNumber(),
                requestedUser.getBsnNumber(),
                accountService.getNewAccountInfo(requestedUser.getAccounts(), requestedUser.getId()));
    }

    public List<UserOverviewDTO> getUsersOverview(String userType) throws EntityNotFoundException {

        UserType type;
        try {
            type = UserType.valueOf(userType);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Incorrect Usertype submitted");
        }

        List<User> users = getUsersByType(List.of(type));

        if (users == null || users.isEmpty()) {
            throw new EntityNotFoundException("No users found for the given user type");
        }

        return users.stream()
                .map(user -> new UserOverviewDTO(user.getId(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }


}
