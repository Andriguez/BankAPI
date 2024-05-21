package nl.inholland.BankAPI.Repository;

import nl.inholland.BankAPI.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAccountsByUserId(long userId);
    List<Account> findAccountsByIban(String iban);

    List<Account> findByUserId(long userId);

}
