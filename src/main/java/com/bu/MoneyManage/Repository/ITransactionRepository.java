package com.bu.MoneyManage.Repository;

import com.bu.MoneyManage.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findFirstByTransactionKeyNameAndStatus(String transaction,String status);
    Optional<List<Transaction>> findByUserUserIdAndDefaultTransactionIsTrueAndStatus(Long userId,String status);
    Optional<List<Transaction>> findByDefaultTransactionIsTrueAndStatus(String status);
}
