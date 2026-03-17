package com.bu.MoneyManage.controller;

import com.bu.MoneyManage.Enum.StatusEnum;
import com.bu.MoneyManage.vo.NoteReqVO;
import com.bu.MoneyManage.vo.NotesResponseVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotesController extends BaseController{
    @PostMapping("/createOrUpdateNote")
    public NotesResponseVO createOrUpdateNote(@RequestBody NoteReqVO noteReqVO){
        NotesResponseVO notesResponseVO =  new NotesResponseVO();
        try{
            notesResponseVO = (NotesResponseVO) callingService(noteReqVO,"createOrUpdateNote");
            notesResponseVO.setStatus(StatusEnum.SUCCESS.getStatus());
        } catch (Exception e) {
            notesResponseVO = (NotesResponseVO) handleException(e,notesResponseVO);
        }
        return notesResponseVO;
    }
    @PostMapping("/fetchNotesInfo")
    public NotesResponseVO fetchNotesInfo(@RequestBody NoteReqVO noteReqVO){
        NotesResponseVO notesResponseVO =  new NotesResponseVO();
        try{
            notesResponseVO = (NotesResponseVO) callingService(noteReqVO,"fetchNotesInfo");
            notesResponseVO.setStatus(StatusEnum.SUCCESS.getStatus());
        } catch (Exception e) {
            notesResponseVO = (NotesResponseVO) handleException(e,notesResponseVO);
        }
        return notesResponseVO;
    }
    @PostMapping("/fetchNotes")
    public NotesResponseVO fetchNotes(@RequestBody NoteReqVO noteReqVO){
        NotesResponseVO notesResponseVO = new NotesResponseVO();
        try{
            notesResponseVO = (NotesResponseVO) callingService(noteReqVO,"fetchNotes");
            notesResponseVO.setStatus(StatusEnum.SUCCESS.getStatus());
        } catch (Exception e) {
            notesResponseVO = (NotesResponseVO) handleException(e,notesResponseVO);
        }
        return notesResponseVO;
    }
}
