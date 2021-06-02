package com.hoya.service.server.impl;

import com.hoya.service.dao.GoodsMapper;
import com.hoya.service.server.GoodsService;
import com.hoya.service.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public GoodsVo getGoodsVoByGoodId(Long goodId) {
        return goodsMapper.getGoodsVoByGoodsId(goodId);
    }

    @Override
    public Boolean reduceStock(GoodsVo goods) {
        return null;
    }
}
