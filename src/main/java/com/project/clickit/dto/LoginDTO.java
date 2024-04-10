package com.project.clickit.dto;

import com.project.clickit.entity.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String password;

    /**
     * <b>MemberEntity로 변환</b>
     * @return MemberEntity
     */
    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .id(this.id)
                .password(this.password)
                .build();
    }
}
