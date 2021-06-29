package com.travel.dao;

import com.travel.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

//    1. 根据用户名查询用户是否存在：User findByUsername(String username)
        @Select("select * from tab_user where username = #{username}")
        User findByUsername(String username);

//    2. 根据手机号查询用户是否存在：User findByTelephone(String telephone)
        @Select("select * from tab_user where telephone = #{telephone}")
        User findByTelephone(String telephone);

//    3. 保存用户对象，只添加username,password,telephone这三个属性。int save(User user)
    @Insert("insert into tab_user(username,password,telephone,salt) values(#{username},#{password},#{telephone},#{salt})")
        void save(User user);
}
