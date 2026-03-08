package com.bu.MoneyManage.converter.impl;

import com.bu.MoneyManage.converter.IAccountConverter;
import com.bu.MoneyManage.entity.Account;
import com.bu.MoneyManage.vo.AccountVO;
import org.springframework.stereotype.Component;

@Component
public class AccountConverterImpl implements IAccountConverter {

    @Override
    public AccountVO convertEntityToVo(Account account, AccountVO accountVO) {
        if(accountVO == null)
            accountVO = new AccountVO();
        if(account != null){
            accountVO.setAccountId(account.getAccountId());
            accountVO.setAccountName(account.getAccountName());
            if(account.getUser() != null && account.getAccountBalance() != null && account.getAccountBalance() > 0)
                accountVO.setAccountBalance(account.getAccountBalance());
        }
        return accountVO;
    }
}
