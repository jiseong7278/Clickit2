package com.project.clickit.dormitory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.DormitoryController;
import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.dormitory.DormitoryNotFoundException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.DormitoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = DormitoryController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@AutoConfigureMockMvc(addFilters = false)
public class DormitoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DormitoryService dormitoryService;

    @Nested
    @DisplayName("duplicateCheck Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DuplicateCheckTest{
        @Test
        @Order(1)
        @DisplayName("duplicateCheck Test")
        void duplicateCheckTest() throws Exception {
            log.info("duplicateCheck Test");
            // given
            String id = anyString();

            given(dormitoryService.isExist(id)).willReturn(true);

            log.info("duplicateCheck Test given: ✔");
            // when

            log.info("duplicateCheck Test when: ✔");
            // then
            mvc.perform(get("/dormitory/duplicateCheck")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("duplicateCheck Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("duplicateCheck Test - Success")
        void duplicateCheckTestSuccess() throws Exception {
            log.info("duplicateCheck Test - Success");
            // given
            String id = anyString();

            given(dormitoryService.isExist(id)).willReturn(false);

            log.info("duplicateCheck Test - Success given: ✔");
            // when

            log.info("duplicateCheck Test - Success when: ✔");
            // then
            mvc.perform(get("/dormitory/duplicateCheck")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isOk());

            log.info("duplicateCheck Test - Success then: ✔");
        }
    }

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("create Test")
        void createTest() throws Exception{
            log.info("create Test");
            // given
            DormitoryDTO dormitoryDTO = mock(DormitoryDTO.class);

            given(dormitoryService.isExist(anyString())).willReturn(false);

            String requestBody = objectMapper.writeValueAsString(dormitoryDTO);

            log.info("create Test given: ✔");
            // when
            ResultActions result = mvc.perform(post("/dormitory/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody));

            log.info("create Test when: ✔");
            // then
            assertAll(
                    () -> result.andExpect(status().isOk()),
                    () -> verify(dormitoryService, times(1)).createDormitory(any())
            );

            log.info("create Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("create Test - Failed")
        void createTestFailed() throws Exception{
            log.info("create Test - Failed");
            // given
            DormitoryDTO dormitoryDTO = mock(DormitoryDTO.class);

            String requestBody = objectMapper.writeValueAsString(dormitoryDTO);

            given(dormitoryService.isExist(anyString())).willReturn(true);

            doThrow(new DuplicatedIdException()).when(dormitoryService).createDormitory(any());

            log.info("create Test - Failed given: ✔");
            // when
            mvc.perform(post("/dormitory/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody))
                    .andExpect(status().isBadRequest());

            log.info("create Test - Failed when: ✔");
            // then
            log.info("create Test - Failed then: ✔");
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
            int size = 10;
            int page = 0;

            log.info("getAll Test given: ✔");
            // when & then
            mvc.perform(get("/dormitory/getAll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("size", Integer.toString(size))
                    .param("page", Integer.toString(page)))
                    .andExpect(status().isOk());

            log.info("getAll Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest() throws Exception {
            log.info("findById Test");
            // given
            String id = anyString();

            given(dormitoryService.isExist(id)).willReturn(true);

            log.info("findById Test given: ✔");
            // when & then
            mvc.perform(get("/dormitory/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("findById Test when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findById Test - Failed")
        void findByIdTestFailed() throws Exception {
            log.info("findById Test - Failed");
            // given
            String id = anyString();

            given(dormitoryService.isExist(id)).willReturn(false);

            doThrow(new DuplicatedIdException()).when(dormitoryService).findById(anyString());

            log.info("findById Test - Failed given: ✔");
            // when & then
            mvc.perform(get("/dormitory/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("findById Test - Failed when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByName Test")
        void findByNameTest() throws Exception {
            log.info("findByName Test");
            // given
            String name = "test";
            int size = 10;
            int page = 0;

            log.info("findByName Test given: ✔");
            // when & then
            mvc.perform(get("/dormitory/findByName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("name", name)
                    .param("size", eq(Integer.toString(size)))
                    .param("page", eq(Integer.toString(page))))
                    .andExpect(status().isOk());

            log.info("findByName Test when & then: ✔");
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
            DormitoryDTO dormitoryDTO = mock(DormitoryDTO.class);

            String requestBody = objectMapper.writeValueAsString(dormitoryDTO);

            log.info("update Test given: ✔");
            // when & then
            mvc.perform(put("/dormitory/updateDormitoryName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody))
                    .andExpect(status().isOk());

            verify(dormitoryService, times(1)).updateDormitory(any());

            log.info("update Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("update Test - Failed")
        void updateTestFailed() throws Exception {
            log.info("update Test - Failed");
            // given
            DormitoryDTO dormitoryDTO = mock(DormitoryDTO.class);

            String requestBody = objectMapper.writeValueAsString(dormitoryDTO);

            doThrow(new DormitoryNotFoundException()).when(dormitoryService).updateDormitory(any());

            log.info("update Test - Failed given: ✔");
            // when & then
            mvc.perform(put("/dormitory/updateDormitoryName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody))
                    .andExpect(status().isBadRequest());

            log.info("update Test - Failed when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("deleteById Test")
        void deleteByIdTest() throws Exception {
            log.info("deleteById Test");
            // given
            String id = anyString();

            given(dormitoryService.isExist(id)).willReturn(true);

            log.info("deleteById Test given: ✔");
            // when & then
            mvc.perform(delete("/dormitory/deleteById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            verify(dormitoryService, times(1)).deleteById(anyString());

            log.info("deleteById Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("deleteById Test - Failed")
        void deleteByIdTestFailed() throws Exception {
            log.info("deleteById Test - Failed");
            // given
            String id = anyString();

            given(dormitoryService.isExist(id)).willReturn(false);

            doThrow(new DormitoryNotFoundException()).when(dormitoryService).deleteById(anyString());

            log.info("deleteById Test - Failed given: ✔");
            // when & then
            mvc.perform(delete("/dormitory/deleteById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("deleteById Test - Failed when & then: ✔");
        }
    }
}
