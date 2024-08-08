package com.pgh.album_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
