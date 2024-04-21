package com.project.clickit.reservation;

import com.project.clickit.dto.ReservationDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.entity.ReservationEntity;
import com.project.clickit.entity.SeatEntity;
import com.project.clickit.exceptions.reservation.DuplicatedReservationException;
import com.project.clickit.exceptions.reservation.ReservationNotFoundException;
import com.project.clickit.repository.ReservationRepository;
import com.project.clickit.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@DisplayName("ReservationService Test")
@ExtendWith({MockitoExtension.class})
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("create Test")
        void createTest(){
            log.info("create Test");
            // given
            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .num(1)
                    .seatId("ping1_1")
                    .memberId("test_member_id")
                    .timestamp(LocalDateTime.now().minusDays(1))
                    .status("예약")
                    .build();

            Page<ReservationEntity> reservationEntities = mock();

            given(reservationRepository.findBySeatEntityIdAndToday(anyString(), any())).willReturn(reservationEntities);

            log.info("create Test given: ✓");
            // when
            reservationService.create(reservationDTO);

            log.info("create Test when: ✓");
            // then
            assertAll(
                    ()->verify(reservationRepository, times(1)).findBySeatEntityIdAndToday(anyString(), any()),
                    ()->verify(reservationRepository, times(1)).save(any())
            );

            log.info("create Test then: ✓");
        }

        @Test
        @Order(2)
        @DisplayName("create Test - ThrowException")
        void createTestWithNotEmpty(){
            log.info("create Test - ThrowException");
            // given
            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .num(1)
                    .seatId("ping1_1")
                    .memberId("test_member_id")
                    .timestamp(LocalDateTime.now().minusDays(1))
                    .status("예약")
                    .build();

            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .status("예약")
                    .build();

            Page<ReservationEntity> reservationEntities = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            given(reservationRepository.findBySeatEntityIdAndToday(anyString(), any())).willReturn(reservationEntities);

            log.info("create Test - ThrowException given: ✓");
            // when
            Throwable result = catchThrowable(()->reservationService.create(reservationDTO));

            log.info("create Test - ThrowException when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isInstanceOf(DuplicatedReservationException.class),
                    ()->verify(reservationRepository, times(1)).findBySeatEntityIdAndToday(anyString(), any()),
                    ()->verifyNoMoreInteractions(reservationRepository)
            );

            log.info("create Test - ThrowException then: ✓");
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class ReadTest{
        @Test
        @Order(1)
        @DisplayName("findAll Test")
        void findAllTest(){
            log.info("findAll Test");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            given(reservationRepository.findAll(Pageable.unpaged())).willReturn(reservationPage);

            log.info("findAll Test given: ✓");
            // when
            Page<ReservationDTO> result = reservationService.findAll(Pageable.unpaged());

            log.info("findAll Test when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isNotNull(),
                    ()->verify(reservationRepository, times(1)).findAll(Pageable.unpaged())
            );

            log.info("findAll Test then: ✓");
        }

        @Test
        @Order(2)
        @DisplayName("findByMemberId Test")
        void findByMemberIdTest(){
            log.info("findByMemberId Test");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            given(reservationRepository.findByMemberEntityId(anyString(), any())).willReturn(reservationPage);

            log.info("findByMemberId Test given: ✓");
            // when
            Page<ReservationDTO> result = reservationService.findByMemberId(anyString(), any());

            log.info("findByMemberId Test when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isNotNull(),
                    ()->verify(reservationRepository, times(1)).findByMemberEntityId(anyString(), any())
            );

            log.info("findByMemberId Test then: ✓");
        }

        @Test
        @Order(3)
        @DisplayName("findByMemberId Test - SecurityContextHolder")
        void findByMemberIdTestWithSecurityContextHolder(){
            log.info("findByMemberId Test - SecurityContextHolder");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

            given(reservationRepository.findByMemberEntityId(anyString(), any(Pageable.class))).willReturn(reservationPage);

            log.info("findByMemberId Test - SecurityContextHolder given: ✓");
            // when
            Page<ReservationDTO> result = reservationService.findByMemberId(Pageable.unpaged());

            log.info("findByMemberId Test - SecurityContextHolder when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isNotNull(),
                    ()->verify(reservationRepository, times(1)).findByMemberEntityId(anyString(), any(Pageable.class))
            );

            log.info("findByMemberId Test - SecurityContextHolder then: ✓");
        }

        @Test
        @Order(4)
        @DisplayName("findBySeatIdAndToday Test")
        void findBySeatIdAndTodayTest(){
            log.info("findBySeatIdAndToday Test");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            given(reservationRepository.findBySeatEntityIdAndToday(anyString(), any())).willReturn(reservationPage);

            log.info("findBySeatIdAndToday Test given: ✓");
            // when
            Page<ReservationDTO> result = reservationService.findBySeatIdAndToday(anyString(), any());

            log.info("findBySeatIdAndToday Test when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isNotNull(),
                    ()->verify(reservationRepository, times(1)).findBySeatEntityIdAndToday(anyString(), any())
            );

            log.info("findBySeatIdAndToday Test then: ✓");
        }

        @Test
        @Order(5)
        @DisplayName("findByMemberIdAndToday Test")
        void findByMemberIdAndTodayTest(){
            log.info("findByMemberIdAndToday Test");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            given(reservationRepository.findByMemberEntityIdAndToday(anyString(), any())).willReturn(reservationPage);

            log.info("findByMemberIdAndToday Test given: ✓");
            // when
            Page<ReservationDTO> result = reservationService.findByMemberIdAndToday(anyString(), any());

            log.info("findByMemberIdAndToday Test when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isNotNull(),
                    ()->verify(reservationRepository, times(1)).findByMemberEntityIdAndToday(anyString(), any())
            );

            log.info("findByMemberIdAndToday Test then: ✓");
        }

        @Test
        @Order(6)
        @DisplayName("findByMemberIdAndToday Test - SecurityContextHolder")
        void findByMemberIdAndTodayTestWithSecurityContextHolder(){
            log.info("findByMemberIdAndToday Test - SecurityContextHolder");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

            given(reservationRepository.findByMemberEntityIdAndToday(anyString(), any())).willReturn(reservationPage);

            log.info("findByMemberIdAndToday Test - SecurityContextHolder given: ✓");
            // when
            Page<ReservationDTO> result = reservationService.findByMemberIdAndToday(Pageable.unpaged());

            log.info("findByMemberIdAndToday Test - SecurityContextHolder when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isNotNull(),
                    ()->verify(reservationRepository, times(1)).findByMemberEntityIdAndToday(anyString(), any())
            );

            log.info("findByMemberIdAndToday Test - SecurityContextHolder then: ✓");
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("update Test")
        void updateTest(){
            log.info("update Test");
            // given
            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .num(1)
                    .seatId("ping1_1")
                    .memberId("test_member_id")
                    .timestamp(LocalDateTime.now().minusDays(1))
                    .status("예약")
                    .build();

            given(reservationRepository.save(any())).willReturn(mock(ReservationEntity.class));

            log.info("update Test given: ✓");
            // when
            reservationService.update(reservationDTO);

            log.info("update Test when: ✓");
            // then
            assertAll(
                    ()->verify(reservationRepository, times(1)).save(any())
            );

            log.info("update Test then: ✓");
        }

        @Test
        @Order(2)
        @DisplayName("updateStatus Test")
        void updateStatusTest(){
            log.info("updateStatus Test");
            // given
            ReservationEntity reservationEntity = ReservationEntity
                    .builder()
                    .num(1)
                    .seatEntity(mock(SeatEntity.class))
                    .memberEntity(mock(MemberEntity.class))
                    .build();

            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

            Page<ReservationEntity> reservationPage = new PageImpl<>(Collections.list(Collections.enumeration(List.of(reservationEntity))));

            given(reservationRepository.findByMemberEntityIdAndToday(anyString(), any())).willReturn(reservationPage);

            log.info("updateStatus Test given: ✓");
            // when
            reservationService.updateStatus(1, "취소");

            log.info("updateStatus Test when: ✓");
            // then
            assertAll(
                    ()->verify(reservationRepository, times(1)).findByMemberEntityIdAndToday(anyString(), any()),
                    ()->verify(reservationRepository, times(1)).updateReservationStatus(anyInt(), anyString())
            );

            log.info("updateStatus Test then: ✓");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("delete Test")
        void deleteTest(){
            log.info("delete Test");
            // given
            given(reservationRepository.existsById(anyInt())).willReturn(true);

            log.info("delete Test given: ✓");
            // when
            reservationService.delete(1);

            log.info("delete Test when: ✓");
            // then
            assertAll(
                    ()->verify(reservationRepository, times(1)).existsById(anyInt()),
                    ()->verify(reservationRepository, times(1)).deleteByNum(anyInt())
            );

            log.info("delete Test then: ✓");
        }

        @Test
        @Order(2)
        @DisplayName("delete Test - ThrowException")
        void deleteTestWithNotFoundException(){
            log.info("delete Test - ThrowException");
            // given
            given(reservationRepository.existsById(anyInt())).willReturn(false);

            log.info("delete Test - ThrowException given: ✓");
            // when
            Throwable result = catchThrowable(()->reservationService.delete(1));

            log.info("delete Test - ThrowException when: ✓");
            // then
            assertAll(
                    ()->assertThat(result).isInstanceOf(ReservationNotFoundException.class),
                    ()->verify(reservationRepository, times(1)).existsById(anyInt()),
                    ()->verifyNoMoreInteractions(reservationRepository)
            );

            log.info("delete Test - ThrowException then: ✓");
        }
    }
}
