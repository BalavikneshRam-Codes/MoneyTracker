package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends BaseVO{
    private String userName;
    private String passkey;
}
