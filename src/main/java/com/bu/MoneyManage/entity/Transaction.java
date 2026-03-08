package com.bu.MoneyManage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Transaction extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private String transactionDisplayName;
    private String transactionKeyName;
    private String status;
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    private Boolean defaultTransaction;

}
