package com.project.clickit.dto;

import com.project.clickit.entity.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String password;

    @Builder
    public LoginDTO(String id, String password){
        this.id = id;
        this.password = password;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .id(this.id)
                .password(this.password)
                .build();
    }
}
