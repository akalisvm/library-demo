package com.example.demo.common;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        //获取请求头中的令牌
        String token = request.getHeader("token");
        Result<?> result;

        try {
            //验证令牌
            return JwtUtils.verifyToken(token);
        } catch (SignatureVerificationException e) {
            result = Result.error("-1", "无效签名");
        } catch (TokenExpiredException e) {
            result = Result.error("-1", "token过期");
        } catch (AlgorithmMismatchException e) {
            result = Result.error("-1", "token算法不一致");
        } catch (Exception e) {
            result = Result.error("-1", "token无效");
        }

        //将Result转为json
        String json = new ObjectMapper().writeValueAsString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}

