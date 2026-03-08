package com.bu.MoneyManage.service;

import com.bu.MoneyManage.vo.BaseVO;

public interface INotesService {
    BaseVO createOrUpdateNote(BaseVO baseVO);
    BaseVO fetchNotesInfo(BaseVO baseVO);
}
