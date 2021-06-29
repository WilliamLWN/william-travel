package com.travel.dao;

import com.travel.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderDao {

//    oid需要自己随机生成字符串，不是自动生成，所以oid需要写入数据库字段

    /**
     * 把购物车数据插入订单表
     * @param order
     * @return
     */
    @Insert("insert into tab_order values(#{oid},#{ordertime},#{total},#{state},#{address},#{contact},#{telephone},#{uid})")
    int save(Order order);

    @Update("update tab_order set state=1 where oid = #{oid}")
    void updateState(String oid);

    /**
     * 作用： 根据订单的id查询订单
     * @param oid  订单的id号
     * @return
     */
    @Select("select * from tab_order where oid=#{oid}")
    Order findByOid(String oid);
}
