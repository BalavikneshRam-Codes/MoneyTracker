package com.bu.MoneyManage.vo;

import lombok.Data;

@Data
public class NoteVO {
    private Long notesId;
    private String notesMessage;
    private String recordTime;
    private String recordData;
    private CategoryVO categoryVO;
    private AccountVO accountVO;
    private Double amount;
    private TransactionVO transactionVO;
}
