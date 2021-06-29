package com.travel.domain;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedHashMap;

@Data
public class Cart {
     private int cartNum; //购物车商品总数量
     private double cartTotal; //购物车总金额
     private LinkedHashMap<Integer, CartItem> cartItemMap; //购物项集合，使用rid做为键 ,一定要在这里创建对象不能在get方法中创建！！
//    注：这里要创建LinkedHashMap,便于购物项的修改

    public int getCartNum() {
//        1. 将购物车数量清0
//        2. 遍历购物项集合，得到每个购物项
//        3. 获取购物项的数量进行累加
//        4. 返回累加的值

        this.cartNum = 0;
        Collection<CartItem> cartItems = cartItemMap.values();
        for (CartItem cartItem : cartItems) {
            this.cartNum += cartItem.getNum();
        }
        return cartNum;
    }

    public double getCartTotal() {
        this.cartTotal = 0;

        Collection<CartItem> values = cartItemMap.values();
        for (CartItem cartItem : values) {
            this.cartTotal += cartItem.getSubTotal();
        }
        return cartTotal;
    }
}
