package com.travel.utils;

import com.github.wxpay.sdk.PayConfig;
import com.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付工具类
 */
public class PayUtils {

    private static WXPay wxPay;

    // 支付成功回调地址
    private static String notifyUrl = "http://5d49be.natappfree.cc/pay/payNotify";

    // 初始化微信支付
    static {
        try {
            PayConfig payConfig = new PayConfig();
            payConfig.setAppID("wx8397f8696b538317"); // 公众账号ID
            payConfig.setMchID("1473426802");   // 商户号
            payConfig.setKey("T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"); // 生成签名的密钥
            wxPay = new WXPay(payConfig);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

	/**
	 * 生成微信订单支付URL
	 * @param orderId 订单的ID
	 * @param totalPay 支付金额
	 * @return 生成的URL
	 */
	public static String createOrder(String orderId, Integer totalPay) {
        Map<String, String> data = new HashMap<String, String>();
        // 商品描述
        data.put("body", "黑马旅游网支付中心-商品支付");
        // 订单号
        data.put("out_trade_no", orderId);
        //金额，单位是分
        data.put("total_fee", totalPay.toString());
        //调用微信支付的终端IP
        data.put("spbill_create_ip", "127.0.0.1");
        //回调地址
        data.put("notify_url", notifyUrl);
        // 支付有效时间10分钟
        Date now = new Date();
        Date now_10 = new Date(now.getTime() + 600000); //10分钟后的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
        String nowTime_10 = dateFormat.format(now_10);
        data.put("time_expire", nowTime_10);
        // 交易类型为扫码支付
        data.put("trade_type", "NATIVE");

        // 利用wxPay工具,完成下单
        Map<String, String> result = null;
        try {
            result = wxPay.unifiedOrder(data);
        } catch (Exception e) {
            throw new RuntimeException("微信下单失败", e);
        }
        // 校验业务状态
        checkResultCode(result);

        // 下单成功，获取支付链接
        String url = result.get("code_url");
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("微信下单失败，支付链接为空");
        }
        return url;
    }

	/**
	 * 检查业务状态
	 * @param result
	 */
	public static void checkResultCode(Map<String, String> result) {
        String resultCode = result.get("result_code");
        if ("FAIL".equals(resultCode)) {
            throw new RuntimeException("【微信支付】微信支付业务失败");
        }
    }

}
