package com.travel.controller;

import com.travel.domain.*;
import com.travel.service.RouteService;
import com.travel.utils.CartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartUtils cartUtils;

    @Autowired
    private RouteService routeService;

    //    添加到购物车
    @RequestMapping("/addCart")
    public ResultInfo addCart(int rid, int num, HttpSession session) {

//        3. 从会话域中获取用户对象
        User user = (User) session.getAttribute("user");
//        4. 调用工具类获取当前用户的购物车
        Cart cart = cartUtils.getCartFromRedis(user);
//        5. 获取所有的购物项
        LinkedHashMap<Integer, CartItem> cartItemMap = cart.getCartItemMap();
//        6. 通过rid获取购物车项，判断当前商品的购物项是否存在
        CartItem cartItem = cartItemMap.get(rid);
//        1. 如果为空，则调用业务层根据rid查询route对象
        if (cartItem == null) {
            //        2. 创建购物项对象
            cartItem = new CartItem();
            //        3. 设置属性值：num和route
            System.out.println("num:" + num);
            cartItem.setNum(num);
            Route route = routeService.findRoute(rid);
            cartItem.setRoute(route);
            //        4. 把购物车项添加到购物车中
            cartItemMap.put(rid, cartItem);
        } else {
            //        7. 如果购物项不为空，则数量累加
            cartItem.setNum(cartItem.getNum() + num);
        }
        //        8. 将"购物车"更新到redis中
        cartUtils.setCartToRedis(user, cart);
        //        9. 将购物车项添加到会话域中,给添加成功页面回显
        session.setAttribute("cartItem", cartItem);
//        10. 返回ResultInfo对象
        return new ResultInfo(true, "添加购物车成功");
    }

    //添加购物车项成功后回显
    @RequestMapping("/showCartItem")
    public ResultInfo showCartItem(HttpSession session) {
//        从会话域中获取cartItem对象
        CartItem cartItem = (CartItem) session.getAttribute("cartItem");

        return new ResultInfo(true, cartItem);
    }

    //查看购物车
    @RequestMapping("/findAll")
    public  ResultInfo findAll(HttpSession session){
        //1. 得到当前登录的用户
        User user = (User) session.getAttribute("user");
        //2. 根据用户名从redis中取出购物车
        Cart cart = cartUtils.getCartFromRedis(user);
        System.out.println("cart是" + cart);
        //3. 把购物车封装到resultInfo中
        return new ResultInfo(true, cart);
    }

    //删除购物车
    @RequestMapping("/deleteCartItem")
    public Cart deleteCartItem(int rid,HttpSession session){
        User user = (User) session.getAttribute("user");

        Cart cart = cartUtils.getCartFromRedis(user);

        LinkedHashMap<Integer, CartItem> cartItemMap = cart.getCartItemMap();

        cartItemMap.remove(rid);

        cartUtils.setCartToRedis(user, cart);

        return cart;
    }
}
