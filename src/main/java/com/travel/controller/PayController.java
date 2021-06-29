package com.travel.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.travel.service.OrderService;
import com.travel.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/pay") //因为微信支付传输xml所以不用写produced
public class PayController {

    @Autowired
    private OrderService orderService;

    /**
     * 通知微信支付的后台帮我们生成一个支付的链接
     * @param oid
     * @param total
     * @return
     */
    @RequestMapping("/createUrl")
    public String createUrl(String oid,int total){
        //给订单号oid和支付金额total给微信支付会生成支付链接，这里不支付total只支付1分钱
        String payURL = PayUtils.createOrder(oid, 1);
        System.out.println("付款链接：" + payURL);
        return payURL;
    }

    /**
     * 作用： 读取微信支付的支付结果，微信支付通过内网穿透走这个方法
     * @param request
     * @return
     */
    @RequestMapping("/payNotify")
    public Map<String,String> payNotify(HttpServletRequest request) throws IOException {
        //        1. 接收请求的输入流对象，这是一个xml格式的文件
        ServletInputStream inputStream = request.getInputStream();
        //        2. 将XML转成一个Map对象,用jackson.dataformat包的XmlMapper可以自动读/写XML与其他类型
        XmlMapper xmlMapper = new XmlMapper();
        Map map = xmlMapper.readValue(inputStream, Map.class);
        //        3. 根据支付的结果调用orderService，修改订单状态
        orderService.updateState(map);
        //4. 如果接收到微信支付的回调之后，处理完毕一定要通知微信支付，否则会不定时回调你的这个函数
        Map<String,String> resultMap = new HashMap<String, String>();
        resultMap.put("return_code","SUCCESS");
        resultMap.put("return_msg","OK");

        //返回给微信支付的
        return resultMap;
    }

    /**
     * 作用： 指定订单号查询订单的支付状态
     * @param oid  订单号
     * @return
     */
    @RequestMapping("/findStatus")
    public boolean findStatus(String oid){
        return orderService.isPay(oid);
    }
}
