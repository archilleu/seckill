package com.hoya.core.page;

import com.github.pagehelper.PageInfo;
import com.hoya.core.exception.ServerExceptionServerError;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"all"})
public class PageHelper {
    private static final String findPage = "findPage";

    public static PageResult findPage(PageRequest pageRequest, Object mapper) {
        return findPage(pageRequest, mapper, findPage);
    }

    public static PageResult findPage(PageRequest pageRequest, Object mapper, Map<String, Object> args) {
        return findPage(pageRequest, mapper, findPage, args);
    }

    public static PageResult findPage(
            PageRequest pageRequest, Object mapper, String methodName, Object... args) {

        Method method = getMethod(mapper.getClass(), methodName, args);
        if (null == method) {
            throw new ServerExceptionServerError(mapper.getClass().getName() + " not found methom:" + methodName);
        }

        com.github.pagehelper.PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows());
        List res = (List) ReflectionUtils.invokeMethod(method, mapper, args);

        PageInfo pageInfo = new PageInfo(res);
        PageResult pageResult = new PageResult();
        pageResult.setPage(pageInfo.getPageNum());
        pageResult.setRows(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }

    private static Method getMethod(Class<?> clazz, String methodName, Object[] params) {
        Class<?> paramTypes[] = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        Method method = ReflectionUtils.findMethod(clazz, methodName, paramTypes);
        return method;
    }
}
