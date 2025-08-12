package com.hugh.base.service.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtUtil {
    private static String SECRET = "ChangeThisSecret";
    private static Algorithm ALGO = Algorithm.HMAC256(SECRET);
    
    // 允许设置密钥的方法，用于从配置文件读取
    public static void setSecret(String secret) {
        SECRET = secret;
        ALGO = Algorithm.HMAC256(SECRET);
    }

    public static String generateToken(String subject, long expiryMillis) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryMillis))
                .sign(ALGO);
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(ALGO).build().verify(token);
    }
    
    public static String getSubject(String token) {
        DecodedJWT jwt = verify(token);
        return jwt.getSubject();
    }
}