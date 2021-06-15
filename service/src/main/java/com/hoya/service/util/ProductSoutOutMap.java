package com.hoya.service.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 在本机内存中标记商品已经销售完毕
 */
public class ProductSoutOutMap {
    private static final ConcurrentHashMap<Long, Boolean> productSoldOutMap = new ConcurrentHashMap<>();

    public static void markSoldOut(Long goodsId) {
        productSoldOutMap.put(goodsId, true);
    }

    public static void clearSoldOut(Long goodsId) {
        productSoldOutMap.remove(goodsId);
    }

    public static Boolean isSoldOut(Long goodsId) {
        return productSoldOutMap.get(goodsId);
    }
}
