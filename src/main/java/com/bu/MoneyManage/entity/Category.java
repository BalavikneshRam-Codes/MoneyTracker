package com.bu.MoneyManage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;
    private String imageKey;
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    private Boolean defaultCategory;
}
