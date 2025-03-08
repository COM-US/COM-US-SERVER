package com.example.comus.domain.user.dto.request;

import com.example.comus.domain.user.entity.SocialType;
import com.example.comus.domain.user.entity.User;

public record LoginRequestDto(
      String name,
      String imageUrl,
      SocialType socialType,
      String socialId
) {

    public User toEntity() {
        return User.builder()
                .name(name)
                .imageUrl(imageUrl)
                .socialType(socialType)
                .socialId(socialId)
                .build();
    }
}
