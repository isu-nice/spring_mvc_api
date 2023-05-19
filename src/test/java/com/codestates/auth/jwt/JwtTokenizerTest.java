package com.codestates.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenizerTest {
    private static JwtTokenizer jwtTokenizer;
    private String secretKey;
    private String base64EncodedSecretKey;

    // 비밀키 인코딩 후 각 테스트 케이스에서 사용
    @BeforeAll
    public void init() {
        jwtTokenizer = new JwtTokenizer();
        secretKey = "kevin1234123412341234123412341234"; // encoded "a2V2aW4xMjM0MTIzNDEyMzQxMjM0MTIzNDEyMzQxMjM0"

        base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);
    }

    @DisplayName("plain text 인 비밀키가 base64 형식으로 인코딩이 정상적으로 수행되는지 테스트")
    @Test
    public void encodeBase64SecretKeyTest() {
        System.out.println(base64EncodedSecretKey);
        // base64 형식으로 인코딩된 비밀키를 디코딩한 값이
        // 원본 plain text 비밀키와 일치하는지 확인
        assertThat(secretKey, is(new String(Decoders.BASE64.decode(base64EncodedSecretKey))));
    }

    @DisplayName("jwtTokenizer가 access token을 정상적으로 생성하는지 테스트")
    @Test
    public void generatedAccessTokenTest() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = jwtTokenizer
                .generateAccessToken(claims, subject,expiration,base64EncodedSecretKey);

        // JWT는 생성시마다 값이 바뀌므로 우선 생성된 access token이 null이 아닌지 여부만 테스트
        // 생성 과정에서 예외가 발생하지 않았기 때문에 정상적으로 생성되었다고 볼 수 있음
        System.out.println(accessToken);
        assertThat(accessToken, notNullValue());
    }

    @DisplayName("JwtTokenizer가 Refresh Token을 정상적으로 생성하는지 테스트")
    @Test
    public void generatedRefreshTokenTest() {
        String subject = "test refresh token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiration = calendar.getTime();

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        System.out.println(refreshToken);

        assertThat(refreshToken, notNullValue());
    }

    @DisplayName("signature 를 잘 검증하는지 테스트")
    @Test
    public void verifySignatureTest() {
        String accessToken = getAccessToken(Calendar.MINUTE, 10);
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    @DisplayName("JWT가 만료되는지 테스트")
    @Test
    public void verifyExpirationTest() throws InterruptedException {
        String accessToken = getAccessToken(Calendar.SECOND, 1);
        assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));

        TimeUnit.MILLISECONDS.sleep(1500);

        assertThrows(ExpiredJwtException.class,
                () -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
    }

    private String getAccessToken(int timeUnit, int timeAmount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);

        Date expiration = calendar.getTime();
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }
}
