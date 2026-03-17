package com.bu.MoneyManage.Repository;

import com.bu.MoneyManage.entity.Notes;
import com.bu.MoneyManage.entity.bean.NotesFilterBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INoteCustomRepository {
    Page<Notes> findNotesByFilter(NotesFilterBean filter, Pageable pageable);
}
