package com.travel.dao;

import com.travel.domain.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface OrderItemDao {

    /**
     * 把购物项数据插入到订单项
     * @param orderItem
     * @return
     */
    @Insert("insert into tab_orderitem(itemtime,state,num,subtotal,rid,oid) values(#{itemtime},#{state},#{num},#{subtotal},#{rid},#{oid})")
    int save(OrderItem orderItem);

    @Update("update tab_orderitem set state=1 where oid = #{oid}")
    void updateState(String oid);
}
