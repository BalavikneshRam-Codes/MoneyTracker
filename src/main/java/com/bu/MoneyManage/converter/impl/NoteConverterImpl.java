package com.bu.MoneyManage.converter.impl;

import com.bu.MoneyManage.Enum.StatusEnum;
import com.bu.MoneyManage.Repository.IAccountRepository;
import com.bu.MoneyManage.Repository.ICategoryRepository;
import com.bu.MoneyManage.Repository.ITransactionRepository;
import com.bu.MoneyManage.Repository.IUserRepository;
import com.bu.MoneyManage.converter.IAccountConverter;
import com.bu.MoneyManage.converter.ICategoryConverter;
import com.bu.MoneyManage.converter.INoteConverter;
import com.bu.MoneyManage.converter.ITransactionConverter;
import com.bu.MoneyManage.entity.Notes;
import com.bu.MoneyManage.utility.DataUtility;
import com.bu.MoneyManage.vo.NoteReqVO;
import com.bu.MoneyManage.vo.NoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

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
    @Autowired
    private ICategoryConverter categoryConverter;
    @Autowired
    private IAccountConverter accountConverter;
    @Autowired
    private ITransactionConverter transactionConverter;

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

    @Override
    public NoteVO convertEntityToVO(NoteVO noteVO, Notes notes) {
        if(noteVO == null) noteVO = new NoteVO();
        noteVO.setNotesId(notes.getNoteId());
        noteVO.setNotesMessage(notes.getNoteMessage());
        noteVO.setAmount(notes.getNoteAmount());
        if(notes.getRecordDateTime() != null) {
            noteVO.setRecordData(notes.getRecordDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
            noteVO.setRecordTime(notes.getRecordDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString());
        }
        if(notes.getCategory() != null)
            noteVO.setCategoryVO(categoryConverter.convertEntityToVO(notes.getCategory(),null));
        if(notes.getAccount() != null)
            noteVO.setAccountVO(accountConverter.convertEntityToVo(notes.getAccount(),null));
        if(notes.getTransaction() != null)
            noteVO.setTransactionVO(transactionConverter.convertEntityToVo(notes.getTransaction(),null));
        return noteVO;
    }
}
