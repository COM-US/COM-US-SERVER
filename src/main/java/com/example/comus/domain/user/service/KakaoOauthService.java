package com.example.comus.domain.user.service;

import com.example.comus.domain.user.entity.SocialType;
import com.example.comus.domain.user.entity.User;
import com.example.comus.domain.user.repository.UserRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class KakaoOauthService {
    private final UserRespository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public Long kakaoLogin(String code) {
        try {
            // OAuth 토큰 요청
            String accessToken = getOauthToken(code);

            // 사용자 정보 요청
            JsonNode userResourceNode = getUserResource(accessToken);
            String name = userResourceNode.path("kakao_account").path("profile").path("nickname").asText();
            String imageUrl = userResourceNode.path("kakao_account").path("profile").path("profile_image_url").asText();

            User user = saveMember(name, imageUrl);
            return user.getId();
        } catch (Exception e) {
            throw new RuntimeException("카카오 로그인 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private String getOauthToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                tokenUri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.containsKey("access_token")) {
            return (String) responseBody.get("access_token");
        } else {
            throw new IllegalStateException("ERROR: Failed to retrieve access token");
        }
    }

    private JsonNode getUserResource(String accessToken) {
        String apiUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("ERROR: Failed to retrieve user resource: " + responseEntity.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ERROR: Failed to parse user resource response", e);
        }
    }


    public User saveMember(String name, String imageUrl) {
        User existUser = userRepository.findByNameAndSocialType(name, SocialType.KAKAO);
        if (existUser == null) {
            User user = User.builder()
                    .socialType(SocialType.KAKAO)
                    .name(name)
                    .imageUrl(imageUrl)
                    .build();
            userRepository.save(user);
            return user;
        }
        return existUser;
    }
}
