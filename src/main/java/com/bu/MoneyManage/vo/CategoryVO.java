package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryVO extends BaseVO{
    private Long categoryId;
    private String categoryName;
    private String imageKey;
}
