package com.project.clickit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDTO {
    private Long memberNum;
    private String id;
    private String password;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenDTO(Long memberNum, String id, String password, String accessToken, String refreshToken){
        this.memberNum = memberNum;
        this.id = id;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
