package com.bu.MoneyManage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Notes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
    @ManyToOne
    @JoinColumn(name = "amount_id")
    private Account account;
    private String noteMessage;
    @OneToMany(mappedBy = "note", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Attachment> attachments;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Double noteAmount;
    private String status;
    private Date recordDateTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
