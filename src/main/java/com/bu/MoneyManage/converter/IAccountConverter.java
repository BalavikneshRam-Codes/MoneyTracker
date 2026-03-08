package com.bu.MoneyManage.converter;

import com.bu.MoneyManage.entity.Account;
import com.bu.MoneyManage.vo.AccountVO;

public interface IAccountConverter {
    AccountVO convertEntityToVo(Account account,AccountVO accountVO);
}
