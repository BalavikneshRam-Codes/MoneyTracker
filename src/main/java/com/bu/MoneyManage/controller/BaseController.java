package com.bu.MoneyManage.controller;

import com.bu.MoneyManage.Enum.StatusEnum;
import com.bu.MoneyManage.service.INotesService;
import com.bu.MoneyManage.vo.BaseItemVO;
import com.bu.MoneyManage.vo.BaseVO;
import com.bu.MoneyManage.vo.UserResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.boot.internal.Abstract;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseController {
    @Autowired
    private INotesService notesService;

    protected BaseVO callingService(BaseVO baseVO,String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BaseVO response = null;
        Class aClass = notesService.getClass();
        Class<?>[] noparams = {BaseVO.class};
        Method method = aClass.getDeclaredMethod(methodName,noparams);
        response = (BaseVO) method.invoke(notesService,baseVO);
        return response;
    }
    protected BaseItemVO handleException(Exception e,BaseItemVO baseItemVO){
        if(baseItemVO == null)
            baseItemVO = new BaseItemVO();
        baseItemVO.setStatus(StatusEnum.FAILED.getStatus());
        baseItemVO.setErrorMessage(e.getMessage());
        return baseItemVO;
    }
    protected void setSessionValue(HttpServletRequest servletRequest, UserResponseVO userResponseVO){
        if(servletRequest != null && servletRequest.getSession() != null)
            servletRequest.getSession().setAttribute("LoggedUserId",userResponseVO.getUserId());
    }
}
