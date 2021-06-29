package com.travel.controller;

import com.travel.domain.*;
import com.travel.service.AddressService;
import com.travel.service.OrderService;
import com.travel.utils.CartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/order", produces = "application/json;charset=utf-8")
public class OrderController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartUtils cartUtils;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/prepareOrder")
    public ResultInfo prepareOrder(HttpSession session){
//        1. 从会话域中获取用户对象
        User user = (User) session.getAttribute("user");
//        2. 调用AddressService，查询当前用户的所有收货地址
        List<Address> addressList = addressService.findByUid(user.getUid());
//        3. 从Redis中查询当前用户的购物车
        Cart cart = cartUtils.getCartFromRedis(user);
//        4. 将地址列表和购物车放在Map对象中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("addressList", addressList);
        map.put("cart", cart);
//        5. 将Map对象再封装到ResultInfo对象中返回
        return new ResultInfo(true, map);
    }

    @RequestMapping("/subOrder")
    public ResultInfo subOrder(HttpSession session,int aid){
        //        1. 从会话域中获取用户对象
        User user = (User) session.getAttribute("user");
//        2. 通过aid得到地址对象
        Address address = addressService.findByAid(aid);
//        3. 调用业务层的方法
        Order order = orderService.save(user, address);
//        4. 返回添加结果，并且封装order对象到ResultInfo的data属性中
        return new ResultInfo(true, order);
    }
}
