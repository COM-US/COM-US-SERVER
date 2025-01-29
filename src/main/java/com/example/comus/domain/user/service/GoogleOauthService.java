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
public class GoogleOauthService {
    private final UserRespository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public Long googleLogin(String code) {
        //Oauth 토큰
        String accessToken = getOauthToken(code);

        //유저 정보
        JsonNode userResourceNode = getUserResource(accessToken);
        String name = userResourceNode.get("name").asText();
        String imageUrl = userResourceNode.get("picture").asText();
        User user = saveMember(name, imageUrl);
        return user.getId();
    }

    // Oauth 토큰 가져오기
    public String getOauthToken(String code) {
        String tokenUri = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(tokenUri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
        });
        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.containsKey("access_token")) {
            return (String) responseBody.get("access_token");
        } else {
            throw new IllegalStateException("ERROR: Failed to retrieve access token");
        }
    }

    // 유저 정보 가져오기
    public JsonNode getUserResource(String accessToken) {
        String apiUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("ERROR : Failed to retrieve user resource: " + responseEntity.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userResourceNode;
        try {
            userResourceNode = objectMapper.readTree(responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ERROR : Failed to parse user resource response", e);
        }
        return userResourceNode;
    }

    // 유저 저장
    public User saveMember(String name, String imageUrl) {
        User existUser = userRepository.findByNameAndSocialType(name, SocialType.GOOGLE);
        if (existUser == null) {
            User user = User.builder()
                    .socialType(SocialType.GOOGLE)
                    .name(name)
                    .imageUrl(imageUrl)
                    .build();
            userRepository.save(user);
            return user;
        }
      return existUser;
    }
}
