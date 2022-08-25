package com.example.demo.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String sign = "library";

    public static String getToken(Map<String, String> map) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR, 24);
        Date expiresDate = nowTime.getTime();

        JWTCreator.Builder builder = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate);

        map.forEach(builder::withClaim);

        return builder.sign(Algorithm.HMAC256(sign));
    }

    public static boolean verifyToken(String token)  {
        return JWT.require(Algorithm.HMAC256(sign)).build().verify(token) != null;
    }

}
