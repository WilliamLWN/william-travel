package com.travel.service.impl;

import com.travel.dao.UserDao;
import com.travel.domain.ResultInfo;
import com.travel.domain.User;
import com.travel.service.UserService;
import com.travel.utils.Md5Utils;
import com.travel.utils.SmsUtils;
import com.travel.utils.UuidUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultInfo register(User user, String smsCode) {

        //从redis中取出系统生成的验证码
        String authCode = (String) redisTemplate.opsForValue().get("smsCode_" + user.getTelephone());
        //如果验证码为空则：new ResultInfo(false, "验证码过期")
        if(authCode == null) {
            return new ResultInfo(false, "验证码过期");
        }
        //如果不相等则：new ResultInfo(false, "验证码错误")
        if(!authCode.equalsIgnoreCase(smsCode)) {
            return new ResultInfo(false, "验证码错误");
        }

        //1. 检查是否存在用户名
        User dbUser = userDao.findByUsername(user.getUsername());
        if(dbUser != null) {
            return new ResultInfo(false, "用户名已被使用");
        }

        //2. 检查手机号是否已经被注册
        User dbTelephone = userDao.findByTelephone(user.getTelephone());
        if(dbTelephone != null) {
            return new ResultInfo(false, "手机号已被使用");
        }

        //3. 对密码进行加盐加密   加盐加密 = 用户名+密码+盐(随机字符串)
        String salt = UuidUtils.simpleUuid();
        user.setSalt(salt);
        String passwordStr = user.getUsername() + user.getPassword() + salt;

        //得到加密后的密码
        String md5Password = Md5Utils.getMd5(passwordStr);
        user.setPassword(md5Password);

        //4. 保存用户的信息
        userDao.save(user);
        return new ResultInfo(true);
    }

    @Override
    public ResultInfo findByUsername(String username) {
        User dbUsername = userDao.findByUsername(username);
        if(dbUsername != null) {
            return new ResultInfo(false, "用户名已被使用");
        }
        return new ResultInfo(true, "用户名可以使用");
    }

    @Override
    public ResultInfo sendSms(String telephone, String authCode) {
//      1. 调用工具类发送短信
//      提供：电话号码，签名，模板号，验证码四个参数
//        String code = SmsUtils.send(telephone, "黑马旅游网", "SMS_205126318", authCode);
        System.out.println("验证码：" + authCode);
        String code = "OK";
//            2.判断是否为OK
        if("OK".equalsIgnoreCase(code)) {

            //发送成功之后我们需要把验证码存储到redis中
            redisTemplate.opsForValue().set("smsCode_"+telephone, authCode, 30, TimeUnit.SECONDS);//设置10秒过期时间

            return new ResultInfo(true, "短信发送成功");
        }
        return new ResultInfo(false, "短信发送失败");
    }

    @Override
    public ResultInfo loginSendSms(String username) {
        //        1. 通过用户名查询用户对象
        User dbUser = userDao.findByUsername(username);
        //        2. 如果用户对象为空，则返回用户名不存在
        if(dbUser == null) {
            return new ResultInfo(false, "用户名不存在");
        }
        //        3. 否则获取手机号
        String telephone = dbUser.getTelephone();
        //        4. 随机生成验证码
        String authCode = RandomStringUtils.randomNumeric(6);
        //        5. 调用发送短信验证码方法
        return sendSms(telephone, authCode);
    }

    @Override
    public ResultInfo login(Map<String, String> param) {
        //从map中取出前端页面提交的参数
        String username = param.get("username");
        String password = param.get("password");
        String smsCode = param.get("smsCode");
        //        1. 通过用户名查找用户
        User dbUser = userDao.findByUsername(username);
//        2. 如果没有找到，则返回用户名不存在
        if(dbUser == null) {
            return new ResultInfo(false, "用户名不存在");
        }
//        3. 如果找到，判断密码是否正确
//        3.1. 拼接字符串=用户名+用户输入的密码+盐
        String passwordStr = username + password + dbUser.getSalt();
//        3.2. 对密码使用md5加密
        String md5Password = Md5Utils.getMd5(passwordStr);
//        4. 如果密码错误，则返回密码错误的结果
        if(!md5Password.equals(dbUser.getPassword())) {
            return new ResultInfo(false, "密码错误");
        }
//        5. 再从Redis中获取验证码，判断验证码是否为空
        String authCode = (String) redisTemplate.opsForValue().get("smsCode_" + dbUser.getTelephone());
//        6. 如果验证码为空，则表示验证码过期
        if(authCode == null) {
            return new ResultInfo(false, "验证码过期");
        } else {
//        7. 如果验证码不为空，则判断验证码是否正确
            if(!smsCode.equalsIgnoreCase(authCode)) {
                return new ResultInfo(false, "验证码错误");
            }
        }
        //        8. 如果验证码正确，则返回登录成功，并且封装user对象到data属性中
        return new ResultInfo(true, dbUser);
    }
}
