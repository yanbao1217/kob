package com.example.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    // 14天
    public static final long JWT_TTL = 60 * 60 * 1000L * 24 * 14;

    // ⚠ 必须 >= 32 字节（HS256 要求 256 bit）
    public static final String JWT_KEY =
            "SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac";

    /**
     * 生成 UUID（无横线）
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 创建 JWT
     */
    public static String createJWT(String subject) {
        return getJwtBuilder(subject, null, getUUID()).compact();
    }

    /**
     * 构建 JWT
     */
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {

        SecretKey secretKey = generalKey();

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        long expMillis = nowMillis + (ttlMillis == null ? JWT_TTL : ttlMillis);
        Date expDate = new Date(expMillis);

        return Jwts.builder()
                .id(uuid)
                .subject(subject)
                .issuer("sg")
                .issuedAt(now)
                .expiration(expDate)
                .signWith(secretKey);
    }

    /**
     * 生成密钥（0.13.0 推荐写法）
     */
    public static SecretKey generalKey() {
        return Keys.hmacShaKeyFor(
                JWT_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 解析 JWT
     */
    public static Claims parseJWT(String jwt) {

        SecretKey secretKey = generalKey();

        return Jwts.parser()
                .verifyWith(secretKey)      // 0.13.0 新写法
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}