package com.bu.MoneyManage.controller;

import com.bu.MoneyManage.Enum.StatusEnum;
import com.bu.MoneyManage.vo.UserReqVO;
import com.bu.MoneyManage.vo.UserResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController{
    @PostMapping("/userAuthenticate")
    public UserResponseVO userAuthenticate(@RequestBody UserReqVO userReqVO, HttpServletRequest servletRequest){
        UserResponseVO userResponseVO = null;
        try{
            userResponseVO = (UserResponseVO) callingService(userReqVO,"userAuthenticate");
            setSessionValue(servletRequest,userResponseVO);
            userResponseVO.setStatus(StatusEnum.SUCCESS.getStatus());
        }catch (Exception e){
            handleException(e,userResponseVO);
        }
        return userResponseVO;
    }
}
