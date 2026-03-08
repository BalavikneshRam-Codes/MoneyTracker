package com.bu.MoneyManage.Repository;

import com.bu.MoneyManage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserIdAndStatus(Long userId,String userStatus);
    Optional<User> findByEmailAndStatus(String email,String userStatus);
}
