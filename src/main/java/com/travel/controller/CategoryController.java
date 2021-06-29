package com.travel.controller;

import com.travel.domain.Category;
import com.travel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/category", produces = "application/json;charset=utf-8")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/findAll")
    public List<Category> findAll(){
        List<Category> categories = categoryService.findAll();
        return categories;
    }
}
