package com.hoya.service.dao;

import com.hoya.service.model.OrderInfo;

public interface OrderInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    Long insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    public OrderInfo getOrderById(long orderId);

}