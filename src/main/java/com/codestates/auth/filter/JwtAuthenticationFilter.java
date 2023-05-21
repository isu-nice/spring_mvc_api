package com.codestates.auth.filter;

import com.codestates.auth.dto.LoginDto;
import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 클라이언트의 로그인 인증 요청을 처리하는 엔트리 포인트 역할
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // 로그인 정보를 전달받아 UserDetailsService와 인터랙션 한 뒤 인증 여부 판단
    private final AuthenticationManager authenticationManager;
    // 클라이언트가 인증에 성공할 경우, JWT를 생성 및 발급함
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Username과 Password 역직렬화를 위해
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        // 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 토큰을 AuthenticationManager에게 전달하며 인증 처리를 위임
        return authenticationManager.authenticate(authenticationToken);
    }

    // 클라이언트의 인증 정보를 이용해 인증에 성공할 경우 호출됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        // Member 엔티티 클래스의 객체를 얻음
        Member member = (Member) authResult.getPrincipal();

        // access token 생성
        String accessToken = delegateAccessToken(member);
        // refresh token 생성
        String refreshToken = delegateRefreshToken(member);

        /*
         response 헤더(Authorization)에 access token 추가
         access token은 클라이언트 측에서 백엔드 애플리케이션 측에 요청을 보낼 때마다
         request 헤더에 추가해서 클라이언트 측의 자격을 증명하는 데 사용됨
         */
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        /*
         response 헤더(Refresh)에 refresh token 추가
         refresh token은 access token이 만료될 경우,
         클라이언트 측이 access token을 새로 발급받기 위해 클라이언트에게 추가적으로 제공될 수 있으며,
         refresh token을 access token과 함께 제공할지 여부는 요구 사항에 따라 달라질 수 있다.
         */
        response.setHeader("Refresh", refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
//        claims.put("memberId", member.getMemberId()); // 식별자도 포함할 수 있다.
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer. getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        return refreshToken;
    }
}
