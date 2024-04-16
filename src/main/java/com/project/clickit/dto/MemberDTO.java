package com.project.clickit.dto;

import com.project.clickit.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String studentNum;
    private String type;
    private String refreshToken;
    private DormitoryDTO dormitoryDTO;

    /**
     * <b>MemberEntity로 변환</b>
     * @return MemberEntity
     */
    public MemberEntity toEntity(){
        if (this.dormitoryDTO == null)
            return MemberEntity.builder()
                    .id(this.id)
                    .password(this.password)
                    .name(this.name)
                    .email(this.email)
                    .phone(this.phone)
                    .studentNum(this.studentNum)
                    .type(this.type)
                    .refreshToken(this.refreshToken)
                    .build();
        return MemberEntity.builder()
                .id(this.id)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .studentNum(this.studentNum)
                .type(this.type)
                .refreshToken(this.refreshToken)
                .dormitoryEntity(this.dormitoryDTO.toEntity())
                .build();
    }
}
