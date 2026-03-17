package com.bu.MoneyManage.service.impl;

import com.bu.MoneyManage.Enum.StatusEnum;
import com.bu.MoneyManage.Repository.*;
import com.bu.MoneyManage.converter.IAccountConverter;
import com.bu.MoneyManage.converter.ICategoryConverter;
import com.bu.MoneyManage.converter.INoteConverter;
import com.bu.MoneyManage.converter.ITransactionConverter;
import com.bu.MoneyManage.entity.*;
import com.bu.MoneyManage.entity.bean.NotesFilterBean;
import com.bu.MoneyManage.service.INotesService;
import com.bu.MoneyManage.utility.DataUtility;
import com.bu.MoneyManage.vo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotesServiceImpl implements INotesService {
    @Autowired
    private INoteConverter noteConverter;
    @Autowired
    private INoteRepository noteRepository;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IAccountConverter accountConverter;
    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private ITransactionConverter transactionConverter;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private ICategoryConverter categoryConverter;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private INoteCustomRepository noteCustomRepository;
    @Autowired
    private DataUtility dataUtility;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BaseVO createOrUpdateNote(BaseVO baseVO) throws Exception {
        NotesResponseVO notesResponseVO = null;
        try {
            if (baseVO instanceof NoteReqVO noteReqVO) {
                if (noteReqVO.getNoteId() != null && noteReqVO.getNoteId() > 0) {

                } else {
                    Notes notes = noteConverter.convertVoToEntity(noteReqVO, null);
                    noteRepository.save(notes);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return notesResponseVO;
    }

    @Override
    public BaseVO fetchNotesInfo(BaseVO baseVO) throws Exception {
        NotesResponseVO notesResponseVO = null;
        try {
            if (baseVO instanceof NoteReqVO noteReqVO) {
                notesResponseVO = new NotesResponseVO();
                buildAccount(noteReqVO, notesResponseVO);
                buildTransaction(noteReqVO, notesResponseVO);
                buildCategory(noteReqVO, notesResponseVO);
            }
        } catch (Exception e) {
            throw e;
        }
        return notesResponseVO;
    }

    @Override
    public BaseVO userAuthenticate(BaseVO baseVO) throws Exception {
        UserResponseVO userResponseVO = null;
        try {
            if (baseVO instanceof UserReqVO userReqVO) {
                if (userReqVO.getUsername() != null) {
                    userResponseVO = new UserResponseVO();
                    Optional<User> user = userRepository.findByEmailAndStatus(userReqVO.getUsername(), StatusEnum.ACTIVE.getStatus());
                    if (user.isPresent()) {
                        User user_ = user.get();
                        if (!user_.getPassword().equals(userReqVO.getPasskey()))
                            throw new Exception("Incorrect Password!!");
                        userResponseVO.setUserId(user_.getUserId());
                        userResponseVO.setRedirectUrl("/notes");
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return userResponseVO;
    }

    @Override
    public BaseVO fetchNotes(BaseVO baseVO) throws Exception {
        NotesResponseVO notesResponseVO = null;
        try {
            if (baseVO instanceof NoteReqVO noteReqVO) {
                if(noteReqVO.getUserId() != null && noteReqVO.getUserId() > 0){
                    Optional<User> user = userRepository.findByUserIdAndStatus(noteReqVO.getUserId(),StatusEnum.ACTIVE.getStatus());
                    if(user.isPresent()){
                        notesResponseVO = new NotesResponseVO();
                        NotesFilterBean notesFilterBean = getNotesFilterBean(noteReqVO);
                        if(notesFilterBean != null){
                            Page<Notes> notes = noteCustomRepository.findNotesByFilter(notesFilterBean,getNotesPageable(noteReqVO.getPageableReqVO()));
                            if(!notes.isEmpty()){
                                Map<LocalDate,List<Notes>> notesDate = notes.get().collect(Collectors.groupingBy(notes_ ->
                                    notes_.getRecordDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                                            ()-> new TreeMap<LocalDate,List<Notes>>(Comparator.reverseOrder()),
                                    Collectors.toList()
                                ));
                                List<UserNotesVO> userNotesVOS = new ArrayList<>();
                                for(Map.Entry<LocalDate,List<Notes>> notesDateGroup : notesDate.entrySet()){
                                   String something =  dataUtility.getFormatedDateFromStringDate(notesDateGroup.getKey(),DataUtility.noteLocalDateFormat,DataUtility.notesListDateFormat);
                                    UserNotesVO userNotesVO = new UserNotesVO();
                                    userNotesVO.setNoteDate(something);
                                    List<NoteVO> noteVOS = new ArrayList<>();
                                    notesDateGroup.getValue().forEach(notes_ -> noteVOS.add(noteConverter.convertEntityToVO(null,notes_)));
                                    userNotesVO.setNoteVOS(noteVOS);
                                    userNotesVOS.add(userNotesVO);
                                }
                                notesResponseVO.setUserNotesVOS(userNotesVOS);
                            }
                        }
                    }else throw new Exception("User not found!");
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return notesResponseVO;
    }
    public NotesFilterBean getNotesFilterBean(NoteReqVO noteReqVO){
        NotesFilterBean notesFilterBean = null;
        if(noteReqVO.getUserId() != null && noteReqVO.getUserId() > 0){
            notesFilterBean = new NotesFilterBean();
            notesFilterBean.setUserId(noteReqVO.getUserId());
            notesFilterBean.setStatus(StatusEnum.ACTIVE.getStatus());
            notesFilterBean.setFromDate(dataUtility.convertStringToDate(noteReqVO.getFromDate(),null,DataUtility.noteDisplayFormat));
            notesFilterBean.setToDate(dataUtility.convertStringToDate(noteReqVO.getToDate(),null,DataUtility.noteDisplayFormat));
        }
        return notesFilterBean;
    }
    public Pageable getNotesPageable(PageableReqVO reqVO){
        return PageRequest.of((reqVO != null && reqVO.getStart() >= 1) ? reqVO.getStart() : 0,(reqVO != null && reqVO.getLength() > 0) ? reqVO.getLength() : 50);
    }

    private void buildAccount(NoteReqVO noteReqVO, NotesResponseVO notesResponseVO) {
        Optional<List<Account>> account;
        if (noteReqVO.getUserId() != null && noteReqVO.getUserId() > 0)
            account = accountRepository.findByUserUserIdAndDefaultAmountIsTrueAndStatus(noteReqVO.getUserId(), StatusEnum.ACTIVE.getStatus());
        else
            account = accountRepository.findByDefaultAmountIsTrueAndStatus(StatusEnum.ACTIVE.getStatus());
        account.ifPresent(accounts -> notesResponseVO.setAccountVOS(accounts.stream().map(account_ -> accountConverter.convertEntityToVo(account_, null)).toList()));
    }

    private void buildTransaction(NoteReqVO noteReqVO, NotesResponseVO notesResponseVO) {
        Optional<List<Transaction>> transactions;
        if (noteReqVO.getUserId() != null && noteReqVO.getUserId() > 0)
            transactions = transactionRepository.findByUserUserIdAndDefaultTransactionIsTrueAndStatus(noteReqVO.getUserId(), StatusEnum.ACTIVE.getStatus());
        else
            transactions = transactionRepository.findByDefaultTransactionIsTrueAndStatus(StatusEnum.ACTIVE.getStatus());
        transactions.ifPresent(transactions_ -> notesResponseVO.setTransactionVOS(transactions_.stream().map(transaction -> transactionConverter.convertEntityToVo(transaction, null)).toList()));
    }

    private void buildCategory(NoteReqVO noteReqVO, NotesResponseVO notesResponseVO) {
        Optional<List<Category>> categories;
        if (noteReqVO.getUserId() != null && noteReqVO.getUserId() > 0)
            categories = categoryRepository.findByUserUserIdAndDefaultCategoryIsTrue(noteReqVO.getUserId());
        else
            categories = categoryRepository.findByDefaultCategoryIsTrue();
        categories.ifPresent(categoryList -> notesResponseVO.setCategoryVOS(categoryList.stream().map(category -> categoryConverter.convertEntityToVO(category, null)).toList()));
    }
}
