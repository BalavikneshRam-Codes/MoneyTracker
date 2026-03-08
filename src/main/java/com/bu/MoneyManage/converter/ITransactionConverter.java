package com.bu.MoneyManage.converter;

import com.bu.MoneyManage.entity.Transaction;
import com.bu.MoneyManage.vo.TransactionVO;

public interface ITransactionConverter {
    TransactionVO convertEntityToVo(Transaction transaction,TransactionVO transactionVO);
}
