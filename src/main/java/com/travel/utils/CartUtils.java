package com.travel.utils;

import com.travel.domain.Cart;
import com.travel.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class CartUtils {

    @Autowired
    private RedisTemplate<String, Cart> redisTemplate;  //注意，不能加static改为静态！！！！

    //设置购物车
    public void setCartToRedis(User user,Cart cart){
        ValueOperations<String, Cart> stringCartValueOperations = redisTemplate.opsForValue();
        stringCartValueOperations.set("cart_"+user.getUsername(),cart);
    }


    //获取购物车
    public Cart getCartFromRedis(User user){
        //1. 从redis中获取购物车
        ValueOperations<String, Cart> stringCartValueOperations = redisTemplate.opsForValue();
        Cart cart = stringCartValueOperations.get("cart_" + user.getUsername());
        //2. 判断从redis取出cart是为空
        if(cart==null){
            cart = new Cart();
            //并且把该购物车存储到redis
            stringCartValueOperations.set("cart_"+user.getUsername(),cart);
        }
        return cart;
    }


    //删除购物车
    public void removeCart(User user){
        redisTemplate.delete("cart_"+user.getUsername());
    }
}
