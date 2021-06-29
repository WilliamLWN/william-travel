package com.travel.dao;

import com.travel.domain.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryDao {
    @Select("select * from tab_category order by cid")
    public List<Category> findAll();
}
