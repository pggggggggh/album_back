package com.pgh.album_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
