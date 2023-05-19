package com.codestates.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenizer {
    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    // plain text 형태인 secret key의 byte[]를 base64 형식의 문자열로 인코딩
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 인증된 사용자에게 JWT를 최초로 발급해주기 위한 JWT 생성 메서드
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        // base64 형식의 비밀키 문자열을 이용해 Key 객체를 얻음
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims) // 인증된 사용자와 관련된 정보 추가
                .setSubject(subject) // JWT 제목 추가
                .setIssuedAt(Calendar.getInstance().getTime()) // JWT 발행 일자 설정
                .setExpiration(expiration) // 만료일시 지정
                .signWith(key) // 서명
                .compact(); // JWT 생성, 직렬화
    }

    // access 토큰이 만료된 경우 새로 생성할 수 있게 해주는 refresh token 생성하는 메서드
    // custom claim 은 추가할 필요 없다
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
        return claims;
    }

    // JWT Signature 검증 기능 -> 위/변조 여부 확인
    // 'jws' 는 Signature가 포함된 JWT 라는 의미
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key) // 서명에 사용된 비밀키 설정
                .build()
                .parseClaimsJws(jws); // JWT 파싱해서 claims 얻음
    }

    // JWT 만료 일시를 지정하기 위한 메서드 -> JWT 생성 시 사용
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    // JWT 서명에 사용할 secret key 생성
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        // base64 형식으로 인코딩된 secret key를 디코딩한 후 byte[]를 리턴함
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        // keyBytes[]를 기반으로 적절한 HMAC 알고리즘을 적용한 Key 객체를 생
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }
}
