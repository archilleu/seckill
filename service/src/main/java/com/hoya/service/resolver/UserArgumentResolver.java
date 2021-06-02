package com.hoya.service.resolver;

/**
 * 用户登陆Bean参数解析器
 */

import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.MiaoShaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.hoya.service.constant.CustomerConstant.COOKIE_NAME_TOKEN;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoShaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String token = getCookieToken(request);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(COOKIE_NAME_TOKEN);
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        MiaoShaUser user = miaoShaUserService.getByToken(token);
        return user;
    }

    private String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookieValue = Arrays.stream(cookies).filter(cookie -> cookie.equals(COOKIE_NAME_TOKEN)).findFirst().get();
        if (null == cookieValue) {
            return "";
        }
        return cookieValue.getValue();
    }
}
