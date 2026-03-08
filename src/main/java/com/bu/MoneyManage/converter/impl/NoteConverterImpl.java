package com.bu.MoneyManage.converter.impl;

import com.bu.MoneyManage.Enum.StatusEnum;
import com.bu.MoneyManage.Repository.IAccountRepository;
import com.bu.MoneyManage.Repository.ICategoryRepository;
import com.bu.MoneyManage.Repository.ITransactionRepository;
import com.bu.MoneyManage.Repository.IUserRepository;
import com.bu.MoneyManage.converter.INoteConverter;
import com.bu.MoneyManage.entity.Notes;
import com.bu.MoneyManage.utility.DataUtility;
import com.bu.MoneyManage.vo.NoteReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoteConverterImpl implements INoteConverter {
    @Autowired
    private DataUtility dataUtility;

    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public Notes convertVoToEntity(NoteReqVO noteReqVO, Notes notes) {
        if (notes == null)
            notes = new Notes();
        notes.setNoteMessage(noteReqVO.getNoteMessage());
        notes.setRecordDateTime(dataUtility.convertStringToDate(noteReqVO.getRecordDate(), noteReqVO.getRecordTime(), DataUtility.noteDateRequestFormat));
        if (noteReqVO.getTransactionName() != null || notes.getTransaction() != null)
            notes.setTransaction(transactionRepository.findFirstByTransactionKeyNameAndStatus(noteReqVO.getTransactionName(), StatusEnum.ACTIVE.getStatus()).orElse(null));
        if ((noteReqVO.getCategoryId() != null && noteReqVO.getCategoryId() > 0) || notes.getCategory() != null)
            if (noteReqVO.getCategoryId() != null && noteReqVO.getCategoryId() > 0)
                notes.setCategory(categoryRepository.getReferenceById(noteReqVO.getCategoryId()));
        if (noteReqVO.getAccountId() != null && noteReqVO.getAccountId() > 0)
            notes.setAccount(accountRepository.getReferenceById(noteReqVO.getAccountId()));
        notes.setNoteAmount(noteReqVO.getAmount());
        notes.setStatus(StatusEnum.ACTIVE.getStatus());
        if (noteReqVO.getUserId() != null && noteReqVO.getUserId() > 0)
            notes.setUser(userRepository.findByUserIdAndStatus(noteReqVO.getUserId(),StatusEnum.ACTIVE.getStatus()).orElse(null));
        return notes;
    }
}
