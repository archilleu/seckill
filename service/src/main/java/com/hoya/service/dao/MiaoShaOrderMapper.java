package com.hoya.service.dao;

import com.hoya.service.model.MiaoShaOrder;

import java.util.List;

public interface MiaoShaOrderMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(MiaoShaOrder record);

    MiaoShaOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaOrder record);

    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    public List<MiaoShaOrder> listByGoodsId(long goodsId);
}