package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountVO extends BaseVO{
    private Long accountId;
    private String accountName;
    private Long accountBalance;
}
