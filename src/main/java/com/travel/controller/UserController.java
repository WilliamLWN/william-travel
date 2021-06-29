package com.travel.controller;

import com.travel.domain.ResultInfo;
import com.travel.domain.User;
import com.travel.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@RestController //@RestController = @Controller+@Responsebody  代表了该类的所有方法都是返回json字符串
@RequestMapping(path = "/user", produces = "application/json;charset=utf-8")
//要在类上指定`produces = "application/json; charset-UTF-8"`，因为导入了Jackson的XML的转换类，默认返回的是XML类型
public class UserController {

    @Autowired
    private UserService userService;


    //如果该类需要返回的一个json字符串，那么需要添加@Responsebody,但是用RestController代替了@ResponseBody
    @RequestMapping("/register")
    public ResultInfo register(@RequestBody Map<String,Object> loginMap) throws InvocationTargetException, IllegalAccessException { //因为传过来的json对象是个键值对组合用Map接收，而且键值对是{smsCode="验证码",user={username="XX",password="XX",telephone=".."}}
        //1. 从map中取出用户输入的验证码与User的参数
        String smsCode = (String) loginMap.get("smsCode");
        Map<String,Object> userMap = (Map<String, Object>) loginMap.get("user");
        User user = new User();
        BeanUtils.populate(user, userMap); //commons-BeanUtils自动封装Map和user对象
        //2. 如果验证码没有问题，然后去注册
        return userService.register(user, smsCode);
    }

    @RequestMapping("/findByUsername")
    public ResultInfo findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @RequestMapping("/sendSms")
    public ResultInfo sendSms(String telephone) {
        //        1. 调用工具类随机生成6个数字
        String authCode = RandomStringUtils.randomNumeric(6);
        //        2. 调用service完成短信发送
        return userService.sendSms(telephone, authCode);
    }

    @RequestMapping("/loginSendMessage")
    public ResultInfo loginSendMessage(String username) {
        return userService.loginSendSms(username);
    }

    @RequestMapping("/login")
    public ResultInfo login(@RequestBody Map<String,String> param, HttpSession session){
//        1. 调用业务方法登录
        ResultInfo resultInfo = userService.login(param);
//        2. 如果登录成功，则将用户对象以键"user"保存在会话域中
        if(resultInfo.getSuccess()) {
            session.setAttribute("user", resultInfo.getData());
        }
//        3. 返回结果对象
        return resultInfo;
    }

    @RequestMapping("/isLogged")
    public ResultInfo isLogged(HttpSession session){
//        1. 获取会话域中是否包含用户信息
        User user = (User) session.getAttribute("user");
//        2. 不为空表示已经登录，封装封装true和user对象返回
        if(user != null) {
            return new ResultInfo(true, user);
        }
//        3. 为空则封装false返回
        return new ResultInfo(false);
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }
}
