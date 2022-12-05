package com.example.demo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 * JWT token格式：header.payload.signature
 * header的格式：算法、token的类型-{"alg": "HS512", "typ": "JWT"}
 * payload的格式：用户名、创建时间、生成时间-{"sub": "wang", "created": 1489079981393, "exp": 1489684781}
 * signature生成算法：HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by YuanJW on 2022/7/26.
 */
@Slf4j
@Component
public class JwtTokenUtils {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 根据用户信息生成token
     * @param sub
     * @return
     */
    public String generateToken(String sub) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, sub);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断token在指定时间内是否刷新过
     * @param token
     * @param time
     * @return
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        // 刷新时间在创建时间的指定时间内
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time))) {
            return true;
        }
        return false;
    }

    /**
     * 当原来的token没有过期时可以刷新
     * @param oldToken 带有tokenHead的token
     * @return
     */
    public String refreshHeadToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if(StrUtil.isEmpty(token)){
            return null;
        }
        Claims claims = getClaimsFromToken(token);
        // token校验失败
        if (claims == null) {
            return null;
        }
        // 如果token已经过期，不支持刷新
        if (!isTokenExpired(token)) {
            return null;
        }
        // 如果token在30分钟之内刷新过，返回原token
        if (tokenRefreshJustBefore(token, 30*60)) {
            return token;
        } else {
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }

    /**
     * 根据用户信息生成JWT的token
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token的过期时间
     * @return Date : 系统当前时间 + expiration * 1000
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取JWT中的用户信息
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败：{}", token);
        }
        return claims;
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.info("获取用户名失败：{}", token);
        }
        return username;
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否已经有效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getExpiredDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 验证token是否有效
     * @param token 客户端传入token
     * @param sub 从数据库中查询出来的用户信息
     * @return
     */
    public boolean validateToken(String token, String sub) {
        String username =getUsernameFromToken(token);
        return username.equals(sub) && !isTokenExpired(token);
    }
}
