package com.hoya.service.constant;

import lombok.Getter;

public interface CustomerConstant {

    String COOKIE_NAME_TOKEN = "token";

    @Getter
    public enum MiaoShaStatus {

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
}
