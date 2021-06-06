package com.hoya.service.controller;

import com.hoya.core.exception.ServerExceptionNotFound;
import com.hoya.service.constant.CustomerConstant;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.vo.GoodsDetailVo;
import com.hoya.service.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/detail/{goodsId}")
    public GoodsDetailVo goodsDetail(MiaoShaUser user, @PathVariable Long goodsId) {
        try {
            GoodsVo goods = goodsService.getGoodsVoByGoodId(goodsId);
            long startAt = goods.getStartDate().getTime();
            long endAt = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();
            int miaoshaStatus = 0;
            int remainSeconds = 0;
            if (now < startAt) {
                // 秒杀还没开始
                miaoshaStatus = CustomerConstant.MiaoShaStatus.NOT_START.getCode();
                remainSeconds = (int) ((startAt - now) / 1000);
            } else if (now > startAt) {
                // 秒杀结束
                miaoshaStatus = CustomerConstant.MiaoShaStatus.END.getCode();
                remainSeconds = -1;
            } else {
                // 秒杀进行中
                miaoshaStatus = CustomerConstant.MiaoShaStatus.START.getCode();
                remainSeconds = 0;
            }

            GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
            goodsDetailVo.setGoods(goods);
            goodsDetailVo.setUser(user);
            goodsDetailVo.setRemainSeconds(remainSeconds);
            goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
            return goodsDetailVo;
        } catch (Exception e) {
            log.error("查询商品失败 error:{}", e);
            throw new ServerExceptionNotFound("商品不存在");
        }
    }

    @GetMapping("/list")
    public List<GoodsVo> goodsList(MiaoShaUser user) {
        List<GoodsVo> goods = goodsService.goodsVoList();
        return goods;
    }

}
