package com.bu.MoneyManage.Repository;

import com.bu.MoneyManage.entity.Category;
import com.bu.MoneyManage.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category,Long> {
    Optional<List<Category>> findByUserUserIdAndDefaultCategoryIsTrue(Long userId);
    Optional<List<Category>> findByDefaultCategoryIsTrue();
}
