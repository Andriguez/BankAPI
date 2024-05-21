package nl.inholland.BankAPI.Repository;

import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.senderId = :senderId OR t.receiverId = :receiverId")
    List<Transaction> findBySenderIdOrReceiverId(@Param("senderId") long senderId,
                                                 @Param("receiverId") long receiverId);

}
