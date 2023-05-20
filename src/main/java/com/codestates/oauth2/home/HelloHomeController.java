package com.codestates.oauth2.home;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloHomeController {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public HelloHomeController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    // V5 - OAuth2 인증 후, Access Token 얻는 방법
    @GetMapping("/hello-oauth2")
    public String home(Authentication authentication) {
        // 내부적으로 OAuth2AuthorizedClientRepository 에서 OAuth2AuthorizedClient를 조회함
        var authorizedClient = authorizedClientService
                .loadAuthorizedClient("google", authentication.getName());

        // access token 을 얻음
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value = " + accessToken.getTokenValue());
        System.out.println("Access Token Type = " + accessToken.getTokenType());
        System.out.println("Access Token Scopes = " + accessToken.getScopes());
        System.out.println("Access Token Issued At = " + accessToken.getIssuedAt());
        System.out.println("Access Token Expires At = " + accessToken.getExpiresAt());

        return "hello-oauth2";
    }

    /* V2 - SecurityContext 이용하는 방법
    @GetMapping("/hello-oauth2")
    public String home() {
        // SecurityContext에서 인증된 Authentication 객체를 통해 principal 객체를 얻음
        var oAuth2User = (OAuth2User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // 사용자의 이메일 정보를 얻음
        // -> 이메일 주소가 출력된다면 OAuth2 인증 성공
        System.out.println(oAuth2User.getAttributes().get("email"));
        return "hello-oauth2";
    }*/

   /* V3 - 인증된 사용자 정보 얻기
   @GetMapping("/hello-oauth2")
    public String home(Authentication authentication) { // 인증된 Authentication을 파라미터로 받음
        var oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(oAuth2User);
        System.out.println("User's email in Google: " + oAuth2User.getAttributes().get("email"));

        return "hello-oauth2";
    }*/

  /*  V4 - OAuth2User 를 파라미터로 직접 전달받음
    @GetMapping("/hello-oauth2")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User) {

        System.out.println("User's email in Google: " + oAuth2User.getAttributes().get("email"));
        return "hello-oauth2";
    }*/

    /* V6 - OAuth2AuthorizedClient를 핸들러 메서드의 파라미터로 전달받는 방법
    @GetMapping("/hello-oauth2")
    public String home(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value = " + accessToken.getTokenValue());
        System.out.println("Access Token Type = " + accessToken.getTokenType());
        System.out.println("Access Token Scopes = " + accessToken.getScopes());
        System.out.println("Access Token Issued At = " + accessToken.getIssuedAt());
        System.out.println("Access Token Expires At = " + accessToken.getExpiresAt());

        return "hello-oauth2";
    }*/
}
