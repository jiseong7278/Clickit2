package com.project.clickit.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.controller.LoginController;
import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.login.DuplicatedIdException;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    private LocalDateTime localDateTime = LocalDateTime.now();

    private LoginDTO loginDTO;
    private LoginDTO loginDTO2;
    private MemberDTO memberDTO;
    private MemberDTO memberDTO2;

    @BeforeEach
    void setUp(){
        // 로그인 테스트 시 사용할 DTO
        loginDTO = LoginDTO.builder()
                .id("test_member_id")
                .password("test_member_password")
                .build();

        // 회원가입 및 아이디 중복 실패 테스트 시 사용할 DTO
        memberDTO = MemberDTO.builder()
                .id("test_member_id")
                .password("test_member_password")
                .name("회원가입")
                .email("signUpTest_email@clickit.com")
                .phone("010-1234-5678")
                .studentNum("20181216")
                .build();

        memberDTO2 = MemberDTO.builder()
                .id("id_"+localDateTime.getSecond()+String.valueOf(localDateTime.getNano()).substring(0, 2))
                .password("password_"+String.valueOf(localDateTime.getNano()).substring(0, 2)+localDateTime.getSecond())
                .name("이름이")
                .email("signUp@clickit.com")
                .phone("010-"+String.valueOf(localDateTime.getNano()).substring(0, 2)+localDateTime.getSecond()+"-"+localDateTime.getSecond()+String.valueOf(localDateTime.getNano()).substring(0, 2))
                .studentNum("20"+localDateTime.getSecond()+"12"+String.valueOf(localDateTime.getNano()).substring(0, 2))
                .build();

        // 로그인 실패 테스트 시 사용할 DTO
        loginDTO = LoginDTO.builder()
                .id("fail_id")
                .password("fail_password")
                .build();

        loginDTO2 = LoginDTO.builder()
                .id("test_member_id")
                .password("test_member_password")
                .build();
    }

    @Test
    @DisplayName("중복 체크 실패 테스트")
    void duplicateCheckTestFailed() throws Exception {
        log.info("아이디 중복 체크 테스트");
        // given
        given(loginService.duplicateCheck(memberDTO.getId())).willReturn(true);

        log.info("중복된 아이디로 중복 체크 시도");
        //when and then
        ResultActions result = mvc.perform(get("/login/duplicateCheck")
                        .param("id", memberDTO.getId()))
                .andExpect(status().isBadRequest());
        log.info("중복 체크 결과: {}", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("중복 체크 성공 테스트")
    void duplicateCheckTestSuccess() throws Exception {
        // 한번도 사용되지 않은 아이디
        String never_used_id = "never_used_id";

        log.info("아이디 중복 체크 테스트");
        // given
        given(loginService.duplicateCheck(never_used_id)).willReturn(false);

        log.info("중복되지 않은 아이디로 중복 체크 시도");
        //when and then
        ResultActions result = mvc.perform(get("/login/duplicateCheck")
                        .param("id", never_used_id)).andExpect(status().isOk());
        log.info("중복 체크 결과: {}", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void singUpTestFailed() throws Exception{
        log.info("회원가입 실패 테스트");

        String con = objectMapper.writeValueAsString(memberDTO);
        log.info("con: {}", con);

        // given
//        given(loginService.signUp(memberDTO)).willReturn(null);
        given(loginService.signUp(memberDTO)).willThrow(new DuplicatedIdException(ErrorCode.DUPLICATED_ID));

        log.info("회원가입 시도");
        // when and then
        ResultActions result = mvc.perform(post("/login/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(con)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        log.info("회원가입 결과: {}", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpTestSuccess() throws Exception{
        log.info("회원가입 테스트");

        // given
        given(loginService.signUp(memberDTO2)).willReturn(new TokenDTO());

        log.info("회원가입 시도");
        // when and then
        ResultActions result = mvc.perform(post("/login/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content("{\"id\":\""+memberDTO2.getId()+"\", \"password\":\""+memberDTO2.getPassword()+"\", \"name\":\""+memberDTO2.getName()+"\", \"email\":\""+memberDTO2.getEmail()+"\", \"phone\":\""+memberDTO2.getPhone()+"\", \"studentNum\":\""+memberDTO2.getStudentNum()+"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void signInTestFailed() throws Exception{
        log.info("로그인 실패 테스트");

        String con = objectMapper.writeValueAsString(loginDTO);
        log.info("con: {}", con);

        // given
        given(loginService.signIn(loginDTO)).willThrow(RuntimeException.class);

        log.info("로그인 시도");
        // when and then
        ResultActions result = mvc.perform(post("/login/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(con)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        log.info("로그인 결과: {}", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void signInTestSuccess() throws Exception{
        log.info("로그인 성공 테스트");

        String con = objectMapper.writeValueAsString(loginDTO2);
        log.info("con: {}", con);

        // given
        given(loginService.signIn(loginDTO)).willReturn(new TokenDTO());

        log.info("로그인 시도");
        // when and then
        ResultActions result = mvc.perform(post("/login/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content("{\"id\":\""+loginDTO.getId()+"\", \"password\":\""+loginDTO.getPassword()+"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
