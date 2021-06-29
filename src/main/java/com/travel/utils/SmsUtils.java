package com.travel.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 发送短信息的工具类
 */
public class SmsUtils {

    //这里要换成在阿里云申请的：accessKeyId和secret
    private static String accessKeyId = "LTAI4GHhatiznGG52T5EidAW";
    private static String secret = "xOFzvwf7G8jPhL3eNpyOmjPjFRZJfx";

    /**
     * 发送短信的方法
     * @param phoneNumbers 11位电话号码，格式：15900000000
     * @param signName 签名名称
     * @param templateCode 模板CODE
     * @param authCode 要发的验证码
     * @return 返回的信息字符串
     */

    //会返回OK
    public static String send(String phoneNumbers, String signName, String templateCode, String authCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + authCode + "\"}");

        String code = null; //返回的字符串
        try {
            //得到返回的响应对象
            CommonResponse commonResponse = client.getCommonResponse(request);
            //得到返回的JSON字符串
            //{"Message":"OK","RequestId":"E3C3BE8F-2294-4541-A3F3-A98A29BB6F55","BizId":"375420194646745909^0","Code":"OK"}
            String json = commonResponse.getData();

            //返回的JSON字符串转成Map对象
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            System.out.println(map);
            //获取键为Message的值
            code = (String) map.get("Code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static void main(String[] args) {
        String code = SmsUtils.send("13250366914", "黑马旅游网", "SMS_205126318", "6789");
        System.out.println(code);
    }
}
