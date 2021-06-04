package com.hoya.service.controller;

import com.hoya.core.exception.ServerExceptionNotFound;
import com.hoya.service.annotation.AccessLimit;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/detail/{goodsId}")
    public GoodsDetailVo goodsDetail(HttpServletResponse response, MiaoShaUser user, @PathVariable Long goodsId) {
        try {
            GoodsVo goods = goodsService.getGoodsVoByGoodId(goodsId);
            long startAt = goods.getStartDate().getTime();
            long endAt = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();
            int miaoshaStatus = 0;
            int remainSeconds = 0;
            if (now < startAt) {
                // 秒杀还没开始
                miaoshaStatus = 0;
                remainSeconds = (int) ((startAt - now) / 1000);
            } else if (now > startAt) {
                // 秒杀结束
                miaoshaStatus = 2;
                remainSeconds = -1;
            } else {
                // 秒杀进行中
                miaoshaStatus = 1;
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
