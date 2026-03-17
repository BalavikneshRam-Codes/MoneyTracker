package com.bu.MoneyManage.entity.bean;

import lombok.Data;

import java.util.Date;

@Data
public class NotesFilterBean {
    private Date fromDate;
    private Date toDate;
    private Long userId;
    private String status;
}
