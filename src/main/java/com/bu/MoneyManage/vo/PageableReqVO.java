package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageableReqVO extends BaseVO{
    private String searchText;
    private int start;
    private int length;
}
