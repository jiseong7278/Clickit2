package com.project.clickit.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.LoginController;
import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.InvalidIdException;
import com.project.clickit.exceptions.jwt.JWTException;
import com.project.clickit.exceptions.login.SignInFailedException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = LoginController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    @Value("${roles.student}")
    private String TYPE_STUDENT;

    @Nested
    @DisplayName("DuplicateCheck Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DuplicateCheckTest{
        @Test
        @Order(1)
        @DisplayName("duplicateCheck Test - ok")
        void duplicateCheck() throws Exception{
            log.info("duplicateCheck Test - ok");
            // given
            String id = anyString();

            given(loginService.isExist(id)).willReturn(false);

            log.info("duplicateCheck Test - ok | given: ✔");
            // when

            log.info("duplicateCheck Test - ok | when: ✔");
            // then
            mvc.perform(get("/login/duplicateCheck")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isOk());

            log.info("duplicateCheck Test - ok | then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("duplicateCheck Test - badRequest")
        void duplicateCheckFalse() throws Exception{
            log.info("duplicateCheck Test - badRequest");
            // given
            String id = anyString();

            given(loginService.isExist(id)).willReturn(true);

            log.info("duplicateCheck Test - badRequest | given: ✔");
            // when

            log.info("duplicateCheck Test - badRequest | when: ✔");
            // then
            mvc.perform(get("/login/duplicateCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("duplicateCheck Test - badRequest | then: ✔");
        }

    }

    @Nested
    @DisplayName("SignUp Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SignUpTest{
        @Test
        @Order(1)
        @DisplayName("signUp Test - ok")
        void signUpTest() throws Exception{
            log.info("signUp Test - ok");
            // given
            MemberDTO memberDTO = MemberDTO.builder()
                    .id("test")
                    .password("test")
                    .name("test")
                    .email("test")
                    .phone("test")
                    .studentNum("test")
                    .type(TYPE_STUDENT)
                    .dormitoryDTO(mock(DormitoryDTO.class))
                    .build();

            TokenDTO tokenDTO = mock(TokenDTO.class);

            given(loginService.isExist(anyString())).willReturn(false);
            given(loginService.signUp(any(MemberDTO.class))).willReturn(tokenDTO);

            log.info("signUp Test - ok | given: ✔");
            // when
            mvc.perform(post("/login/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isOk());

            log.info("signUp Test - ok | when: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("signUp Test - badRequest")
        void signUpTestFalse() throws Exception{
            log.info("signUp Test - badRequest");
            // given
            MemberDTO memberDTO = mock(MemberDTO.class);

            given(loginService.isExist(anyString())).willReturn(true);
            given(loginService.signUp(any(MemberDTO.class))).willThrow(new DuplicatedIdException(ErrorCode.DUPLICATED_ID));

            log.info("signUp Test - badRequest | given: ✔");
            // when
            mvc.perform(post("/login/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isBadRequest());

            log.info("signUp Test - badRequest | when: ✔");
        }
    }

    @Nested
    @DisplayName("SignIn Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SignInTest{
        @Test
        @Order(1)
        @DisplayName("signIn Test - ok")
        void signInTest() throws Exception{
            log.info("signIn Test - ok");
            // given
            LoginDTO loginDTO = LoginDTO.builder()
                    .id("test")
                    .password("test")
                    .build();

            TokenDTO tokenDTO = mock(TokenDTO.class);

            given(loginService.signIn(any(LoginDTO.class))).willReturn(tokenDTO);

            log.info("signIn Test - ok | given: ✔");
            // when
            mvc.perform(post("/login/signIn")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk());

            log.info("signIn Test - ok | when: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("signIn Test(Invalid Id) - badRequest")
        void signInTestFalse() throws Exception{
            log.info("signIn Test(Invalid Id) - badRequest");
            // given
            LoginDTO loginDTO = mock(LoginDTO.class);

            given(loginService.signIn(any(LoginDTO.class))).willThrow(new SignInFailedException(ErrorCode.SIGN_IN_FAILED));

            log.info("signIn Test(Invalid Id) - badRequest | given: ✔");
            // when
            mvc.perform(post("/login/signIn")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isBadRequest());

            log.info("signIn Test(Invalid Id) - badRequest | when: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("signIn Test(Invalid Password) - badRequest")
        void signInTestFalse2() throws Exception{
            log.info("signIn Test(Invalid Password) - badRequest");
            // given
            LoginDTO loginDTO = mock(LoginDTO.class);

            given(loginService.signIn(any(LoginDTO.class))).willThrow(new SignInFailedException(ErrorCode.SIGN_IN_FAILED));

            log.info("signIn Test(Invalid Password) - badRequest | given: ✔");
            // when
            mvc.perform(post("/login/signIn")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isBadRequest());

            log.info("signIn Test(Invalid Password) - badRequest | when: ✔");
        }
    }

    @Nested
    @DisplayName("sendVerifyCode Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendVerifyCode{
        @Test
        @Order(1)
        @DisplayName("sendVerifyCode Test - SMS")
        void sendVerifyCodeTestSMS() throws Exception{
            log.info("sendVerifyCode Test - SMS");
            // given
            String id = "test";

            willDoNothing().given(loginService).sendVerifyCodeBySMS(anyString());

            log.info("sendVerifyCode Test - SMS | given: ✔");
            // when & then
            mvc.perform(post("/login/sendVerifyBySMS")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("sendVerifyCode Test - SMS | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("sendVerifyCode Test(올바르지 않은 아이디) - SMS")
        void sendVerifyCodeTestSMSFalse() throws Exception{
            log.info("sendVerifyCode Test(올바르지 않은 아이디) - SMS");
            // given
            String id = "test";

            willThrow(new InvalidIdException(ErrorCode.INVALID_ID)).given(loginService).sendVerifyCodeBySMS(anyString());

            log.info("sendVerifyCode Test(올바르지 않은 아이디) - SMS | given: ✔");
            // when & then
            mvc.perform(post("/login/sendVerifyBySMS")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("sendVerifyCode Test(올바르지 않은 아이디) - SMS | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("sendVerifyCode Test - Email")
        void sendVerifyCodeTestEmail() throws Exception{
            log.info("sendVerifyCode Test - Email");
            // given
            String id = "test";

            willDoNothing().given(loginService).sendVerifyCodeByEmail(anyString());

            log.info("sendVerifyCode Test - Email | given: ✔");
            // when & then
            mvc.perform(post("/login/sendVerifyByEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("sendVerifyCode Test - Email | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("sendVerifyCode Test(올바르지 않은 아이디) - Email")
        void sendVerifyCodeTestEmailFalse() throws Exception{
            log.info("sendVerifyCode Test(올바르지 않은 아이디) - Email");
            // given
            String id = "test";

            willThrow(new InvalidIdException(ErrorCode.INVALID_ID)).given(loginService).sendVerifyCodeByEmail(anyString());

            log.info("sendVerifyCode Test(올바르지 않은 아이디) - Email | given: ✔");
            // when & then
            mvc.perform(post("/login/sendVerifyByEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("sendVerifyCode Test(올바르지 않은 아이디) - Email | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("verifyCode Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class verifyCodeTest{
        @Test
        @Order(1)
        @DisplayName("verifyCodeBySMS Test - ok")
        void verifyCodeBySMSTest() throws Exception{
            log.info("verifyCodeBySMS Test - ok");
            // given
            String key = "test";
            String code = "test";

            given(loginService.verification(anyString(), anyString())).willReturn(true);

            log.info("verifyCodeBySMS Test - ok | given: ✔");
            // when & then
            mvc.perform(post("/login/verifyCodeBySMS")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("key", key)
                    .param("code", code))
                    .andExpect(status().isOk());

            log.info("verifyCodeBySMS Test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("verifyCodeBySMS Test - badRequest")
        void verifyCodeBySMSTestFalse() throws Exception{
            log.info("verifyCodeBySMS Test - badRequest");
            // given
            String key = "test";
            String code = "test";

            given(loginService.verification(anyString(), anyString())).willReturn(false);

            log.info("verifyCodeBySMS Test - badRequest | given: ✔");
            // when & then
            mvc.perform(post("/login/verifyCodeBySMS")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("key", key)
                    .param("code", code))
                    .andExpect(status().isBadRequest());

            log.info("verifyCodeBySMS Test - badRequest | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("verifyCodeByEmail Test - ok")
        void verifyCodeByEmailTest() throws Exception{
            log.info("verifyCodeByEmail Test - ok");
            // given
            String key = "test";
            String code = "test";

            given(loginService.verification(anyString(), anyString())).willReturn(true);

            log.info("verifyCodeByEmail Test - ok | given: ✔");
            // when & then
            mvc.perform(post("/login/verifyCodeByEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("key", key)
                    .param("code", code))
                    .andExpect(status().isOk());

            log.info("verifyCodeByEmail Test - ok | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("verifyCodeByEmail Test - badRequest")
        void verifyCodeByEmailTestFalse() throws Exception{
            log.info("verifyCodeByEmail Test - badRequest");
            // given
            String key = "test";
            String code = "test";

            given(loginService.verification(anyString(), anyString())).willReturn(false);

            log.info("verifyCodeByEmail Test - badRequest | given: ✔");
            // when & then
            mvc.perform(post("/login/verifyCodeByEmail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("key", key)
                    .param("code", code))
                    .andExpect(status().isBadRequest());

            log.info("verifyCodeByEmail Test - badRequest | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Logout Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class LogoutTest{
        @Test
        @Order(1)
        @DisplayName("logout Test - ok")
        void logoutTest() throws Exception{
            log.info("logout Test - ok");
            // given
            String token = "test";

            doNothing().when(loginService).logout(anyString());

            log.info("logout Test - ok | given: ✔");
            // when
            mvc.perform(post("/login/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .header("Authorization", token))
                    .andExpect(status().isOk());

            log.info("logout Test - ok | when: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("logout Test - badRequest")
        void logoutTestFalse() throws Exception{
            log.info("logout Test - badRequest");
            // given
            String token = "test";

            // 여러가지 Exception이 있지만 IllegalTokenException을 사용
            // 나중에 다른 Exception으로 변경하여 테스트 진행
            willThrow(JWTException.class).given(loginService).logout(anyString());

            log.info("logout Test - badRequest | given: ✔");
            // when
            mvc.perform(post("/login/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .header("Authorization", token))
                    .andExpect(status().isBadRequest());

            log.info("logout Test - badRequest | when: ✔");
        }
    }
}
