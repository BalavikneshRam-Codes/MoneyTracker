package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseVO extends BaseItemVO{
    private Long userId;
    private String redirectUrl;
}
