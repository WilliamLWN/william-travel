package com.travel.service.impl;

import com.travel.dao.CategoryDao;
import com.travel.domain.Category;
import com.travel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplImpl implements CategoryService {

    @Autowired
    private RedisTemplate<String ,Object> redisTemplate;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> findAll() {
//        1. 先从redis中获取线路分类集合
        List<Category> categories = (List<Category>) redisTemplate.opsForValue().get("categories");
//        2. 如果为空，则查询数据库，得到List集合
        if(categories == null) {
//        3. 将List集合存入redis中
            categories = categoryDao.findAll();
            redisTemplate.opsForValue().set("categories", categories);
        }
//        4. 返回List对象
        return categories;
    }
}
