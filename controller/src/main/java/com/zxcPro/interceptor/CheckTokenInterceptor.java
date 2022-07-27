package com.zxcPro.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcPro.vo.ResStatus;
import com.zxcPro.vo.ResultVO;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

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
        if (token == null) {
            ResultVO resultVO = new ResultVO(ResStatus.NO, "请先登录", null);
            doResponse(response, resultVO);
            return false;
        }
        else {
            //在redis进行获取用户信息，而不是使用jwt去校验密钥了
            String userInfoString = stringRedisTemplate.boundValueOps(token).get();
            if (userInfoString == null) {
                ResultVO resultVO = new ResultVO(ResStatus.NO, "请重新登录，登录过期", null);
                doResponse(response, resultVO);
                return false;
            }
            else {
                //登录成功，给token的redis续命
                stringRedisTemplate.boundValueOps(token).expire(30, TimeUnit.MINUTES);
                return true;
            }
//            try {
//                JwtParser parser = Jwts.parser();
//                parser.setSigningKey("zxc666");//密钥一致
//                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
//                return true;
//            } catch (ExpiredJwtException e) {
//                ResultVO resultVO = new ResultVO(ResStatus.NO, "登录过期，请重新登录!", null);
//                doResponse(response, resultVO);
//            } catch (UnsupportedJwtException e) {
//                ResultVO resultVO = new ResultVO(ResStatus.NO, "token 不合法!", null);
//                doResponse(response, resultVO);
//            } catch (Exception e) {
//                ResultVO resultVO = new ResultVO(ResStatus.NO, "请登录!", null);
//                doResponse(response, resultVO);
//            }
//            return false;
        }
    }

    private void doResponse(HttpServletResponse response, ResultVO resultVO) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String s = new ObjectMapper().writeValueAsString(resultVO);
        out.print(s);
        out.flush();
        out.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
