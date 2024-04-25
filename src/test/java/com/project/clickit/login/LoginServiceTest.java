package com.project.clickit.login;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.InvalidIdException;
import com.project.clickit.exceptions.login.SignInFailedException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.service.LoginService;
import com.project.clickit.service.RedisService;
import com.project.clickit.util.EmailUtil;
import com.project.clickit.util.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@Slf4j
@DisplayName("LoginService Test")
@ExtendWith({MockitoExtension.class})
public class LoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private SMSUtil smsUtil;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private RedisService redisService;

    private final String TYPE_STUDENT = "CLICKIT_STUDENT";

    @Nested
    @DisplayName("isExist Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IsExist{
        @Test
        @Order(1)
        @DisplayName("isExist Test")
        void isExistTest(){
            log.info("isExist Test");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("isExist Test given: ✔");
            // when
            Boolean result = loginService.isExist(id);

            log.info("isExist Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isFalse(),
                    () -> assertThat(result).isInstanceOf(Boolean.class)
            );

            log.info("isExist Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("isExist - (중복된 아이디)")
        void isExistTestWithDuplicatedId(){
            log.info("isExist Test - (중복된 아이디)");
            // given
            String id = "test_member_id";

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("isExist Test - (중복된 아이디) | given: ✔");
            // when
            Boolean result = loginService.isExist(id);

            log.info("isExist Test - (중복된 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isTrue(),
                    () -> assertThat(result).isInstanceOf(Boolean.class)
            );

            log.info("isExist Test - (중복된 아이디) | then: ✔");
        }
    }

    @Nested
    @DisplayName("signUp Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SignUpTest{
        @Test
        @Order(1)
        @DisplayName("signUp Test")
        void signUpTest(){
            log.info("signUp Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_dormitory_id")
                    .name("test_dormitory_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("id")
                    .password("password")
                    .name("name")
                    .email("email")
                    .phone("phone")
                    .studentNum("20181216")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(false);
            given(jwtProvider.createAccessToken(anyString(), anyList())).willReturn("access_token");
            given(jwtProvider.createRefreshToken(anyString(), anyList())).willReturn("refresh_token");

            given(memberRepository.save(any(MemberEntity.class))).willReturn(memberDTO.toEntity());

            log.info("signUp Test given: ✔");
            // when
            TokenDTO result = loginService.signUp(memberDTO, TYPE_STUDENT);

            log.info("signUp Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(TokenDTO.class)
            );

            log.info("signUp Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("signUp Test(중복된 아이디)")
        void signUpTestWithDuplicatedId(){
            log.info("signUp Test(중복된 아이디)");
            // given
            MemberDTO memberDTO = MemberDTO.builder()
                    .id("test_member_id")
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("signUp Test(중복된 아이디) given: ✔");
            // when
            Throwable result = catchThrowable(() -> loginService.signUp(memberDTO, TYPE_STUDENT));

            log.info("signUp Test(중복된 아이디) when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class)
            );

            log.info("signUp Test(중복된 아이디) then: ✔");
        }

    }

    @Nested
    @DisplayName("signIn Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SignInTest{
        @Test
        @Order(1)
        @DisplayName("signIn Test")
        void signInTest(){
            log.info("signIn Test");
            // given
            LoginDTO loginDTO = LoginDTO.builder()
                    .id("id_12")
                    .password("password_22")
                    .build();

            MemberEntity memberEntity = MemberEntity.builder()
                    .id("id_12")
                    .password("$2a$10$PFBSZO36OS.kE0xUD3Ams.P1NLz8Rv6ZDPgn2UgbY28z5PJgru9W2")
                    .type(TYPE_STUDENT)
                    .build();

//            memberEntity.setPassword("$2a$10$PFBSZO36OS.kE0xUD3Ams.P1NLz8Rv6ZDPgn2UgbY28z5PJgru9W2");

            given(memberRepository.existsById(anyString())).willReturn(true);
            given(memberRepository.findById(anyString())).willReturn(memberEntity);

            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            log.info("passwordEncoder.matches: {}", passwordEncoder.matches(anyString(), anyString()));

            given(jwtProvider.createAccessToken(anyString(), anyList())).willReturn("access_token");
            given(jwtProvider.createRefreshToken(anyString(), anyList())).willReturn("refresh_token");

            log.info("signIn Test given: ✔");
            // when
            TokenDTO result = loginService.signIn(loginDTO);

            log.info("signIn Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(TokenDTO.class)
            );

            log.info("signIn Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("signIn Test (아이디가 존재하지 않음)")
        void signInTestInvalidId(){
            log.info("signIn Test (아이디가 존재하지 않음)");
            // given
            LoginDTO loginDTO = LoginDTO.builder()
                    .id("never_used_id")
                    .password("test_member_password")
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("signIn Test (아이디가 존재하지 않음) given: ✔");
            // when
            Throwable result = catchThrowable(() -> loginService.signIn(loginDTO));

            log.info("signIn Test (아이디가 존재하지 않음) when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(SignInFailedException.class)
            );

            log.info("signIn Test (아이디가 존재하지 않음) then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("signIn Test (비밀번호가 일치하지 않음)")
        void signInTestInvalidPassword(){
            log.info("signIn Test (비밀번호가 일치하지 않음)");
            // given
            LoginDTO loginDTO = LoginDTO.builder()
                    .id("test_member_id")
                    .password("invalid_password")
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(true);
            given(memberRepository.findById(anyString())).willReturn(MemberEntity.builder()
                    .id("test_member_id")
                    .password("test_member_password")
                    .type(TYPE_STUDENT)
                    .build());

            log.info("signIn Test (비밀번호가 일치하지 않음) given: ✔");
            // when
            Throwable result = catchThrowable(() -> loginService.signIn(loginDTO));

            log.info("signIn Test (비밀번호가 일치하지 않음) when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(SignInFailedException.class)
            );

            log.info("signIn Test (비밀번호가 일치하지 않음) then: ✔");
        }
    }

    @Nested
    @DisplayName("sendVerifyCode Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class sendVerifyTest{
        @Test
        @Order(1)
        @DisplayName("sendVerifyCodeBySMS Test")
        void sendVerifyCodeBySMSTest(){
            log.info("sendVerifyCodeBySMS Test");
            // given
            String id = "id";

            MemberEntity memberEntity = MemberEntity.builder()
                    .id("id")
                    .phone("010-1234-5678")
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(true);
            given(memberRepository.findById(anyString())).willReturn(memberEntity);

            willDoNothing().given(smsUtil).sendOne(anyString(), anyString());

            willDoNothing().given(redisService).setData(anyString(), anyString(), any(Duration.class));

            log.info("sendVerifyCodeBySMS Test given: ✔");
            // when
            loginService.sendVerifyCodeBySMS(id);

            log.info("sendVerifyCodeBySMS Test when: ✔");
            // then
            assertAll(
                    () -> assertThatCode(() -> loginService.sendVerifyCodeBySMS(id)).doesNotThrowAnyException()
            );

            log.info("sendVerifyCodeBySMS Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("sendVerifyCodeBySMS Test (아이디가 존재하지 않음)")
        void sendVerifyCodeBySMSTestWithInvalidId(){
            log.info("sendVerifyCodeBySMS Test (아이디가 존재하지 않음)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("sendVerifyCodeBySMS Test (아이디가 존재하지 않음) given: ✔");
            // when
            Throwable result = catchThrowable(() -> loginService.sendVerifyCodeBySMS(id));

            log.info("sendVerifyCodeBySMS Test (아이디가 존재하지 않음) when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(InvalidIdException.class)
            );

            log.info("sendVerifyCodeBySMS Test (아이디가 존재하지 않음) then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("sendVerifyCodeByEmail Test")
        void sendVerifyCodeByEmailTest(){
            log.info("sendVerifyCodeByEmail Test");
            // given
            String id = "id";

            MemberEntity memberEntity = MemberEntity.builder()
                    .id("id")
                    .email("test_member_email")
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(true);
            given(memberRepository.findById(anyString())).willReturn(memberEntity);

            willDoNothing().given(emailUtil).sendEmail(anyString(), anyString(), anyString());

            willDoNothing().given(redisService).setData(anyString(), anyString(), any(Duration.class));

            log.info("sendVerifyCodeByEmail Test given: ✔");
            // when
            loginService.sendVerifyCodeByEmail(id);

            log.info("sendVerifyCodeByEmail Test when: ✔");
            // then
            assertAll(
                    () -> assertThatCode(() -> loginService.sendVerifyCodeByEmail(id)).doesNotThrowAnyException()
            );

            log.info("sendVerifyCodeByEmail Test then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("sendVerifyCodeByEmail Test (아이디가 존재하지 않음)")
        void sendVerifyCodeByEmailTestWithInvalidId(){
            log.info("sendVerifyCodeByEmail Test (아이디가 존재하지 않음)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("sendVerifyCodeByEmail Test (아이디가 존재하지 않음) given: ✔");
            // when
            Throwable result = catchThrowable(() -> loginService.sendVerifyCodeByEmail(id));

            log.info("sendVerifyCodeByEmail Test (아이디가 존재하지 않음) when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(InvalidIdException.class)
            );

            log.info("sendVerifyCodeByEmail Test (아이디가 존재하지 않음) then: ✔");
        }
    }

    @Nested
    @DisplayName("updatePassword Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class updatePasswordTest{
        @Test
        @Order(1)
        @DisplayName("updatePasswordByPhone Test")
        void updatePasswordByPhoneTest(){
            log.info("updatePasswordByPhone Test");
            // given
            String phone = "010-1234-5678";
            String password = "test_password";

            willDoNothing().given(memberRepository).updatePasswordByPhone(anyString(), anyString());

            log.info("updatePasswordByPhone Test given: ✔");
            // when
            loginService.updatePasswordByPhone(phone, password);

            log.info("updatePasswordByPhone Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().updatePasswordByPhone(anyString(), anyString()),
                    () -> assertThatCode(() -> loginService.updatePasswordByPhone(phone, password)).doesNotThrowAnyException()
            );

            log.info("updatePasswordByPhone Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("updatePasswordByEmail Test")
        void updatePasswordByEmailTest(){
            log.info("updatePasswordByEmail Test");
            // given
            String email = "test_member_email";
            String password = "test_password";

            willDoNothing().given(memberRepository).updatePasswordByEmail(anyString(), anyString());

            log.info("updatePasswordByEmail Test given: ✔");
            // when
            loginService.updatePasswordByEmail(email, password);

            log.info("updatePasswordByEmail Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().updatePasswordByEmail(anyString(), anyString()),
                    () -> assertThatCode(() -> loginService.updatePasswordByEmail(email, password)).doesNotThrowAnyException()
            );

            log.info("updatePasswordByEmail Test then: ✔");
        }
    }

    @Nested
    @DisplayName("logout Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Logout{
        @Test
        @Order(1)
        @DisplayName("logout Test")
        void logoutTest(){
            log.info("logout Test");
            // given
            String token = "token";

            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

            given(jwtProvider.resolveToken(anyString())).willReturn(token);
            given(jwtProvider.validateToken(anyString())).willReturn(true);

            log.info("logout Test given: ✔");
            // when
            loginService.logout(token);

            log.info("logout Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(SecurityContextHolder.getContext()).isNotNull(),
                    () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull()
            );

            log.info("logout Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("logout Test (토큰이 유효하지 않음)")
        void logoutTestWithInvalidToken(){
            log.info("logout Test (토큰이 유효하지 않음)");
            // given
            String token = "token";

            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

            given(jwtProvider.resolveToken(anyString())).willReturn(token);
            given(jwtProvider.validateToken(anyString())).willThrow(RuntimeException.class);

            log.info("logout Test (토큰이 유효하지 않음) given: ✔");
            // when
            Throwable result = catchThrowable(() -> loginService.logout(token));

            log.info("logout Test (토큰이 유효하지 않음) when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(RuntimeException.class),
                    () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull()

            );

            log.info("logout Test (토큰이 유효하지 않음) then: ✔");
        }
    }
}
