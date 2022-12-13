package com.codestates.auth.filter;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.auth.utils.CustomAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

// 클라이언트 측에서 전송된 request header에 포함된 JWT 검증을 위한 필터
// request 당 한 번 수행하므로 OncePerRequestFilter 이용
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer; // JWT 검증, Claims 얻는 데 사용
    private final CustomAuthorityUtils authorityUtils;
    // 검증 성공 시 Authentication 객체에 채울 사용자의 권한을 생성

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer,
                                 CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (SignatureException signatureException) {
            request.setAttribute("exception", signatureException);
        } catch (ExpiredJwtException expiredJwtException) {
            request.setAttribute("exception", expiredJwtException);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        // 검증에 성공하고, SecurityContext에 Authentication을 저장한 후,
        // 다음 Security Filter를 호출
        filterChain.doFilter(request, response);
    }

    // 특정 조건이 true이면 해당 필터의 동작을 수행하지 않고 다음 필터로 건너뛰도록 해줌
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Authorization header 값 얻음
//        String authorization = request.getHeader("Authorization");
        String authorization = request.getHeader(AUTHORIZATION);
        // 헤더 값이 null이거나 Bearer로 시작하지 않는다면 해당 필터의 동작을 수행하지 않는다,
        // -> JWT가 헤더에 포함되지 않은 경우를 의미한다.
        return authorization == null || !authorization.startsWith("Bearer ");
    }

    // JWT 검증
    private Map<String, Object> verifyJws(HttpServletRequest request) {
        // request header 에서 JWT 얻음
        // jws = Json Web Token Signed
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer", "");
        // 비밀키 얻음
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        // JWT에서 claims 파싱 성공 -> signature 검증에 성공했다는 의미
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
        return claims;
    }

    // Authentication 객체를 SecurityContext에 저장하기 위한 메서드
    private void setAuthenticationToContext(Map<String, Object> claims) {
        // JWT에서 파싱한 claims에서 username 얻음
        String username = (String) claims.get("username");
        // JWT의 claims에서 얻은 권한 정보를 기반으로 List<GrantedAuthority> 생성 (권한 부여)
        List<GrantedAuthority> authorities =
                authorityUtils.createAuthorities((List) claims.get("roles"));

        // detail 넣어줌
        // Authentication 객체 생성
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        // SecurityContext에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
