package com.bu.MoneyManage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private String accountName;
    private Long accountBalance;
    private String status;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    private Boolean defaultAmount;
}
