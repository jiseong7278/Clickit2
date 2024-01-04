package com.project.clickit.entity;

import com.project.clickit.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberEntity {
    @Id
    @Column(name = "member_id")
    private String id; // 로그인 아이디

    @Column(name = "member_password")
    private String password; // 로그인 비밀번호, 해시 암호화

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_email")
    private String email; // 이메일, SALT를 이용한 비밀키(대칭키) 암호화

    @Column(name = "member_phone")
    private String phone; // 전화번호, SALT를 이용한 비밀키(대칭키) 암호화

    @Column(name = "member_student_num")
    private String studentNum; // 학번

    @Column(name = "member_type")
    private String type; // ADMIN, STAFF, STUDENT

    @Column(name = "member_refresh_token")
    private String refreshToken; // 리프레시 토큰

    public MemberEntity(String password, String refreshToken) {
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public MemberDTO toDTO(){
        return MemberDTO.builder()
                .id(this.id)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .studentNum(this.studentNum)
                .type(this.type)
                .refreshToken(this.refreshToken)
                .build();
    }
}
