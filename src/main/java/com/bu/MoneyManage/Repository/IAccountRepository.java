package com.bu.MoneyManage.Repository;

import com.bu.MoneyManage.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account,Long> {
    Optional<List<Account>> findByUserUserIdAndDefaultAmountIsTrueAndStatus(Long userId, String status);
    Optional<List<Account>> findByDefaultAmountIsTrueAndStatus(String status);
}
