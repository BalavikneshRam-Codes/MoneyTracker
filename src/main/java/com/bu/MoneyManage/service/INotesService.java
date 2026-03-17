package com.bu.MoneyManage.service;

import com.bu.MoneyManage.vo.BaseVO;

public interface INotesService {
    BaseVO createOrUpdateNote(BaseVO baseVO) throws Exception;
    BaseVO fetchNotesInfo(BaseVO baseVO) throws Exception;
    BaseVO userAuthenticate(BaseVO baseVO) throws Exception;
    BaseVO fetchNotes(BaseVO baseVO) throws Exception;
}
