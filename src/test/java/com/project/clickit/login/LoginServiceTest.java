package com.project.clickit.login;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.InvalidIdException;
import com.project.clickit.exceptions.login.InvalidPasswordException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.service.LoginService;
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

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""
                
                \tgiven
                \t  ┣ id = {}
                \t  ┗ memberRepository.existsById(id) = {}
                """, id, false);
            // when
            Boolean result = loginService.isExist(id);

            log.info("""
                
                \twhen
                \t  ┗ result = {}
                """, result);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isFalse(),
                    () -> assertThat(result).isInstanceOf(Boolean.class)
            );

            log.info("""
                
                \tthen
                \t  ┣ assertThat(result).isNotNull()
                \t  ┣ assertThat(result).isFalse()
                \t  ┗ assertThat(result).isInstanceOf(Boolean.class)
                """);
        }

        @Test
        @Order(2)
        @DisplayName("isExist (중복된 아이디)")
        void isExistTestWithDuplicatedId(){
            log.info("isExist Test (중복된 아이디)");
            // given
            String id = "test_member_id";

            given(memberRepository.existsById(id)).willReturn(true);

            log.info("""
                
                \tgiven
                \t  ┣ id = {}
                \t  ┗ memberRepository.existsById(id) = {}
                """, id, true);
            // when
            Boolean result = loginService.isExist(id);

            log.info("""
                
                \twhen
                \t  ┗ result = {}
                """, result);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isTrue(),
                    () -> assertThat(result).isInstanceOf(Boolean.class)
            );

            log.info("""
                
                \tthen
                \t  ┣ assertThat(result).isNotNull()
                \t  ┣ assertThat(result).isTrue()
                \t  ┗ assertThat(result).isInstanceOf(Boolean.class)
                """);
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

            String role = TYPE_STUDENT;

            given(memberRepository.existsById(memberDTO.getId())).willReturn(false);
            given(jwtProvider.createAccessToken(memberDTO.getId(), Collections.singletonList(role))).willReturn("access_token");
            given(jwtProvider.createRefreshToken(memberDTO.getId(), Collections.singletonList(role))).willReturn("refresh_token");

            when(memberRepository.save(any(MemberEntity.class))).thenReturn(memberDTO.toEntity());

            log.info("""
                    \tgiven
                    \t  ┣ MemberDTO
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┣ password = {}
                    \t  ┃  ┣ name = {}
                    \t  ┃  ┣ email = {}
                    \t  ┃  ┣ phone = {}
                    \t  ┃  ┣ studentNum = {}
                    \t  ┃  ┣ dormitoryDTO
                    \t  ┃  ┃  ┣ id = {}
                    \t  ┃  ┃  ┗ name = {}
                    \t  ┣ role = {}
                    \t  ┣ given(memberRepository.existsById(memberDTO.getId())).willReturn({})
                    \t  ┣ given(jwtProvider.createAccessToken(memberDTO.getId(), Collections.singletonList(role))).willReturn("access_token")
                    \t  ┣ given(jwtProvider.createRefreshToken(memberDTO.getId(), Collections.singletonList(role))).willReturn("refresh_token")
                    \t  ┗ when(memberRepository.save(any(MemberEntity.class))).thenReturn(memberDTO.toEntity())
                    """, memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(), memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(), dormitoryDTO.getId(), dormitoryDTO.getName(), role, false);
            // when
            TokenDTO result = loginService.signUp(memberDTO, role);

            log.info("""
                    \twhen
                    \t  ┗ TokenDTO result = loginService.signUp(memberDTO, role)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(TokenDTO.class)
            );

            log.info("""
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(TokenDTO.class)
                    """);
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

            given(memberRepository.existsById(memberDTO.getId())).willReturn(true);

            log.info("""
                    \tgiven
                    \t  ┣ MemberDTO
                    \t  ┃  ┗ id = {}
                    \t  ┣ given(memberRepository.existsById(memberDTO.getId())).willReturn({})
                    """, memberDTO.getId(), true);
            // when
            Throwable result = catchThrowable(() -> loginService.signUp(memberDTO, TYPE_STUDENT));

            log.info("""
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> loginService.signUp(memberDTO, TYPE_STUDENT))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class)
            );

            log.info("""
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(DuplicatedIdException.class)
                    """);
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
                    .id("test_member_id")
                    .password("test_member_password")
                    .build();

            given(memberRepository.existsById(loginDTO.getId())).willReturn(true);
            given(memberRepository.findById(loginDTO.getId())).willReturn(MemberEntity.builder()
                    .id("test_member_id")
                    .password("test_member_password")
                    .type(TYPE_STUDENT)
                    .build());

            when(jwtProvider.createAccessToken(loginDTO.getId(), Collections.singletonList(TYPE_STUDENT))).thenReturn("access_token");
            when(jwtProvider.createRefreshToken(loginDTO.getId(), Collections.singletonList(TYPE_STUDENT))).thenReturn("refresh_token");

            log.info("""
                    \tgiven
                    \t  ┣ LoginDTO
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┗ password = {}
                    \t  ┣ given(memberRepository.existsById(loginDTO.getId())).willReturn({})
                    \t  ┣ given(memberRepository.findById(loginDTO.getId())).willReturn(MemberEntity.builder().id("test_member_id").password("test_member_password").build())
                    \t  ┣ when(jwtProvider.createAccessToken(loginDTO.getId(), Collections.singletonList(TYPE_STUDENT))).thenReturn("access_token")
                    \t  ┗ when(jwtProvider.createRefreshToken(loginDTO.getId(), Collections.singletonList(TYPE_STUDENT))).thenReturn("refresh_token")
                    """, loginDTO.getId(), loginDTO.getPassword(), true);
            // when
            TokenDTO result = loginService.signIn(loginDTO);

            log.info("""
                    \twhen
                    \t  ┗ TokenDTO result = loginService.signIn(loginDTO)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(TokenDTO.class)
            );

            log.info("""
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(TokenDTO.class)
                    """);
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

            given(memberRepository.existsById(loginDTO.getId())).willReturn(false);

            log.info("""
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┗ given(memberRepository.existsById({}})).willReturn({})
                    """, loginDTO.getId(), loginDTO.getId(), false);
            // when
            Throwable result = catchThrowable(() -> loginService.signIn(loginDTO));

            log.info("""
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> loginService.signIn(loginDTO))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(InvalidIdException.class)
            );

            log.info("""
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(InvalidIdException.class)
                    """);
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

            given(memberRepository.existsById(loginDTO.getId())).willReturn(true);
            given(memberRepository.findById(loginDTO.getId())).willReturn(MemberEntity.builder()
                    .id("test_member_id")
                    .password("test_member_password")
                    .type(TYPE_STUDENT)
                    .build());

            log.info("""
                    \tgiven
                    \t  ┣ LoginDTO
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┗ password = {}
                    \t  ┣ given(memberRepository.existsById(loginDTO.getId())).willReturn({})
                    \t  ┗ given(memberRepository.findById(loginDTO.getId())).willReturn(MemberEntity.builder().id("test_member_id").password("test_member_password").build())
                    """, loginDTO.getId(), loginDTO.getPassword(), true);
            // when
            Throwable result = catchThrowable(() -> loginService.signIn(loginDTO));

            log.info("""
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> loginService.signIn(loginDTO))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(InvalidPasswordException.class)
            );

            log.info("""
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(InvalidPasswordException.class)
                    """);
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

            given(jwtProvider.resolveToken(token)).willReturn(token);
            given(jwtProvider.validateToken(token)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ token = {}
                    \t  ┣ SecurityContext securityContext = new SecurityContextImpl()
                    \t  ┣ securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"))
                    \t  ┣ SecurityContextHolder.setContext(securityContext)
                    \t  ┣ given(jwtProvider.resolveToken({}})).willReturn({}})
                    \t  ┗ given(jwtProvider.validateToken({}})).willReturn(true)
                    """, token, token, token, token);
            // when
            loginService.logout(token);

            log.info("""
                    
                    \twhen
                    \t  ┗ loginService.logout(token)
                    """);
            // then
            assertAll(
                    () -> assertThat(SecurityContextHolder.getContext()).isNotNull(),
                    () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(SecurityContextHolder.getContext()).isNotNull()
                    \t  ┗ assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull()
                    """);
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

            given(jwtProvider.resolveToken(token)).willReturn(token);
            given(jwtProvider.validateToken(token)).willThrow(RuntimeException.class);

            log.info("""
                    
                    \tgiven
                    \t  ┣ token = {}
                    \t  ┣ SecurityContext securityContext = new SecurityContextImpl()
                    \t  ┣ securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"))
                    \t  ┣ SecurityContextHolder.setContext(securityContext)
                    \t  ┣ given(jwtProvider.resolveToken({}})).willReturn({}})
                    \t  ┗ given(jwtProvider.validateToken({}})).willThrow(RuntimeException.class)
                    """, token, token, token, token);
            // when
            Throwable result = catchThrowable(() -> loginService.logout(token));

            log.info("""
                    
                    \twhen
                    \t  ┗ loginService.logout(token)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(RuntimeException.class),
                    () -> assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull()

            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(RuntimeException.class)
                    \t  ┗ assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull()
                    """);
        }
    }
}
