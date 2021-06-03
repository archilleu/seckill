package com.hoya.service.server.impl;

import com.hoya.service.dao.OrderInfoMapper;
import com.hoya.service.model.OrderInfo;
import com.hoya.service.server.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public Long insertSelective(OrderInfo record) {
        return orderInfoMapper.insertSelective(record);
    }
}
