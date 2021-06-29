package com.travel.service.impl;

import com.travel.dao.AddressDao;
import com.travel.domain.Address;
import com.travel.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findByUid(int uid) {
        return addressDao.findByUid(uid);
    }

    @Override
    public Address findByAid(int aid) {
        return addressDao.findByAid(aid);
    }
}
