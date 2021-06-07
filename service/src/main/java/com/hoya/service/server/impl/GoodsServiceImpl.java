package com.hoya.service.server.impl;

import com.hoya.service.dao.GoodsMapper;
import com.hoya.service.model.MiaoShaGoods;
import com.hoya.service.server.GoodsService;
import com.hoya.service.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> goodsVoList() {
        return goodsMapper.goodsVoList();
    }

    @Override
    public GoodsVo getGoodsVoByGoodId(Long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    @Override
    @Transactional
    public Boolean reduceStock(GoodsVo goods) {
        MiaoShaGoods miaoShaGoods = new MiaoShaGoods();
        miaoShaGoods.setGoodsId(goods.getId());
        goodsMapper.reduceStock(miaoShaGoods);
        goodsMapper.reduceMiaoshaStock(miaoShaGoods);
        return true;
    }
}
