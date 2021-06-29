package com.travel.domain;

import lombok.Data;

@Data
public class CartItem {
    private Route route; //商品对象
    private int num; //数量
    private double subTotal; //小计

    public double getSubTotal() {
//        计算结果 = 线路价格 * 数量
//        返回计算结果
        return route.getPrice() * num;
    }
}
