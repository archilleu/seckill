package com.hoya.service.server;

import com.hoya.service.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    List<GoodsVo> goodsVoList();

    GoodsVo getGoodsVoByGoodId(Long goodId);

    Boolean reduceStock(GoodsVo goods);
   
}
