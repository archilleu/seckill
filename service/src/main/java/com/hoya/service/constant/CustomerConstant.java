package com.hoya.service.constant;

import lombok.Getter;

public interface CustomerConstant {

    String COOKIE_NAME_TOKEN = "token";

    //正在进行中
    Integer MS_ING = 0;

    //秒杀成功
    Integer MS_S = 1;

    //秒杀失败
    Integer MS_F = -1;

    //秒杀前缀
    class RedisKeyPrefix {

        public static final String PRODUCT_STOCK = "product_stock";

        public static final String PRODUCT = "product";

        public static final String MIAOSHA_ORDER = "miaosha_order";

        public static final String MIAOSHA_ORDER_WAIT = "miaosha_order_wait";

        public static final String MIAOSHA_VERIFY_CODE = "miaosha_verify_code";

        public static final String MIAOSHA_ORDER_TOKEN = "miaosha_order_token";

    }

    @Getter
    enum MiaoShaStatus {

        NOT_START(0, "未开始秒杀"),
        START(1, "秒杀开始"),
        END(2, "秒杀结束");

        private int code;
        private String message;

        private MiaoShaStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

    }

    class ZookeeperPathPrefix {

        public static final String PRODUCT_SOLD_OUT = "/product_sold_out";

        public static String getZKSoldOutProductPath(Long goodId) {
            return ZookeeperPathPrefix.PRODUCT_SOLD_OUT + "/" + goodId;
        }

    }
}
