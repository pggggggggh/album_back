package com.pgh.album_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponseDTO {
    private String username;
    private String nickname;
    private String role;
}
