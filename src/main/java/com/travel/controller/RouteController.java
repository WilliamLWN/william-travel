package com.travel.controller;

import com.github.pagehelper.PageInfo;
import com.travel.domain.Route;
import com.travel.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/route", produces = "application/json;charset=utf-8")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @RequestMapping("/findRouteListByCid")
    //搜索rname的时候都是单独搜索旅游路线的名称的，绝对不肯能出现根据类别与旅游名称一起去搜索的。
    public PageInfo<Route> findRouteListByCid(@RequestParam(defaultValue = "0") Integer cid,
                                              @RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "") String rname) {
        System.out.println("当前页：" + pageNum);

        if (!rname.equals("")) {
            cid = 0;
        }

        PageInfo<Route> pageInfo = routeService.findRouteByCid(pageNum, cid, rname);

        return pageInfo;
    }

    //    查询旅游路线详情
    @RequestMapping("/findRouteByRid")
    public Route findRouteByRid(Integer rid) {
//        2. 调用业务层查询Route对象
        Route route = routeService.findRouteByRid(rid);
//        3. 转成JSON对象输出给浏览器
        return route;
    }
}
