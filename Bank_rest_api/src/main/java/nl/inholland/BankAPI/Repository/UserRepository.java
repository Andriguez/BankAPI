package nl.inholland.BankAPI.Repository;

import nl.inholland.BankAPI.Model.User;
import nl.inholland.BankAPI.Model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    User findUserByBsnNumber(Long Bsn);

    List<User> findByUserTypeIn(List<UserType> userType);

    Boolean existsByEmail(String email);
}
