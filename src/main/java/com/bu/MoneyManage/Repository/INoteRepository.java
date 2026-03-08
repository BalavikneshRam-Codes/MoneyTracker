package com.bu.MoneyManage.Repository;

import com.bu.MoneyManage.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INoteRepository extends JpaRepository<Notes,Long> {
}
