package com.bu.MoneyManage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Attachment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;
    private String fileKey;
    private String fileName;
    private String status;
    @ManyToOne
    @JoinColumn(name = "notes_id")
    private Notes note;
}
