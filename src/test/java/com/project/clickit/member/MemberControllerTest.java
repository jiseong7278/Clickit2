package com.project.clickit.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.MemberController;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("create Test")
        void createTest() throws Exception {
            log.info("create Test");
            // given
            MemberDTO memberDTO = mock(MemberDTO.class);

            willDoNothing().given(memberService).create(any(MemberDTO.class));

            log.info("create Test given: ✔");
            // when & then
            mvc.perform(post("/member/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isOk());

            log.info("create Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("create Test - duplicated id")
        void createTestDuplicatedId() throws Exception {
            log.info("create Test - duplicated id");
            // given
            MemberDTO memberDTO = mock(MemberDTO.class);

            willThrow(new DuplicatedIdException(ErrorCode.DUPLICATED_ID)).given(memberService).create(any(MemberDTO.class));

            log.info("create Test - duplicated id | given: ✔");
            // when & then
            mvc.perform(post("/member/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isBadRequest());

            log.info("create Test - duplicated id | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("createList Test")
        void createListTest() throws Exception{
            log.info("createList Test");
            // given
            MemberDTO memberDTO1 = mock(MemberDTO.class);
            MemberDTO memberDTO2 = mock(MemberDTO.class);

            List<MemberDTO> memberDTOList = List.of(memberDTO1, memberDTO2);

            willDoNothing().given(memberService).createList(anyList());

            log.info("createList Test given: ✔");
            // when & then
            mvc.perform(post("/member/createList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTOList)))
                    .andExpect(status().isOk());

            log.info("createList Test when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("createList Test - duplicated id")
        void createListTestDuplicatedId() throws Exception{
            log.info("createList Test - duplicated id");
            // given
            MemberDTO memberDTO1 = mock(MemberDTO.class);
            MemberDTO memberDTO2 = mock(MemberDTO.class);

            List<MemberDTO> memberDTOList = List.of(memberDTO1, memberDTO2);

            willThrow(new DuplicatedIdException(ErrorCode.DUPLICATED_ID)).given(memberService).createList(anyList());

            log.info("createList Test - duplicated id | given: ✔");
            // when & then
            mvc.perform(post("/member/createList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTOList)))
                    .andExpect(status().isBadRequest());

            log.info("createList Test - duplicated id | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadTest{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest() throws Exception{
            log.info("getAll Test");
            // given
            int size = 10;
            int page = 0;

            given(memberService.getAll(any())).willReturn(Page.empty());

            log.info("getAll Test given: ✔");
            // when & then
            mvc.perform(get("/member/getAll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("size", String.valueOf(size))
                    .param("page", String.valueOf(page)))
                    .andExpect(status().isOk());

            log.info("getAll Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findByMemberId Test")
        void findByMemberIdTest() throws Exception{
            log.info("findByMemberId Test");
            // given
            String id = "test";

            given(memberService.findByMemberId(any())).willReturn(mock(MemberDTO.class));

            log.info("findByMemberId Test given: ✔");
            // when & then
            mvc.perform(get("/member/findByMemberId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("findByMemberId Test when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByMemberId Test - MemberNotFoundException")
        void findByMemberIdTestMemberNotFoundException() throws Exception{
            log.info("findByMemberId Test - MemberNotFoundException");
            // given
            String id = "test";

            given(memberService.findByMemberId(any())).willThrow(new ObjectNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

            log.info("findByMemberId Test - MemberNotFoundException | given: ✔");
            // when & then
            mvc.perform(get("/member/findByMemberId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("findByMemberId Test - MemberNotFoundException | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByMemberName Test")
        void findByMemberNameTest() throws Exception{
            log.info("findByMemberName Test");
            // given
            String name = "test";
            int size = 10;
            int page = 0;

            given(memberService.findByMemberName(any(), any())).willReturn(Page.empty());

            log.info("findByMemberName Test given: ✔");
            // when & then
            mvc.perform(get("/member/findByMemberName")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("name", name)
                            .param("size", String.valueOf(size))
                            .param("page", String.valueOf(page)))
                    .andExpect(status().isOk());

            log.info("findByMemberName Test when & then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("findByDormitoryId Test")
        void findByDormitoryIdTest() throws Exception{
            log.info("findByDormitoryId Test");
            // given
            String dormitoryId = "test";
            int size = 10;
            int page = 0;

            given(memberService.findByDormitoryId(any(), any())).willReturn(Page.empty());

            log.info("findByDormitoryId Test given: ✔");
            // when & then
            mvc.perform(get("/member/findByDormitoryId")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("dormitoryId", dormitoryId)
                            .param("size", String.valueOf(size))
                            .param("page", String.valueOf(page)))
                    .andExpect(status().isOk());

            log.info("findByDormitoryId Test when & then: ✔");
        }

        @Test
        @Order(6)
        @DisplayName("getAllStudent Test")
        void getAllStudentTest() throws Exception{
            log.info("getAllStudent Test");
            // given
            int size = 10;
            int page = 0;

            given(memberService.getAllStudent(any(Pageable.class))).willReturn(Page.empty());

            log.info("getAllStudent Test given: ✔");
            // when & then
            mvc.perform(get("/member/getAllStudent")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("size", String.valueOf(size))
                            .param("page", String.valueOf(page)))
                    .andExpect(status().isOk());

            log.info("getAllStudent Test when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("updateMember Test")
        void updateMemberTest() throws Exception{
            log.info("updateMember Test");
            // given
            MemberDTO memberDTO = mock(MemberDTO.class);

            willDoNothing().given(memberService).update(any(MemberDTO.class));

            log.info("updateMember Test given: ✔");
            // when & then
            mvc.perform(put("/member/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isOk());

            log.info("updateMember Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("updatePassword Test")
        void updatePasswordTest() throws Exception{
            log.info("updatePassword Test");
            // given
            String password = "password";

            willDoNothing().given(memberService).updatePassword(any());

            log.info("updatePassword Test given: ✔");
            // when & then
            mvc.perform(put("/member/updatePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("password", password))
                    .andExpect(status().isOk());

            log.info("updatePassword Test when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("updatePassword Test - MemberNotFoundException")
        void updatePasswordTestMemberNotFoundException() throws Exception{
            log.info("updatePassword Test - MemberNotFoundException");
            // given
            String password = "password";

            willThrow(new ObjectNotFoundException(ErrorCode.MEMBER_NOT_FOUND)).given(memberService).updatePassword(any());

            log.info("updatePassword Test - MemberNotFoundException | given: ✔");
            // when & then
            mvc.perform(put("/member/updatePassword")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("password", password))
                    .andExpect(status().isBadRequest());

            log.info("updatePassword Test - MemberNotFoundException | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("updateMemberForStaff Test")
        void updateMemberForStaffTest() throws Exception{
            log.info("updateMemberForStaff Test");
            // given
            MemberDTO memberDTO = mock(MemberDTO.class);

            willDoNothing().given(memberService).updateMemberForStaff(any(MemberDTO.class));

            log.info("updateMemberForStaff Test given: ✔");
            // when & then
            mvc.perform(put("/member/updateMemberForStaff")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isOk());

            log.info("updateMemberForStaff Test when & then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("updateMemberForStaff Test - MemberNotFoundException")
        void updateMemberForStaffTestMemberNotFoundException() throws Exception{
            log.info("updateMemberForStaff Test - MemberNotFoundException");
            // given
            MemberDTO memberDTO = mock(MemberDTO.class);

            willThrow(new ObjectNotFoundException(ErrorCode.MEMBER_NOT_FOUND)).given(memberService).updateMemberForStaff(any(MemberDTO.class));

            log.info("updateMemberForStaff Test - MemberNotFoundException | given: ✔");
            // when & then
            mvc.perform(put("/member/updateMemberForStaff")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(memberDTO)))
                    .andExpect(status().isBadRequest());

            log.info("updateMemberForStaff Test - MemberNotFoundException | when & then: ✔");
        }

        @Test
        @Order(6)
        @DisplayName("updateRefreshToken Test")
        void updateRefreshTokenTest() throws Exception {
            log.info("updateRefreshToken Test");
            // given
            String id = "test";
            String refreshToken = "refreshToken";

            willDoNothing().given(memberService).updateRefreshToken(any(), any());

            log.info("updateRefreshToken Test given: ✔");
            // when & then
            mvc.perform(put("/member/updateRefreshToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id)
                            .param("refreshToken", refreshToken))
                    .andExpect(status().isOk());

            log.info("updateRefreshToken Test when & then: ✔");
        }

        @Test
        @Order(7)
        @DisplayName("updateRefreshToken Test - MemberNotFoundException")
        void updateRefreshTokenTestMemberNotFoundException() throws Exception {
            log.info("updateRefreshToken Test - MemberNotFoundException");
            // given
            String id = "test";
            String refreshToken = "refreshToken";

            willThrow(new ObjectNotFoundException(ErrorCode.MEMBER_NOT_FOUND)).given(memberService).updateRefreshToken(any(), any());

            log.info("updateRefreshToken Test - MemberNotFoundException | given: ✔");
            // when & then
            mvc.perform(put("/member/updateRefreshToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id)
                            .param("refreshToken", refreshToken))
                    .andExpect(status().isBadRequest());

            log.info("updateRefreshToken Test - MemberNotFoundException | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("deleteById Test")
        void deleteByIdTest() throws Exception{
            log.info("deleteById Test");
            // given
            String id = "test";

            willDoNothing().given(memberService).deleteById(any());

            log.info("deleteById Test given: ✔");
            // when & then
            mvc.perform(delete("/member/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isOk());

            log.info("deleteById Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("deleteById Test - MemberNotFoundException")
        void deleteByIdTestMemberNotFoundException() throws Exception{
            log.info("deleteById Test - MemberNotFoundException");
            // given
            String id = "test";

            willThrow(new ObjectNotFoundException(ErrorCode.MEMBER_NOT_FOUND)).given(memberService).deleteById(any());

            log.info("deleteById Test - MemberNotFoundException | given: ✔");
            // when & then
            mvc.perform(delete("/member/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("deleteById Test - MemberNotFoundException | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("deleteAllStudent Test")
        void deleteAllStudentTest() throws Exception{
            log.info("deleteAllStudent Test");
            // given
            willDoNothing().given(memberService).deleteAll();

            log.info("deleteAllStudent Test given: ✔");
            // when & then
            mvc.perform(delete("/member/deleteAllStudent"))
                    .andExpect(status().isOk());

            log.info("deleteAllStudent Test when & then: ✔");
        }
    }
}
