package com.hoya.service.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础模型
 */
@Data
public class BaseModel {

    private Long id;

    private String createBy;

    //时间精确到秒(数据库精确到秒)
    private LocalDateTime createTime;

    private String lastUpdateBy;

    //时间精确到秒(数据库精确到秒)
    private LocalDateTime lastUpdateTime;

}
