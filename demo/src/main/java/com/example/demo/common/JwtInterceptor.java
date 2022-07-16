package com.example.demo.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        if(!(handler instanceof HandlerMethod)){
            //所有非controller方法，都放行
            return true;
        }
        String token = request.getHeader("token");
        if(!StringUtils.hasLength(token)) {
            return false;
        }

        Integer userId = JWT.decode(token).getClaim("id").asInt();
        //验证token
        User user = userService.selectById(userId);
        if(user == null) {
            return false;
        }
        //用户密码加签验证
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            verifier.verify(token);
            String username = JwtUtils.getClaimByName(token, "username").asString();
            String role = JwtUtils.getClaimByName(token, "role").asString();
            request.setAttribute("username", username);
            request.setAttribute("role", role);
        } catch (JWTVerificationException e) {
            return false;
        }
        //验证成功
        return true;
    }
}

