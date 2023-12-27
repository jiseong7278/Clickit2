package com.project.clickit.login;

import com.project.clickit.controller.LoginController;
import com.project.clickit.dto.LoginDTO;
import com.project.clickit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoginService loginService;

    private LoginDTO loginDTO;

    @BeforeEach
    void setUp(){
        loginDTO = LoginDTO.builder()
                .id("test_member_id")
                .password("test_member_password")
                .build();
    }

    @Test
    @DisplayName("중복 체크 테스트 - 중복된 아이디")
    void duplicateCheckTest() throws Exception {
        log.info("아이디 중복 체크 테스트");
        // given
        given(loginService.duplicateCheck(loginDTO.getId())).willReturn(true);

        log.info("중복된 아이디로 중복 체크 시도");
        //when and then
        ResultActions result = mvc.perform(get("/login/duplicateCheck")
                        .param("id", loginDTO.getId()))
                .andExpect(status().isBadRequest());
        log.info("중복 체크 결과: {}", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("중복 체크 테스트 - 중복되지 않은 아이디")
    void duplicateCheckTest2() throws Exception {
        log.info("아이디 중복 체크 테스트");
        // given
        given(loginService.duplicateCheck(loginDTO.getId())).willReturn(false);

        log.info("중복되지 않은 아이디로 중복 체크 시도");
        //when and then
        ResultActions result = mvc.perform(get("/login/duplicateCheck")
                        .param("id", "never_used_id")).andExpect(status().isOk());
        log.info("중복 체크 결과: {}", result.andReturn().getResponse().getContentAsString());
    }
}
