package nl.inholland.BankAPI.Service;

import nl.inholland.BankAPI.Model.Account;
import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import nl.inholland.BankAPI.Repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Security fix F1: customers could move money out of accounts they don't own.
class AccountServiceOwnershipTest {

    private AccountRepository accountRepository;
    private AccountService accountService;
    private User alice;
    private User bob;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);

        alice = userWith(1L, UserType.CUSTOMER);
        bob = userWith(2L, UserType.CUSTOMER);

        Account bobsAccount = new Account();
        bobsAccount.setIban("NL01INHO0000000002");
        bobsAccount.setUser(bob);
        when(accountRepository.findAccountsByIban("NL01INHO0000000002")).thenReturn(List.of(bobsAccount));
        when(accountRepository.findAccountsByIban("NL00INHO0000000000")).thenReturn(List.of());
    }

    private User userWith(long id, UserType type) {
        User user = new User();
        user.setId(id);
        user.setUserType(List.of(type));
        return user;
    }

    @Test
    void ownerIsRecognised() {
        assertTrue(accountService.isOwner(bob, "NL01INHO0000000002"));
    }

    @Test
    void customerDoesNotOwnSomeoneElsesAccount() {
        assertFalse(accountService.isOwner(alice, "NL01INHO0000000002"));
    }

    @Test
    void unknownIbanIsNotOwned() {
        assertFalse(accountService.isOwner(alice, "NL00INHO0000000000"));
    }

    @Test
    void adminMayActOnAnyAccount() {
        assertTrue(accountService.isOwner(userWith(3L, UserType.ADMIN), "NL01INHO0000000002"));
    }
}