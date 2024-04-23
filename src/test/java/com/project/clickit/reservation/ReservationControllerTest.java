package com.project.clickit.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.clickit.configs.SecurityConfig;
import com.project.clickit.controller.ReservationController;
import com.project.clickit.dto.ReservationDTO;
import com.project.clickit.exceptions.reservation.DuplicatedReservationException;
import com.project.clickit.exceptions.reservation.ReservationNotFoundException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.ReservationService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = ReservationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("create Test - ok")
        void createTest() throws Exception{
            log.info("create Test - ok");
            // given
            ReservationDTO reservationDTO = mock(ReservationDTO.class);

            doNothing().when(reservationService).create(any(ReservationDTO.class));

            log.info("create test - ok | given: ✔");
            // when & then
            mvc.perform(post("/reservation/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(reservationDTO)))
                    .andExpect(status().isOk());

            log.info("create test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("create Test - bad request")
        void createTestBadRequest() throws Exception{
            log.info("create Test - bad request");
            // given
            ReservationDTO reservationDTO = mock(ReservationDTO.class);

            doThrow(new DuplicatedReservationException()).when(reservationService).create(any(ReservationDTO.class));

            log.info("create test - bad request | given: ✔");
            // when & then
            mvc.perform(post("/reservation/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(reservationDTO)))
                    .andExpect(status().isBadRequest());

            log.info("create test - bad request | when & then: ✔");
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
            when(reservationService.findAll(any())).thenReturn(Page.empty());

            log.info("getAll test given: ✔");
            // when & then
            mvc.perform(get("/reservation/getAll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());

            log.info("getAll test when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findByMemberId Test")
        void findByMemberIdTest() throws Exception{
            log.info("findByMemberId Test");
            // given
            String memberId = "test";

            when(reservationService.findByMemberId(any(), any())).thenReturn(Page.empty());

            log.info("findByMemberId test given: ✔");
            // when & then
            mvc.perform(get("/reservation/findByMemberId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("memberId", memberId)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());

            log.info("findByMemberId test when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findMyReservation Test")
        void findMyReservationTest() throws Exception{
            log.info("findMyReservation Test");
            // given
            when(reservationService.findByMemberId(any())).thenReturn(Page.empty());

            log.info("findMyReservation test given: ✔");
            // when & then
            mvc.perform(get("/reservation/findMyReservation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());

            log.info("findMyReservation test when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findBySeatIdAndToday Test")
        void findBySeatIdAndTodayTest() throws Exception{
            log.info("findBySeatIdAndToday Test");
            // given
            String seatId = "test";

            when(reservationService.findBySeatIdAndToday(any(), any())).thenReturn(Page.empty());

            log.info("findBySeatIdAndToday test given: ✔");
            // when & then
            mvc.perform(get("/reservation/findBySeatIdAndToday")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("seatId", seatId)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());

            log.info("findBySeatIdAndToday test when & then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("findByMemberIdAndToday Test")
        void findByMemberIdAndTodayTest() throws Exception{
            log.info("findByMemberIdAndToday Test");
            // given
            String memberId = "test";

            when(reservationService.findByMemberIdAndToday(any(), any())).thenReturn(Page.empty());

            log.info("findByMemberIdAndToday test given: ✔");
            // when & then
            mvc.perform(get("/reservation/findByMemberIdAndToday")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("memberId", memberId)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());

            log.info("findByMemberIdAndToday test when & then: ✔");
        }

        @Test
        @Order(6)
        @DisplayName("findMyReservationToday Test")
        void findMyReservationTodayTest() throws Exception{
            log.info("findMyReservationToday Test");
            // given
            when(reservationService.findByMemberIdAndToday(any())).thenReturn(Page.empty());

            log.info("findMyReservationToday test given: ✔");
            // when & then
            mvc.perform(get("/reservation/findMyReservationToday")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk());

            log.info("findMyReservationToday test when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("update Test - ok")
        void updateTest() throws Exception{
            log.info("update Test - ok");
            // given
            ReservationDTO reservationDTO = mock(ReservationDTO.class);

            doNothing().when(reservationService).update(any(ReservationDTO.class));

            log.info("update test - ok | given: ✔");
            // when & then
            mvc.perform(put("/reservation/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(reservationDTO)))
                    .andExpect(status().isOk());

            log.info("update test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("update Test - badRequest")
        void updateTestBadRequest() throws Exception{
            log.info("update Test - badRequest");
            // given
            ReservationDTO reservationDTO = mock(ReservationDTO.class);

            doThrow(new ReservationNotFoundException()).when(reservationService).update(any(ReservationDTO.class));

            log.info("update test - badRequest | given: ✔");
            // when & then
            mvc.perform(put("/reservation/updateStatus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(reservationDTO)))
                    .andExpect(status().isBadRequest());

            log.info("update test - badRequest | when & then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("updateStatus Test - ok")
        void updateStatusTest() throws Exception{
            log.info("updateStatus Test - ok");
            // given
            ReservationDTO reservationDTO1 = mock(ReservationDTO.class);
            ReservationDTO reservationDTO2 = mock(ReservationDTO.class);

            List<ReservationDTO> reservationDTOList = List.of(reservationDTO1, reservationDTO2);

            doNothing().when(reservationService).updateStatus(any(), any());

            log.info("updateStatus test - ok | given: ✔");
            // when & then
            mvc.perform(put("/reservation/updateStatus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(reservationDTOList)))
                    .andExpect(status().isOk());

            log.info("updateStatus test - ok | when & then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("updateStatus Test - badRequest")
        void updateStatusTestBadRequest() throws Exception{
            log.info("updateStatus Test - badRequest");
            // given
            ReservationDTO reservationDTO1 = mock(ReservationDTO.class);
            ReservationDTO reservationDTO2 = mock(ReservationDTO.class);

            List<ReservationDTO> reservationDTOList = List.of(reservationDTO1, reservationDTO2);

            doThrow(new ReservationNotFoundException()).when(reservationService).updateStatus(any(), any());

            log.info("updateStatus test - badRequest | given: ✔");
            // when & then
            mvc.perform(put("/reservation/updateStatus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(reservationDTOList)))
                    .andExpect(status().isBadRequest());

            log.info("updateStatus test - badRequest | when & then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("delete Test - ok")
        void deleteTest() throws Exception{
            log.info("delete Test - ok");
            // given
            int num = 1;

            doNothing().when(reservationService).delete(anyInt());

            log.info("delete test - ok | given: ✔");
            // when & then
            mvc.perform(delete("/reservation/delete")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("num", String.valueOf(num)))
                    .andExpect(status().isOk());

            log.info("delete test - ok | when & then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("delete Test - badRequest")
        void deleteTestBadRequest() throws Exception{
            log.info("delete Test - badRequest");
            // given
            int num = 1;

            doThrow(new ReservationNotFoundException()).when(reservationService).delete(anyInt());

            log.info("delete test - badRequest | given: ✔");
            // when & then
            mvc.perform(delete("/reservation/delete")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .param("num", String.valueOf(num)))
                    .andExpect(status().isBadRequest());

            log.info("delete test - badRequest | when & then: ✔");
        }
    }
}
