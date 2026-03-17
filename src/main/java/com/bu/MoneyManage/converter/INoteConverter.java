package com.bu.MoneyManage.converter;

import com.bu.MoneyManage.entity.Notes;
import com.bu.MoneyManage.vo.NoteReqVO;
import com.bu.MoneyManage.vo.NoteVO;

public interface INoteConverter {
    Notes convertVoToEntity(NoteReqVO noteVO,Notes notes);
    NoteVO convertEntityToVO(NoteVO noteVO,Notes notes);
}
