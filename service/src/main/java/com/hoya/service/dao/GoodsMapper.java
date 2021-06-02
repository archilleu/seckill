package com.hoya.service.dao;

import com.hoya.service.model.Goods;
import com.hoya.service.vo.GoodsVo;

import java.util.List;

public interface GoodsMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    List<GoodsVo> goodsVoList();

    GoodsVo goodsVoByGoodsId(Long goodsId);

    GoodsVo getGoodsVoByGoodsId(long goodsId);

}