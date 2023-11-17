package com.project.clickit.member.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_num")
    private Integer memberNum;

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
    private String type; // DEV, STAFF, STUDENT

    @Column(name = "member_refresh_token")
    private String refreshToken; // 리프레시 토큰

    @Column(name = "member_dormitory")
    private Integer dormitoryNum; // 기숙사 번호

    public MemberEntity(String password, String refreshToken) {
        this.password = password;
        this.refreshToken = refreshToken;
    }
}
