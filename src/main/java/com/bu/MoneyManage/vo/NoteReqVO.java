package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoteReqVO extends BaseVO{
    private Long noteId;
    private String transactionName;
    private Long accountId;
    private Long categoryId;
    private String noteMessage;
    private Double amount;
    private String recordDate;
    private String recordTime;
    private Long userId;
    private String fromDate;
    private String toDate;
    private PageableReqVO pageableReqVO;
}
