package com.collinz.mobilemoney.repositories;

import com.collinz.mobilemoney.models.Wallet;
import com.collinz.mobilemoney.models.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    @Query("SELECT SUM(t.amount) FROM WalletTransaction t WHERE t.sender = ?1 AND t.createdAt >= ?2 AND t.createdAt <= ?3")
    BigDecimal getTotalSentUsingRange(Wallet wallet, LocalDateTime from, LocalDateTime to);
}
