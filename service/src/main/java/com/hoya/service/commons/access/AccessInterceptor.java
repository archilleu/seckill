package com.hoya.service.commons.access;

/**
 * 用户限流，避免一直刷新导致服务器压力增大
 */

import com.alibaba.fastjson.JSON;
import com.hoya.core.exception.ServerError;
import com.hoya.service.annotation.AccessLimit;
import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.commons.redis.impl.AccessKeyPrefix;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.MiaoShaUserService;
import com.hoya.service.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Optional;

import static com.hoya.service.constant.CustomerConstant.COOKIE_NAME_TOKEN;

@Slf4j
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @Autowired
    RedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        log.info("打印拦截方法handler ：{} ", handler);

        HandlerMethod hm = (HandlerMethod) handler;

        MiaoShaUser user = getUser(request);
        UserContext.setUser(user);
        AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
        if (accessLimit == null) {
            return true;
        }
        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        boolean needLogin = accessLimit.needLogin();
        String key = request.getRequestURI();
        if (needLogin) {
            if (user == null) {
                render(response, new ServerError(403, "用户未登录"));
                return false;
            }
            key += "_" + user.getNickname();
        } else {
            //do nothing
        }

        // 限流判断
        AccessKeyPrefix ak = AccessKeyPrefix.withExpire(seconds);
        Integer count = redisClient.get(ak, key, Integer.class);
        if (count == null) {
            redisClient.set(ak, key, 1);
        } else if (count < maxCount) {
            redisClient.incr(ak, key);
        } else {
            render(response, new ServerError(403, "当前访问过多"));
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        UserContext.removeUser();
    }

    private void render(HttpServletResponse response, ServerError e) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        OutputStream out = response.getOutputStream();
        out.write(JSON.toJSONString(e).getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoShaUser getUser(HttpServletRequest request) {
        String token = getCookieToken(request);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(COOKIE_NAME_TOKEN);
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        return miaoShaUserService.getByToken(token);
    }

    private String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> val = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(COOKIE_NAME_TOKEN)).findFirst();
        if (val.isPresent()) {
            return val.get().getValue();
        }

        return "";
    }
}
