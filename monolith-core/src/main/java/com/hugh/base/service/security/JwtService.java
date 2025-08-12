package com.hugh.base.service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Base64;

/**
 * JWT helper: sign and parse tokens. Secret should be provided via configuration.
 */
public class JwtService {

    private final Key key;
    private final long ttlMs;

    public JwtService(String base64Secret, long ttlMs) {
        byte[] decoded = Base64.getDecoder().decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(decoded);
        this.ttlMs = ttlMs;
    }

    public String sign(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlMs))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
