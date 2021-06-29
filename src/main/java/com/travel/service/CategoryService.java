package com.travel.service;

import com.travel.domain.Category;

import java.util.List;

public interface CategoryService {

    //查询所有的旅游路线类别
    public List<Category> findAll();
}
