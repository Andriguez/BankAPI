package nl.inholland.BankAPI.Controller;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.BankAPI.Model.DTO.UserDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(params = "type")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserOverviewDTO>> getUsersByType(String type) throws EntityNotFoundException {
        List<UserOverviewDTO> users = userService.getUsersOverview(type.toUpperCase());
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping(params = "id")
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long id) throws AuthorizationServiceException, EntityNotFoundException {

        User loggedUser = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!hasAccess(loggedUser, id)) {
            throw new AuthorizationServiceException("user has no access to this data!");
        }

        User requestedUser = (id != 0) ? userService.getUserById(id) : loggedUser;

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDTO(requestedUser));
    }

    private Boolean hasAccess(User user, Long requestedId) {
        if (user.getUserType().contains(UserType.ADMIN)) {
            return true;
        }

        if (requestedId != null && user.getId() == requestedId) {
            return true;
        }

        return false;
    }

    //<!--deployment, functional testing, cleaning up code--> 

}
