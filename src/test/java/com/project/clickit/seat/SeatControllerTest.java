package com.project.clickit.seat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.SeatController;
import com.project.clickit.dto.SeatDTO;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.SeatService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = SeatController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class SeatControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeatService seatService;

    @Nested
    @DisplayName("Duplication Check Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DuplicationCheckTest{
        @Test
        @Order(1)
        @DisplayName("duplicationCheck Test - ok")
        void duplicationCheckTestOk() throws Exception {
            log.info("duplicationCheck Test - ok");
            // given
            String id = "A1";

            given(seatService.isExist(anyString())).willReturn(false);

            log.info("duplicationCheck Test - ok | given: ✔");
            // when & then
            mvc.perform(get("/seat/duplicateCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("duplicationCheck Test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("duplicationCheck Test - badRequest")
        void duplicationCheckTestBadRequest() throws Exception {
            log.info("duplicationCheck Test - badRequest");
            // given
            String id = "A1";

            given(seatService.isExist(anyString())).willReturn(true);

            log.info("duplicationCheck Test - badRequest | given: ✔");
            // when & then
            mvc.perform(get("/seat/duplicateCheck")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("duplicationCheck Test - badRequest | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("createSeat Test - ok")
        void createSeatTestOk() throws Exception {
            log.info("createSeat Test - ok");
            // given
            SeatDTO seatDTO = mock(SeatDTO.class);

            willDoNothing().given(seatService).createSeat(any(SeatDTO.class));

            log.info("createSeat Test - ok | given: ✔");
            // when & then
            mvc.perform(post("/seat/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(seatDTO)))
                    .andExpect(status().isOk());

            log.info("createSeat Test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("createSeat Test - badRequest")
        void createSeatTestBadRequest() throws Exception {
            log.info("createSeat Test - badRequest");
            // given
            SeatDTO seatDTO = mock(SeatDTO.class);

            willThrow(new DuplicatedIdException(ErrorCode.DUPLICATED_ID)).given(seatService).createSeat(any(SeatDTO.class));

            log.info("createSeat Test - badRequest | given: ✔");
            // when & then
            mvc.perform(post("/seat/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(seatDTO)))
                    .andExpect(status().isBadRequest());

            log.info("createSeat Test - badRequest | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("createList Test - ok")
        void createListTestOk() throws Exception {
            log.info("createList Test - ok");
            // given
            SeatDTO seatDTO1 = mock(SeatDTO.class);
            SeatDTO seatDTO2 = mock(SeatDTO.class);

            List<SeatDTO> seatDTOList = List.of(seatDTO1, seatDTO2);

            willDoNothing().given(seatService).createList(anyList());

            log.info("createList Test - ok | given: ✔");
            // when & then
            mvc.perform(post("/seat/createList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(seatDTOList)))
                    .andExpect(status().isOk());

            log.info("createList Test - ok | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("createList Test - badRequest")
        void createListTestBadRequest() throws Exception {
            log.info("createList Test - badRequest");
            // given
            SeatDTO seatDTO1 = mock(SeatDTO.class);
            SeatDTO seatDTO2 = mock(SeatDTO.class);

            List<SeatDTO> seatDTOList = List.of(seatDTO1, seatDTO2);

            willThrow(new DuplicatedIdException(ErrorCode.DUPLICATED_ID)).given(seatService).createList(anyList());

            log.info("createList Test - badRequest | given: ✔");
            // when & then
            mvc.perform(post("/seat/createList")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(seatDTOList)))
                    .andExpect(status().isBadRequest());

            log.info("createList Test - badRequest | when & then: ✔");
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

            given(seatService.getAll(any())).willReturn(Page.empty());

            log.info("getAll Test | given: ✔");
            // when & then
            mvc.perform(get("/seat/getAll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("page", String.valueOf(page))
                    .param("size", String.valueOf(size))
            )
                    .andExpect(status().isOk());
        }

        @Test
        @Order(2)
        @DisplayName("findById Test - ok")
        void findByIdTestOk() throws Exception {
            log.info("findById Test - ok");
            // given
            String id = "A1";

            given(seatService.findById(anyString())).willReturn(mock(SeatDTO.class));

            log.info("findById Test - ok | given: ✔");
            // when & then
            mvc.perform(get("/seat/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isOk());

            log.info("findById Test - ok | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findById Test - SeatNotFoundException")
        void findByIdTestNotFound() throws Exception {
            log.info("findById Test - SeatNotFoundException");
            // given
            String id = "A1";

            given(seatService.findById(anyString())).willThrow(new ObjectNotFoundException(ErrorCode.SEAT_NOT_FOUND));

            log.info("findById Test - SeatNotFoundException | given: ✔");
            // when & then
            mvc.perform(get("/seat/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("findById Test - SeatNotFoundException | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByFacilityId Test")
        void findByFacilityIdTest() throws Exception {
            log.info("findByFacilityId Test");
            // given
            int size = 10;
            int page = 0;

            String facilityId = "A";

            given(seatService.findByFacilityId(anyString(), any())).willReturn(Page.empty());

            log.info("findByFacilityId Test | given: ✔");
            // when & then
            mvc.perform(get("/seat/findByFacilityId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("page", String.valueOf(page))
                    .param("size", String.valueOf(size))
                    .param("facilityId", facilityId))
                    .andExpect(status().isOk());

            log.info("findByFacilityId Test | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest {
        @Test
        @Order(1)
        @DisplayName("updateSeat Test - ok")
        void updateSeatTestOk() throws Exception {
            log.info("updateSeat Test - ok");
            // given
            SeatDTO seatDTO = mock(SeatDTO.class);

            willDoNothing().given(seatService).updateSeat(any(SeatDTO.class));

            log.info("updateSeat Test - ok | given: ✔");
            // when & then
            mvc.perform(put("/seat/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(seatDTO)))
                    .andExpect(status().isOk());

            log.info("updateSeat Test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("updateSeat Test - SeatNotFoundException")
        void updateSeatTestNotFound() throws Exception {
            log.info("updateSeat Test - SeatNotFoundException");
            // given
            SeatDTO seatDTO = mock(SeatDTO.class);

            willThrow(new ObjectNotFoundException(ErrorCode.SEAT_NOT_FOUND)).given(seatService).updateSeat(any(SeatDTO.class));

            log.info("updateSeat Test - SeatNotFoundException | given: ✔");
            // when & then
            mvc.perform(put("/seat/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(seatDTO)))
                    .andExpect(status().isBadRequest());

            log.info("updateSeat Test - SeatNotFoundException | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("updateSeatFacility Test - ok")
        void updateSeatFacilityTestOk() throws Exception {
            log.info("updateSeatFacility Test - ok");
            // given
            String id = "A1";
            String facilityId = "A";

            willDoNothing().given(seatService).updateSeatFacility(anyString(), anyString());

            log.info("updateSeatFacility Test - ok | given: ✔");
            // when & then
            mvc.perform(put("/seat/updateSeatFacility")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id)
                            .param("facilityId", facilityId))
                    .andExpect(status().isOk());

            log.info("updateSeatFacility Test - ok | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("updateSeatFacility Test - SeatNotFoundException")
        void updateSeatFacilityTestNotFound() throws Exception {
            log.info("updateSeatFacility Test - SeatNotFoundException");
            // given
            String id = "A1";
            String facilityId = "A";

            willThrow(new ObjectNotFoundException(ErrorCode.SEAT_NOT_FOUND)).given(seatService).updateSeatFacility(anyString(), anyString());

            log.info("updateSeatFacility Test - SeatNotFoundException | given: ✔");
            // when & then
            mvc.perform(put("/seat/updateSeatFacility")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id)
                            .param("facilityId", facilityId))
                    .andExpect(status().isBadRequest());

            log.info("updateSeatFacility Test - SeatNotFoundException | when & then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("updateSeatIsEmpty Test - ok")
        void updateSeatIsEmptyTestOk() throws Exception {
            log.info("updateSeatIsEmpty Test - ok");
            // given
            String id = "A1";
            boolean isEmpty = true;

            willDoNothing().given(seatService).updateSeatIsEmpty(anyString(), anyBoolean());

            log.info("updateSeatIsEmpty Test - ok | given: ✔");
            // when & then
            mvc.perform(put("/seat/updateIsEmpty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id)
                            .param("isEmpty", String.valueOf(isEmpty)))
                    .andExpect(status().isOk());

            log.info("updateSeatIsEmpty Test - ok | when & then: ✔");
        }

        @Test
        @Order(6)
        @DisplayName("updateSeatIsEmpty Test - SeatNotFoundException")
        void updateSeatIsEmptyTestNotFound() throws Exception {
            log.info("updateSeatIsEmpty Test - SeatNotFoundException");
            // given
            String id = "A1";
            boolean isEmpty = true;

            willThrow(new ObjectNotFoundException(ErrorCode.SEAT_NOT_FOUND)).given(seatService).updateSeatIsEmpty(anyString(), anyBoolean());

            log.info("updateSeatIsEmpty Test - SeatNotFoundException | given: ✔");
            // when & then
            mvc.perform(put("/seat/updateIsEmpty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id)
                            .param("isEmpty", String.valueOf(isEmpty)))
                    .andExpect(status().isBadRequest());

            log.info("updateSeatIsEmpty Test - SeatNotFoundException | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest {
        @Test
        @Order(1)
        @DisplayName("deleteSeat Test - ok")
        void deleteSeatTestOk() throws Exception {
            log.info("deleteSeat Test - ok");
            // given
            String id = "A1";

            willDoNothing().given(seatService).deleteById(anyString());

            log.info("deleteSeat Test - ok | given: ✔");
            // when & then
            mvc.perform(delete("/seat/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isOk());

            log.info("deleteSeat Test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("deleteSeat Test - SeatNotFoundException")
        void deleteSeatTestNotFound() throws Exception {
            log.info("deleteSeat Test - SeatNotFoundException");
            // given
            String id = "A1";

            willThrow(new ObjectNotFoundException(ErrorCode.SEAT_NOT_FOUND)).given(seatService).deleteById(anyString());

            log.info("deleteSeat Test - SeatNotFoundException | given: ✔");
            // when & then
            mvc.perform(delete("/seat/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .param("id", id))
                    .andExpect(status().isBadRequest());

            log.info("deleteSeat Test - SeatNotFoundException | when & then: ✔");
        }
    }
}
