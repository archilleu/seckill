package com.hoya.service.dao;

import com.hoya.service.model.Goods;
import com.hoya.service.model.MiaoShaGoods;
import com.hoya.service.vo.GoodsVo;

import java.util.List;

public interface GoodsMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    List<GoodsVo> goodsVoList();

    GoodsVo getGoodsVoByGoodsId(long goodsId);

    int reduceStock(MiaoShaGoods miaoShaGoods);

    int reduceMiaoshaStock(MiaoShaGoods miaoShaGoods);

}