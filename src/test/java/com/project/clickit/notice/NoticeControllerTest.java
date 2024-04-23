package com.project.clickit.notice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.NoticeController;
import com.project.clickit.dto.NoticeDTO;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.exceptions.notice.NoticeNotFoundException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = NoticeController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class NoticeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeService noticeService;

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
            NoticeDTO noticeDTO = mock(NoticeDTO.class);

            doNothing().when(noticeService).createNotice(any(NoticeDTO.class));

            log.info("create Test given: ✔");
            // when & then
            mvc.perform(post("/notice/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(noticeDTO)))
                    .andExpect(status().isOk());

            log.info("create Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("create Test - duplicatedIdException")
        void createTestDuplicatedIdException() throws Exception {
            log.info("create Test - duplicatedIdException");
            // given
            NoticeDTO noticeDTO = mock(NoticeDTO.class);

            doThrow(new DuplicatedIdException()).when(noticeService).createNotice(any(NoticeDTO.class));

            log.info("create Test - duplicatedIdException given: ✔");
            // when & then
            mvc.perform(post("/notice/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(noticeDTO)))
                    .andExpect(status().isBadRequest());

            log.info("create Test - duplicatedIdException when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadTest{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest() throws Exception {
            log.info("getAll Test");
            // given
            int page = 0;
            int size = 10;

            log.info("getAll Test given: ✔");
            // when & then
            mvc.perform(get("/notice/getAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size)))
                    .andExpect(status().isOk());

            log.info("getAll Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findByNoticeNum Test")
        void findByNoticeNumTest() throws Exception {
            log.info("findByNoticeNum Test");
            // given
            int num = 1;

            when(noticeService.findByNoticeNum(num)).thenReturn(mock(NoticeDTO.class));

            log.info("findByNoticeNum Test given: ✔");
            // when & then
            mvc.perform(get("/notice/findByNoticeNum")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("num", String.valueOf(num)))
                    .andExpect(status().isOk());

            log.info("findByNoticeNum Test when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByNoticeNum Test - NoticeNotFoundException")
        void findByNoticeNumTestNoticeNotFoundException() throws Exception {
            log.info("findByNoticeNum Test - NoticeNotFoundException");
            // given
            int num = 1;

            given(noticeService.isExist(anyInt())).willThrow(new NoticeNotFoundException());

            doThrow(new ObjectNotFoundException()).when(noticeService).findByNoticeNum(anyInt());

            log.info("findByNoticeNum Test - NoticeNotFoundException given: ✔");
            // when & then
            mvc.perform(get("/notice/findByNoticeNum")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("num", String.valueOf(num)))
                    .andExpect(status().isBadRequest());

            log.info("findByNoticeNum Test - NoticeNotFoundException when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByWriterId Test")
        void findByWriterIdTest() throws Exception {
            log.info("findByWriterId Test");
            // given
            String writerId = "test";
            int page = 0;
            int size = 10;

            when(noticeService.findNoticeByWriterId(anyString(), any())).thenReturn(Page.empty());

            log.info("findByWriterId Test given: ✔");
            // when & then
            mvc.perform(get("/notice/findByWriterId")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("writerId", writerId)
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size)))
                    .andExpect(status().isOk());

            log.info("findByWriterId Test when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("update Test")
        void updateTest() throws Exception {
            log.info("update Test");
            // given
            NoticeDTO noticeDTO = mock(NoticeDTO.class);

            doNothing().when(noticeService).updateNotice(any(NoticeDTO.class));

            log.info("update Test given: ✔");
            // when & then
            mvc.perform(put("/notice/updateNotice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(noticeDTO)))
                    .andExpect(status().isOk());

            log.info("update Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("update Test - ObjectNotFoundException")
        void updateTestObjectNotFoundException() throws Exception {
            log.info("update Test - ObjectNotFoundException");
            // given
            NoticeDTO noticeDTO = mock(NoticeDTO.class);

            doThrow(new ObjectNotFoundException()).when(noticeService).updateNotice(any(NoticeDTO.class));

            log.info("update Test - ObjectNotFoundException given: ✔");
            // when & then
            mvc.perform(put("/notice/updateNotice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(noticeDTO)))
                    .andExpect(status().isBadRequest());

            log.info("update Test - ObjectNotFoundException when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("delete Test")
        void deleteTest() throws Exception {
            log.info("delete Test");
            // given
            int num = 1;

            doNothing().when(noticeService).deleteNotice(anyInt());

            log.info("delete Test given: ✔");
            // when & then
            mvc.perform(delete("/notice/deleteNotice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("num", String.valueOf(num)))
                    .andExpect(status().isOk());

            log.info("delete Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("delete Test - ObjectNotFoundException")
        void deleteTestObjectNotFoundException() throws Exception {
            log.info("delete Test - ObjectNotFoundException");
            // given
            int num = 1;

            doThrow(new ObjectNotFoundException()).when(noticeService).deleteNotice(anyInt());

            log.info("delete Test - ObjectNotFoundException given: ✔");
            // when & then
            mvc.perform(delete("/notice/deleteNotice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("num", String.valueOf(num)))
                    .andExpect(status().isBadRequest());

            log.info("delete Test - ObjectNotFoundException when & then: ✔");
        }
    }
}
