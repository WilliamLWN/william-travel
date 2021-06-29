package com.travel.service.impl;

import com.travel.dao.OrderDao;
import com.travel.dao.OrderItemDao;
import com.travel.domain.*;
import com.travel.service.OrderService;
import com.travel.utils.CartUtils;
import com.travel.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CartUtils cartUtils;

    @Autowired
    private OrderItemDao orderItemDao;

    /**
     * 用户购物车提交订单
     *
     * @param user
     * @param address
     * @return
     */
    @Override
    public Order save(User user, Address address) {

        Cart cart = cartUtils.getCartFromRedis(user);

        Order order = new Order();
        order.setOid(UuidUtils.simpleUuid());
        order.setOrdertime(new Date());
        order.setTotal(cart.getCartTotal());
        order.setState(0); //0代表未付款
        order.setContact(address.getContact());
        order.setAddress(address.getAddress());
        order.setTelephone(address.getTelephone());
        order.setUid(user.getUid());

        orderDao.save(order);

        //遍历购物项，设置订单项
        Collection<CartItem> cartItems = cart.getCartItemMap().values();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemtime(new Date());
            orderItem.setState(0);
            orderItem.setNum(cartItem.getNum());
            orderItem.setSubtotal(cartItem.getSubTotal());
            orderItem.setRid(cartItem.getRoute().getRid());
            orderItem.setOid(order.getOid());

            orderItemDao.save(orderItem);
        }

        //清空购物车
        cartUtils.removeCart(user);

        //返回订单对象给页面回显
        return order;
    }

    /**
     * 编写updateState更新订单状态的方法
     * 1. 参数是Map，用Map封装了微信支付的结果SUCCESS/FAIL，  OK
     * 2. 其中包含以下2个键：out_trade_no订单号，result_code支付结果
     *
     * @param param
     */
    @Override
    public void updateState(Map<String, String> param) {
        //1. 从微信回调封装的map中取出订单号，还有支付的结果
        String oid = param.get("out_trade_no");
        String resultCode = param.get("result_code");

        //2. 判断是否支付成功
        if("SUCCESS".equals(resultCode)) {
            orderDao.updateState(oid);
            orderItemDao.updateState(oid);
        } else {
            throw new RuntimeException("微信支付失败");
        }
    }

    /**
     * 通过oid查询订单的状态
     *
     * @param oid
     * @return 如果支付返回true，没有支付返回false
     */
    @Override
    public boolean isPay(String oid) {
//        1. 通过oid查询订单对象
        Order order = orderDao.findByOid(oid);
//        2. 判断订单的状态
//        1. 如果等于0，返回false，表示未支付
//        2. 否则返回true，表示已支付
        return order.getState() == 1 ? true : false;
    }
}
