package com.zxcPro.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 当访问静态页面时，也需要将登录认证的redis延长加时时间
 */
@Component
public class SetTimeForAllInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        //浏览器的预检机制
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        String token = request.getHeader("token");

        if (token != null) {
            String s = stringRedisTemplate.boundValueOps(token).get();
            if (s != null) {
                stringRedisTemplate.boundValueOps(token).expire(30, TimeUnit.MINUTES);
            }
        }

        return true;//只是通过访问静态页面增加用户登录的认证时间
    }

}
