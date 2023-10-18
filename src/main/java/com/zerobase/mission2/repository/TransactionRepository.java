package com.zerobase.mission2.repository;

import com.zerobase.mission2.domain.Account;
import com.zerobase.mission2.domain.AccountUser;
import com.zerobase.mission2.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
}
