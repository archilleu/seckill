package com.hoya.service.server;

import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import com.hoya.service.vo.OrderInfoVo;

public interface OrderService {

    MiaoShaOrderVo getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId);

    OrderInfoVo createOrder(MiaoShaUserVo user, GoodsVo goods);

    OrderInfoVo getOrderById(Long orderId);

}
