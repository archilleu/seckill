package com.hoya.service.server;

import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import com.hoya.service.vo.OrderInfoVo;

public interface MiaoshaService {

    OrderInfoVo miaosha(MiaoShaUserVo user, GoodsVo goods);

    Integer insertMiaoshaOrder(MiaoShaOrderVo miaoshaOrder);

    MiaoShaOrderVo getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId);

    String createMiaoshaPath(MiaoShaUserVo user, Long goodsId);

    Long getMiaoshaResult(Long userId, Long goodsId);

    Boolean checkPath(MiaoShaUserVo user, long goodsId, String path);

}
