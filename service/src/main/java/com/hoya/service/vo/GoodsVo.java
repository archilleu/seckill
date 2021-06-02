package com.hoya.service.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsVo {

    private Long id;

    private BigDecimal miaoshaPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    private BigDecimal goodsPrice;

    private Integer goodsStock;

    private String goodsDetail;
}
