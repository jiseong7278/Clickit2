package com.project.clickit.login;

import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
public class LoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private LoginService loginService;

    @Value("${roles.student}")
    private String TYPE_STUDENT;

    private LoginDTO loginDTO;

    private LoginDTO loginDTO2;

    private MemberDTO memberDTO;

    @BeforeEach
    void setUp(){
        loginDTO = LoginDTO.builder()
                .id("test_member_id")
                .password("test_member_password")
                .build();

        loginDTO2 = LoginDTO.builder()
                .id("never_used_id")
                .password("0000")
                .build();

        memberDTO = MemberDTO.builder()
                .id("111")
                .password("222")
                .name("333")
                .email("444")
                .phone("555")
                .studentNum("666")
                .build();
    }

    @Test
    @DisplayName("중복 체크 테스트")
    void duplicateCheckTest(){
        log.info("아이디 중복 테스트");
        // given
        given(memberRepository.existsById(loginDTO.getId())).willReturn(true);

        // when
        Boolean isExist = loginService.duplicateCheck(loginDTO.getId());
        log.info("예상: true, 결과: " + isExist);

        // then
        assertThat(isExist).isTrue();
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("중복 체크 테스트(중복되지 않은 아이디)")
    void duplicateCheckTest2(){
        log.info("아이디 중복 테스트");
        // given
        given(memberRepository.existsById(loginDTO2.getId())).willReturn(false);

        // when
        Boolean isExist = loginService.duplicateCheck(loginDTO2.getId());
        log.info("예상: false, 결과: " + isExist);

        // then
        assertThat(isExist).isFalse();
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest(){
        log.info("회원가입 테스트 시작");
        // given
        given(memberRepository.existsById(memberDTO.getId())).willReturn(false);
        given(memberRepository.save(any(MemberEntity.class))).willReturn(null);

        given(jwtProvider.createAccessToken(any(String.class), any())).willReturn("access_token");

        // when
        TokenDTO tokenDTO = loginService.signUp(memberDTO, TYPE_STUDENT);

        // then
        assertThat(tokenDTO).isNotNull();
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("회원가입 중복된 아이디")
    void signUpTestFailed(){
        log.info("회원가입 테스트 시작");
        // given
        MemberDTO duplicatedMemberDTO = MemberDTO.builder()
                .id("test_member_id")
                .password("222")
                .name("333")
                .email("444")
                .phone("555")
                .studentNum("666")
                .build();

        when(memberRepository.existsById(duplicatedMemberDTO.getId())).thenReturn(true);

        // when
        assertThatThrownBy(() -> loginService.signUp(duplicatedMemberDTO, TYPE_STUDENT))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 가입된 아이디입니다.");

        // then
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest(){
        log.info("로그인 테스트 시작");
        // given
        given(memberRepository.existsById(loginDTO.getId())).willReturn(true);

        MemberEntity memberEntity = MemberEntity.builder()
                .id(loginDTO.getId())
                .password(loginDTO.getPassword())
                .build();

        given(memberRepository.findById(loginDTO.getId())).willReturn(memberEntity);

        given(jwtProvider.createAccessToken(any(String.class), any())).willReturn("access_token");
        given(jwtProvider.createRefreshToken(any(String.class), any())).willReturn("refresh_token");

        // when
        TokenDTO tokenDTO = loginService.signIn(loginDTO);

        // then
        assertThat(tokenDTO).isNotNull();
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginTestFailed(){
        log.info("로그인 실패 테스트 시작");
        // given
        given(memberRepository.existsById(loginDTO.getId())).willReturn(false);

        // when
        assertThatThrownBy(() -> loginService.signIn(loginDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이디가 존재하지 않습니다.");

        // then
        log.info("테스트 종료");
    }
}
