package com.bu.MoneyManage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotesVO extends BaseVO{
    private String noteDate;
    private List<NoteVO> noteVOS;
}
