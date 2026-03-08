package com.bu.MoneyManage.converter.impl;

import com.bu.MoneyManage.converter.ITransactionConverter;
import com.bu.MoneyManage.entity.Transaction;
import com.bu.MoneyManage.vo.TransactionVO;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverterImpl implements ITransactionConverter {
    @Override
    public TransactionVO convertEntityToVo(Transaction transaction, TransactionVO transactionVO) {
        if(transactionVO == null)
            transactionVO = new TransactionVO();
        if(transaction != null){
            transactionVO.setTransactionKeyName(transaction.getTransactionKeyName());
            transactionVO.setTransactionDisplayName(transaction.getTransactionDisplayName());
        }
        return transactionVO;
    }
}
