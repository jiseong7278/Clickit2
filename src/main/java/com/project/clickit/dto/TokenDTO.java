package com.project.clickit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDTO {
    private String id;
    private String password;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenDTO(String id, String password, String accessToken, String refreshToken){
        this.id = id;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
