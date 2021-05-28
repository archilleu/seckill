package com.hoya.core.page;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class PageRequest {
    /**
     * 当前页码
     */
    private int page = 1;

    /**
     * 每页数量
     */
    private int rows = 10;

    /**
     * 查询参数
     */
    private Map<String, Object> params = new LinkedHashMap<>();

    /**
     * 获取参数key值
     * @param key
     * @return value值
     */
    public Object getParam(String key) {
        return getParams().get(key);
    }
}
