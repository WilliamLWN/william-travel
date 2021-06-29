package com.travel.service;

import com.travel.domain.ResultInfo;
import com.travel.domain.User;

import java.util.Map;

public interface UserService {

//    定义ResultInfo register(User user)方法实现用户的注册
    ResultInfo register(User user, String smsCode);

    //判断用户是否存在
    ResultInfo findByUsername(String username);

    //阿里云验证码
    ResultInfo sendSms(String telephone, String authCode);

    //登录发验证码
    ResultInfo loginSendSms(String username);

    //登录方法
    ResultInfo login(Map<String,String> param);
}
