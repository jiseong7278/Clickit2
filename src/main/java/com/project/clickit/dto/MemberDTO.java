package com.project.clickit.dto;

import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    public MemberDTO(String id, String password,
                     String name, String email, String phone, String studentNum){
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.studentNum = studentNum;
    }

    @Builder
    public MemberDTO(String id, String password,
                     String name, String email, String phone, String studentNum,
                     String type, String refreshToken, DormitoryDTO dormitoryDTO){
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.studentNum = studentNum;
        this.type = type;
        this.refreshToken = refreshToken;
        this.dormitoryDTO = dormitoryDTO;
    }

    public MemberEntity toEntity(){
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
//                .dormitoryEntity(DormitoryEntity.builder()
//                        .id(this.dormitoryId)
//                        .build())
                .build();
    }
}
