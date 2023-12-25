package com.project.clickit.member.dto;

import com.project.clickit.member.domain.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long memberNum;
    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String studentNum;
    private String type;
    private String refreshToken;
    private Long dormitoryNum;

    @Builder
    public MemberDTO(Long memberNum, String id, String password){
        this.memberNum = memberNum;
        this.id = id;
        this.password = password;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .memberNum(this.memberNum)
                .id(this.id)
                .password(this.password)
                .build();
    }
}
