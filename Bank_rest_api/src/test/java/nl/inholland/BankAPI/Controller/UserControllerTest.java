package nl.inholland.BankAPI.Controller;

import nl.inholland.BankAPI.Model.DTO.UserDTO;
import nl.inholland.BankAPI.Model.DTO.UserOverviewDTO;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Security.JwtProvider;
import nl.inholland.BankAPI.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserController userController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetALlUsersNoAuthenticationFails() throws Exception{
        mockMvc.perform(get("/users")).andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "admin@email.com", authorities = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setUserType(List.of(UserType.CUSTOMER));

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setUserType(List.of(UserType.CUSTOMER));

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetUsersByType() throws Exception {
        UserOverviewDTO userOverview1 = new UserOverviewDTO(1L, "Mock One", "User");
        UserOverviewDTO userOverview2 = new UserOverviewDTO(2L, "Mock Two", "User");

        List<UserOverviewDTO> usersOverview = Arrays.asList(userOverview1, userOverview2);

        when(userService.getUsersOverview("CUSTOMER")).thenReturn(usersOverview);

        mockMvc.perform(get("/users").param("type", "guest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Mock One"))
                .andExpect(jsonPath("$[1].firstName").value("Mock Two"));
    }

    @Test
    @WithMockUser(username = "customer2@email.com")
    public void testGetUserByIdWithAccess() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("customer2@email.com");
        mockUser.setUserType(List.of(UserType.CUSTOMER));

        when(userService.getUserByEmail("customer2@email.com")).thenReturn(mockUser);
        when(userService.getUserById(1L)).thenReturn(mockUser);
        when(userService.getUserDTO(mockUser)).thenReturn(new UserDTO(mockUser.getId(), mockUser.getFirstName(), mockUser.getLastName(), mockUser.getEmail(), mockUser.getPhoneNumber(), mockUser.getBsnNumber(), new HashMap<>()));

        mockMvc.perform(get("/users").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("customer2@email.com"));
    }

    @Test
    @WithMockUser(username = "customer@email.com")
    public void testGetUserByIdWithoutAccess() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("customer@email.com");
        mockUser.setUserType(List.of(UserType.GUEST));

        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setEmail("customer2@email.com");
        anotherUser.setUserType(List.of(UserType.CUSTOMER));

        when(userService.getUserByEmail("customer@email.com")).thenReturn(mockUser);
        when(userService.getUserById(2L)).thenReturn(anotherUser);

        mockMvc.perform(get("/users").param("id", "2"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("user has no access to this data!"));
    }

    @Test
    @WithMockUser(username = "admin@email.com", authorities = {"ADMIN"})
    public void testGetUserByIdAsAdmin() throws Exception {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("admin@email.com");
        adminUser.setUserType(List.of(UserType.ADMIN));

        User regularUser = new User();
        regularUser.setId(2L);
        regularUser.setEmail("customer@email.com");
        regularUser.setUserType(List.of(UserType.CUSTOMER));

        when(userService.getUserByEmail("admin@email.com")).thenReturn(adminUser);
        when(userService.getUserById(2L)).thenReturn(regularUser);
        when(userService.getUserDTO(regularUser)).thenReturn(new UserDTO(regularUser.getId(), regularUser.getFirstName(), regularUser.getLastName(), regularUser.getEmail(), regularUser.getPhoneNumber(), regularUser.getBsnNumber(), new HashMap<>()));

        mockMvc.perform(get("/users").param("id", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("customer@email.com"));
    }



}