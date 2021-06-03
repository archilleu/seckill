package com.hoya.service.server;


import com.hoya.service.model.OrderInfo;

public interface OrderInfoService {
    Long insertSelective(OrderInfo record);
}
