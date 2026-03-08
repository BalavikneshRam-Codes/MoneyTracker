package com.bu.MoneyManage.Enum;

public enum StatusEnum {
    SUCCESS("SUCCESS"),FAILED("FAILED"),ACTIVE("ACTIVE");
    private String status;

    StatusEnum(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
}
