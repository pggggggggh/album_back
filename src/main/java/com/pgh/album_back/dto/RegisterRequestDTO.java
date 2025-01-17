package com.pgh.album_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String nickname;

    @NotNull
    private String password;
}
