package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotesResponseVO extends BaseItemVO{
    private List<AccountVO> accountVOS;
    private List<TransactionVO> transactionVOS;
    private List<CategoryVO> categoryVOS;
    private List<UserNotesVO> userNotesVOS;
}
