package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserReqVO extends BaseVO{
    private String username;
    private String passkey;
}
