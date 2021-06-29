package com.travel.service;

import com.travel.domain.Address;

import java.util.List;

public interface AddressService {
    /*
       根据用户的id查找用户的地址
    */
    List<Address> findByUid(int uid);

    // 作用：根据地址的aid查找指定的地址
    Address findByAid(int aid);
}
