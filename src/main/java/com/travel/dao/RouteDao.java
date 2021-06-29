package com.travel.dao;

import com.travel.domain.Route;
import com.travel.domain.RouteImg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface RouteDao {

    List<Route> findRouteByCid(@Param("cid") Integer cid, @Param("rname") String rname);

    //根据旅游路线的rid查看旅游路线、商家、类别，给商品详细页面用
    @Select("select * from tab_route r inner join tab_seller s on r.sid = s.sid inner join tab_category c on r.cid = c.cid where r.rid = #{rid}")
    Map<String,Object> findRouteByRid(Integer rid);

    //根据旅游路线的rid查询该旅游路线的图片
    @Select("select * from tab_route_img where rid = #{rid}")
    List<RouteImg> findRouteImgsByRid(Integer rid);

    //    根据旅游路线的rid查看旅游路线，给购物车的购物项用
    @Select("select * from tab_route where rid = #{rid}")
    Route findRoute(Integer rid);
}
