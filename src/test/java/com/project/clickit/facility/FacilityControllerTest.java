package com.project.clickit.facility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.FacilityController;
import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.facility.FacilityNotFoundException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.FacilityService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = FacilityController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@AutoConfigureMockMvc(addFilters = false)
public class FacilityControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacilityService facilityService;

    @Nested
    @DisplayName("DuplicateCheck Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DuplicateCheckTest{
        @Test
        @Order(1)
        @DisplayName("duplicateCheck Test - ok")
        void duplicateCheckTest() throws Exception {
            log.info("duplicateCheck Test - ok");
            // given
            String id = anyString();

            given(facilityService.isExist(id)).willReturn(false);

            log.info("duplicateCheck Test - ok | given: ✔");
            // when

            log.info("duplicateCheck Test - ok | when: ✔");
            // then
            mvc.perform(get("/facility/duplicateCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("duplicateCheck Test - ok | then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("duplicateCheck Test - badRequest")
        void duplicateCheckTestFalse() throws Exception {
            log.info("duplicateCheck Test - badRequest");
            // given
            String id = anyString();

            given(facilityService.isExist(id)).willReturn(true);

            log.info("duplicateCheck Test - badRequest | given: ✔");
            // when

            log.info("duplicateCheck Test - badRequest | when: ✔");
            // then
            mvc.perform(get("/facility/duplicateCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("duplicateCheck Test - badRequest | then: ✔");
        }
    }

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
            FacilityDTO facilityDTO = mock(FacilityDTO.class);

            given(facilityService.isExist(anyString())).willReturn(false);

            String requestBody = objectMapper.writeValueAsString(facilityDTO);

            log.info("create Test given: ✔");
            // when & then
            mvc.perform(post("/facility/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody))
                    .andExpect(status().isOk());

            verify(facilityService, times(1)).createFacility(any());

            log.info("create Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("create Test - Failed")
        void createTestFailed() throws Exception {
            log.info("create Test - Failed");
            // given
            FacilityDTO facilityDTO = mock(FacilityDTO.class);

            String requestBody = objectMapper.writeValueAsString(facilityDTO);

            given(facilityService.isExist(anyString())).willReturn(true);

            doThrow(new DuplicatedIdException()).when(facilityService).createFacility(any());

            log.info("create Test - Failed given: ✔");
            // when & then
            mvc.perform(post("/facility/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody))
                    .andExpect(status().isBadRequest());

            log.info("create Test - Failed when & then: ✔");
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

            log.info("getAll Test given: ✔");
            // when & then
            mvc.perform(get("/facility/getAll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("size", String.valueOf(size))
                    .param("page", String.valueOf(page)))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest() throws Exception{
            log.info("findById Test");
            // given
            String id = anyString();

            given(facilityService.isExist(id)).willReturn(true);

            log.info("findById Test given: ✔");
            // when & then
            mvc.perform(get("/facility/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("findById Test when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findById Test - Failed")
        void findByIdTestFailed() throws Exception{
            log.info("findById Test - Failed");
            // given
            String id = anyString();

            given(facilityService.isExist(id)).willReturn(false);

            doThrow(new FacilityNotFoundException()).when(facilityService).findById(anyString());

            log.info("findById Test - Failed given: ✔");
            // when & then
            mvc.perform(get("/facility/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("findById Test - Failed when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByName Test")
        void findByNameTest() throws Exception{
            log.info("findByName Test");
            // given
            String name = "test";
            int size = 10;
            int page = 0;

            log.info("findByName Test given: ✔");
            // when & then
            mvc.perform(get("/facility/findByName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("name", name)
                    .param("size", eq(String.valueOf(size)))
                    .param("page", eq(String.valueOf(page))))
                    .andExpect(status().isOk());

            log.info("findByName Test when & then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("findByDormitoryId Test")
        void findByDormitoryIdTest() throws Exception {
            log.info("findByDormitoryId Test");
            // given
            String dormitoryId = "test";
            int size = 10;
            int page = 0;

            log.info("findByDormitoryId Test given: ✔");
            // when & then
            mvc.perform(get("/facility/findByDormitoryId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("dormitoryId", dormitoryId)
                    .param("size", eq(String.valueOf(size)))
                    .param("page", eq(String.valueOf(page)))
            ).andExpect(status().isOk());

            log.info("findByDormitoryId Test when & then: ✔");
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
            FacilityDTO facilityDTO = mock(FacilityDTO.class);

            given(facilityService.isExist(anyString())).willReturn(true);

            String requestBody = objectMapper.writeValueAsString(facilityDTO);

            log.info("update Test given: ✔");
            // when & then
            mvc.perform(put("/facility/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(requestBody))
                    .andExpect(status().isOk());

            verify(facilityService, times(1)).updateFacility(any());

            log.info("update Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("update Test - Failed")
        void updateTestFailed() throws Exception {
            log.info("update Test - Failed");
            // given
            FacilityDTO facilityDTO = mock(FacilityDTO.class);

            String requestBody = objectMapper.writeValueAsString(facilityDTO);

            given(facilityService.isExist(anyString())).willReturn(false);

            doThrow(new FacilityNotFoundException()).when(facilityService).updateFacility(any());

            log.info("update Test - Failed given: ✔");
            // when & then
            mvc.perform(put("/facility/update")
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
        @DisplayName("delete Test")
        void deleteTest() throws Exception {
            log.info("delete Test");
            // given
            String id = anyString();

            given(facilityService.isExist(id)).willReturn(true);

            log.info("delete Test given: ✔");
            // when & then
            mvc.perform(delete("/facility/delete")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            verify(facilityService, times(1)).deleteById(anyString());

            log.info("delete Test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("delete Test - Failed")
        void deleteTestFailed() throws Exception {
            log.info("delete Test - Failed");
            // given
            String id = anyString();

            given(facilityService.isExist(id)).willReturn(false);

            doThrow(new FacilityNotFoundException()).when(facilityService).deleteById(anyString());

            log.info("delete Test - Failed given: ✔");
            // when & then
            mvc.perform(delete("/facility/delete")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("delete Test - Failed when & then: ✔");
        }
    }
}
