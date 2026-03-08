package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionVO extends BaseVO{
    private Long transactionId;
    private String transactionDisplayName;
    private String transactionKeyName;
}
