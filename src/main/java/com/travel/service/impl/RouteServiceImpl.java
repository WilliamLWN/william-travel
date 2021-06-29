package com.travel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.travel.dao.RouteDao;
import com.travel.domain.Category;
import com.travel.domain.Route;
import com.travel.domain.RouteImg;
import com.travel.domain.Seller;
import com.travel.service.RouteService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteDao routeDao;

    ////PageInfo对象是PageHelper提供给你去使用的，相当于以前你们自定义PageBean
    @Override
    public PageInfo<Route> findRouteByCid(Integer pageNum, Integer cid, String rname) {
        //1. 设置当前页与页面大大小
        PageHelper.startPage(pageNum, 3);

        //2. 查询页面中的数据
        List<Route> routeList = routeDao.findRouteByCid(cid, rname); //有sql要执行,在这里就需要拼接limit语句

        //3. 创建PageInfo把页面的数据存储到PageInfo中即可,
        PageInfo<Route> pageInfo = new PageInfo<Route>(routeList);

        return pageInfo;
    }

    @Override
    public Route findRouteByRid(int rid) {

        try {
//        1. 调用DAO得到一个Map对象，包含三张表的信息：线路，分类，商家
            Map<String, Object> routeMap = routeDao.findRouteByRid(rid);
//        2. 实例化三个对象Route, Category, Seller
            Route route = new Route();
            Category category = new Category();
            Seller seller = new Seller();
//        3. 使用BeanUtils将三个对象的属性都从Map中复制
            BeanUtils.populate(route, routeMap);
            BeanUtils.populate(category, routeMap);
            BeanUtils.populate(seller, routeMap);
//        4. 调用DAO得到线路对应的图片集合
            List<RouteImg> routeImgs = routeDao.findRouteImgsByRid(rid);
//        5. 将分类，商家，图片集合封装到Route对象的属性中
            route.setCategory(category);
            route.setSeller(seller);
            route.setRouteImgList(routeImgs);
//        6. 返回封装好的Route对象
            return route;
        } catch (Exception e) {
            e.printStackTrace();

            //最好是抛给运行时异常，是为了在编译时不报错，也不要直接抛给controller因为不是表示层的异常
            throw new RuntimeException(e);
        }
    }

    //根据旅游路线的rid查看旅游路线
    @Override
    public Route findRoute(int rid) {
        return routeDao.findRoute(rid);
    }
}
