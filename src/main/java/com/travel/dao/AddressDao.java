package com.travel.dao;

import com.travel.domain.Address;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AddressDao {

    /*
  根据用户的id查找用户的地址
 */
    @Select("select * from tab_address where uid=#{uid}")
    List<Address> findByUid(int uid);

//    通过aid查询一个地址
    @Select("select * from tab_address where aid = #{aid}")
    Address findByAid(int aid);
}

