package com.travel.service;

import com.github.pagehelper.PageInfo;
import com.travel.domain.Route;

public interface RouteService {

    //根据旅游路线类别查询旅游路线
    //PageInfo对象是PageHelper提供给你去使用的，相当于以前你们自定义PageBean
    PageInfo<Route> findRouteByCid(Integer cid, Integer pageNum, String rname);

//    查询线路详情，包含了4张表的信息，给商品详细页面用
    Route findRouteByRid(int rid);

//    根据旅游路线的rid查看旅游路线，给购物车的购物项用
    Route findRoute(int rid);
}
