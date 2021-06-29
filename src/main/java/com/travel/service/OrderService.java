package com.travel.service;

import com.travel.domain.Address;
import com.travel.domain.Order;
import com.travel.domain.User;

import java.util.Map;

public interface OrderService {

    /**
     * 用户购物车提交订单
     * @param user
     * @param address
     * @return
     */
    Order save(User user, Address address);

    /**
     编写updateState更新订单状态的方法
     1. 参数是Map，用Map封装了微信支付的结果SUCCESS/FAIL，  OK
     2. 其中包含以下2个键：out_trade_no订单号，result_code支付结果
     */
    void updateState(Map<String, String> param);

    /**
     通过oid查询订单的状态
     @return 如果支付返回true，没有支付返回false
     */
    boolean isPay(String oid);
}
