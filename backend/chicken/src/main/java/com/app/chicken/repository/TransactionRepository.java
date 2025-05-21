package com.app.chicken.repository;

import com.app.chicken.model.Transaction;
import com.app.chicken.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    List<Transaction> findByUser(User user);
    
    List<Transaction> findByUserAndDatetimeBetween(User user, LocalDateTime start, LocalDateTime end);
    
    boolean existsByTransactionIdentifier(String transactionIdentifier);
    
    @Query("SELECT t.category as category, SUM(t.amount) as total FROM Transaction t " +
           "WHERE t.user = ?1 AND t.amount < 0 GROUP BY t.category")
    List<Map<String, Object>> findTotalSpendingByCategory(User user);
    
    @Query("SELECT SUM(ABS(t.amount)) FROM Transaction t " +
           "WHERE t.user = ?1 AND t.amount < 0 AND t.datetime BETWEEN ?2 AND ?3")
    Double findTotalSpentBetweenDates(User user, LocalDateTime start, LocalDateTime end);
} 