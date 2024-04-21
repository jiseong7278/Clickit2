package com.project.clickit.seat;

import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.dto.SeatDTO;
import com.project.clickit.entity.SeatEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.repository.SeatRepository;
import com.project.clickit.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@DisplayName("SeatService Test")
@ExtendWith({MockitoExtension.class})
public class SeatServiceTest {
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Nested
    @DisplayName("isExist Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class isExistTest{
        @Test
        @Order(1)
        @DisplayName("isExist Test")
        void isExist(){
            log.info("isExist Test");
            // given
            String id = "ping1_1";

            given(seatRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┗ given(seatRepository.existsById(id)).willReturn(true);
                    """, id);
            // when
            Boolean result = seatService.isExist(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = seatService.isExist(id);
                    """);
            // then
            assertThat(result).isTrue();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isTrue();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("isExist Test(Not Exist)")
        void isExistTestNotExist(){
            log.info("isExist Test(Not Exist)");
            // given
            String id = "ping10_10";

            given(seatRepository.existsById(id)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┗ given(seatRepository.existsById(id)).willReturn(false);
                    """, id);
            // when
            Boolean result = seatService.isExist(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = seatService.isExist(id);
                    """);
            // then
            assertThat(result).isFalse();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isFalse();
                    """);
        }
    }

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("create Test")
        void createTest(){
            log.info("create Test");
            // given
            FacilityDTO facilityDTO = FacilityDTO.builder()
                    .id("dor_1_badminton")
                    .name("배드민턴장")
                    .build();

            SeatDTO seatDTO = SeatDTO.builder()
                    .id("test")
                    .name("test")
                    .time(1)
                    .isEmpty(true)
                    .facilityDTO(facilityDTO)
                    .build();

            given(seatRepository.existsById(seatDTO.getId())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ SeatDTO
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┣ name = {}
                    \t  ┃  ┣ time = {}
                    \t  ┃  ┣ isEmpty = {}
                    \t  ┃  ┗ facilityId = {}
                    \t  ┗ given(seatRepository.existsById(seatDTO.getId())).willReturn(false);
                    """, seatDTO.getId(), seatDTO.getName(), seatDTO.getTime(), seatDTO.getIsEmpty(), seatDTO.getFacilityDTO().getId());
            // when
            seatService.createSeat(seatDTO);

            log.info("""
                    
                    \twhen
                    \t  ┗ seatService.createSeat(seatDTO);
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).save(any(SeatEntity.class)),
                    () -> assertThatCode(() -> seatService.createSeat(seatDTO)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).save(any(SeatEntity.class));
                    \t  ┗ assertThatCode(() -> seatService.createSeat(seatDTO)).doesNotThrowAnyException();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("create Test(DuplicatedIdException)")
        void createTestWithDuplicatedId(){
            log.info("create Test(DuplicatedIdException)");
            // given
            String id = "bad1_1";

            given(seatRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┗ given(seatRepository.existsById(id)).willReturn(true);
                    """, id);
            // when
            Throwable result = catchThrowable(() -> seatService.createSeat(SeatDTO.builder().id(id).build()));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.createSeat(SeatDTO.builder().id(id).build()));
                    """);
            // then
            assertThat(result).isInstanceOf(DuplicatedIdException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(DuplicatedIdException.class);
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("createList Test")
        void createListTest(){
            log.info("createList Test");
            // given
            FacilityDTO facilityDTO = FacilityDTO.builder()
                    .id("dor_1_badminton")
                    .name("배드민턴장")
                    .build();

            SeatDTO seatDTO1 = SeatDTO.builder()
                    .id("test1")
                    .name("test1")
                    .time(1)
                    .isEmpty(true)
                    .facilityDTO(facilityDTO)
                    .build();

            SeatDTO seatDTO2 = SeatDTO.builder()
                    .id("test2")
                    .name("test2")
                    .time(1)
                    .isEmpty(true)
                    .facilityDTO(facilityDTO)
                    .build();

            List<SeatDTO> dormitoryDTOList = List.of(seatDTO1, seatDTO2);

            given(seatRepository.existsById(seatDTO1.getId())).willReturn(false);
            given(seatRepository.existsById(seatDTO2.getId())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ List<SeatDTO>
                    \t  ┃  ┣ SeatDTO1
                    \t  ┃  ┃  ┣ id = {}
                    \t  ┃  ┃  ┣ name = {}
                    \t  ┃  ┃  ┣ time = {}
                    \t  ┃  ┃  ┣ isEmpty = {}
                    \t  ┃  ┃  ┗ facilityDTO
                    \t  ┃  ┃     ┣ id = {}
                    \t  ┃  ┃     ┗ name = {}
                    \t  ┃  ┗ SeatDTO2
                    \t  ┃     ┣ id = {}
                    \t  ┃     ┣ name = {}
                    \t  ┃     ┣ time = {}
                    \t  ┃     ┣ isEmpty = {}
                    \t  ┃     ┗ facilityDTO
                    \t  ┃        ┣ id = {}
                    \t  ┃        ┗ name = {}
                    \t  ┗ given(seatRepository.existsById(seatDTO1.getId())).willReturn(false);
                    """, seatDTO1.getId(), seatDTO1.getName(), seatDTO1.getTime(), seatDTO1.getIsEmpty(), seatDTO1.getFacilityDTO().getId(), seatDTO1.getFacilityDTO().getName(),
                            seatDTO2.getId(), seatDTO2.getName(), seatDTO2.getTime(), seatDTO2.getIsEmpty(), seatDTO2.getFacilityDTO().getId(), seatDTO2.getFacilityDTO().getName());
            // when
            seatService.createList(dormitoryDTOList);

            log.info("""
                    
                    \twhen
                    \t  ┗ seatService.createList(dormitoryDTOList);
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).saveAll(any()),
                    () -> assertThatCode(() -> seatService.createList(dormitoryDTOList)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).saveAll(any());
                    \t  ┗ assertThatCode(() -> seatService.createList(dormitoryDTOList)).doesNotThrowAnyException();
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("createList Test(DuplicatedIdException)")
        void createListTestWithDuplicatedId() {
            log.info("createList Test(DuplicatedIdException)");
            // given
            FacilityDTO facilityDTO = FacilityDTO.builder()
                    .id("dor_1_badminton")
                    .name("배드민턴장")
                    .build();

            SeatDTO seatDTO1 = SeatDTO.builder()
                    .id("test1")
                    .name("test1")
                    .time(1)
                    .isEmpty(true)
                    .facilityDTO(facilityDTO)
                    .build();

            SeatDTO seatDTO2 = SeatDTO.builder()
                    .id("test2")
                    .name("test2")
                    .time(1)
                    .isEmpty(true)
                    .facilityDTO(facilityDTO)
                    .build();

            List<SeatDTO> dormitoryDTOList = List.of(seatDTO1, seatDTO2);

            given(seatRepository.existsById(seatDTO1.getId())).willReturn(true);

            log.info("""

                            \tgiven
                            \t  ┣ List<SeatDTO>
                            \t  ┃  ┣ SeatDTO1
                            \t  ┃  ┃  ┣ id = {}
                            \t  ┃  ┃  ┣ name = {}
                            \t  ┃  ┃  ┣ time = {}
                            \t  ┃  ┃  ┣ isEmpty = {}
                            \t  ┃  ┃  ┗ facilityDTO
                            \t  ┃  ┃     ┣ id = {}
                            \t  ┃  ┃     ┗ name = {}
                            \t  ┃  ┗ SeatDTO2
                            \t  ┃     ┣ id = {}
                            \t  ┃     ┣ name = {}
                            \t  ┃     ┣ time = {}
                            \t  ┃     ┣ isEmpty = {}
                            \t  ┃     ┗ facilityDTO
                            \t  ┃        ┣ id = {}
                            \t  ┃        ┗ name = {}
                            \t  ┗ given(seatRepository.existsById(seatDTO1.getId())).willReturn(true);
                            """, seatDTO1.getId(), seatDTO1.getName(), seatDTO1.getTime(), seatDTO1.getIsEmpty(), seatDTO1.getFacilityDTO().getId(), seatDTO1.getFacilityDTO().getName(),
                    seatDTO2.getId(), seatDTO2.getName(), seatDTO2.getTime(), seatDTO2.getIsEmpty(), seatDTO2.getFacilityDTO().getId(), seatDTO2.getFacilityDTO().getName());
            // when
            Throwable result = catchThrowable(() -> seatService.createList(dormitoryDTOList));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.createList(dormitoryDTOList));
                    """);
            // then
            assertThat(result).isInstanceOf(DuplicatedIdException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(DuplicatedIdException.class);
                    """);
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadTest{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest(){
            log.info("getAll Test");
            // given
            Page<SeatEntity> seatEntityPage = Page.empty();

            given(seatRepository.findAll(Pageable.unpaged())).willReturn(seatEntityPage);

            log.info("""
                    
                    \tgiven
                    \t  ┣ Page<SeatEntity> seatEntityPage = Page.empty();
                    \t  ┗ given(seatRepository.findAll(Pageable.unpaged())).willReturn(seatEntityPage);
                    """);
            // when
            Page<SeatDTO> result = seatService.getAll(Pageable.unpaged());

            log.info("""
                    
                    \twhen
                    \t  ┗ result = seatService.getAll(Pageable.unpaged());
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).findAll(Pageable.unpaged()),
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).findAll(Pageable.unpaged());
                    \t  ┣ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result).isInstanceOf(Page.class);
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest(){
            log.info("findById Test");
            // given

            SeatEntity seatEntity = SeatEntity.builder().build();

            given(seatRepository.existsById(anyString())).willReturn(true);
            given(seatRepository.findBySeatId(anyString())).willReturn(seatEntity);

            log.info("""
                    
                    \tgiven
                    \t  ┣ given(seatRepository.existsById(id)).willReturn(true);
                    \t  ┗ given(seatRepository.findBySeatId(id)).willReturn(any(SeatEntity.class));
                    """);
            // when
            SeatDTO result = seatService.findById(anyString());

            log.info("""
                    
                    \twhen
                    \t  ┗ result = seatService.findById(id);
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).existsById(anyString()),
                    () -> verify(seatRepository, times(1)).findBySeatId(anyString()),
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(SeatDTO.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).existsById(anyString());
                    \t  ┣ verify(seatRepository, times(1)).findBySeatId(anyString());
                    \t  ┣ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result).isInstanceOf(SeatDTO.class);
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("findById Test(Not Exist)")
        void findByIdTestWithNotExist(){
            log.info("findById Test(Not Exist)");
            // given
            given(seatRepository.existsById(anyString())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┗ given(seatRepository.existsById(anyString())).willReturn(false);
                    """);
            // when
            Throwable result = catchThrowable(() -> seatService.findById(anyString()));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.findById(anyString());
                    """);
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(Exception.class);
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("findByFacilityId Test")
        void findByFacilityIdTest(){
            log.info("findByFacilityId Test");
            // given
            String id = "dor_1_badminton";

            Page<SeatEntity> seatEntityPage = Page.empty();

            given(seatRepository.findByFacilityId(id, Pageable.unpaged())).willReturn(seatEntityPage);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┣ Page<SeatEntity> seatEntityPage = Page.empty();
                    \t  ┗ given(seatRepository.findByFacilityId(id, Pageable.unpaged())).willReturn(seatEntityPage);
                    """, id);
            // when
            Page<SeatDTO> result = seatService.findByFacilityId(id, Pageable.unpaged());

            log.info("""
                    
                    \twhen
                    \t  ┗ result = seatService.findByFacilityId(id, Pageable.unpaged());
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).findByFacilityId(id, Pageable.unpaged()),
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).findByFacilityId(id, Pageable.unpaged());
                    \t  ┣ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result).isInstanceOf(Page.class);
                    """);
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("updateSeat Test")
        void updateSeatTest(){
            log.info("updateSeat Test");
            // given
            FacilityDTO facilityDTO = FacilityDTO.builder()
                    .id("dor_1_badminton")
                    .name("배드민턴장")
                    .build();

            SeatDTO seatDTO = SeatDTO.builder()
                    .id("test")
                    .name("test")
                    .time(1)
                    .isEmpty(true)
                    .facilityDTO(facilityDTO)
                    .build();

            given(seatRepository.existsById(seatDTO.getId())).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ SeatDTO
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┣ name = {}
                    \t  ┃  ┣ time = {}
                    \t  ┃  ┣ isEmpty = {}
                    \t  ┃  ┗ facilityDTO
                    \t  ┃     ┣ id = {}
                    \t  ┃     ┗ name = {}
                    \t  ┗ given(seatRepository.existsById(seatDTO.getId())).willReturn(true);
                    """, seatDTO.getId(), seatDTO.getName(), seatDTO.getTime(), seatDTO.getIsEmpty(), seatDTO.getFacilityDTO().getId(), seatDTO.getFacilityDTO().getName());
            // when
            seatService.updateSeat(seatDTO);

            log.info("""
                    
                    \twhen
                    \t  ┗ seatService.updateSeat(seatDTO);
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).save(any(SeatEntity.class)),
                    () -> assertThatCode(() -> seatService.updateSeat(seatDTO)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).save(any(SeatEntity.class));
                    \t  ┗ assertThatCode(() -> seatService.updateSeat(seatDTO)).doesNotThrowAnyException();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("updateSeat Test(Not Exist)")
        void updateSeatTestWithNotExist(){
            log.info("updateSeat Test(Not Exist)");
            // given
            SeatDTO seatDTO = SeatDTO.builder()
                            .id(anyString())
                            .build();

            given(seatRepository.existsById(seatDTO.getId())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ SeatDTO
                    \t  ┃  ┗ id = anyString();
                    \t  ┗ given(seatRepository.existsById(seatDTO.getId())).willReturn(false);
                    """);
            // when
            Throwable result = catchThrowable(() -> seatService.updateSeat(seatDTO));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.updateSeat(seatDTO));
                    """);
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(ObjectNotFoundException.class);
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("updateSeatFacility Test")
        void updateSeatFacilityTest(){
            log.info("updateSeatFacility Test");
            // given
            String seatId = anyString();

            given(seatRepository.existsById(seatId)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ seatId = anyString();
                    \t  ┗ given(seatRepository.existsById(seatId)).willReturn(true);
                    """);
            // when
            seatService.updateSeatFacility(seatId, anyString());

            log.info("""
                    
                    \twhen
                    \t  ┗ seatService.updateSeatFacility(seatId, anyString());
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).updateSeatFacility(anyString(), anyString()),
                    () -> assertThatCode(() -> seatService.updateSeatFacility(seatId, anyString())).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).updateSeatFacility(anyString(), anyString());
                    \t  ┗ assertThatCode(() -> seatService.updateSeatFacility(seatId, anyString())).doesNotThrowAnyException();
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("updateSeatFacility Test(Not Exist)")
        void updateSeatFacilityTestWithNotExist(){
            log.info("updateSeatFacility Test(Not Exist)");
            // given
            String seatId = anyString();

            given(seatRepository.existsById(seatId)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ seatId = anyString();
                    \t  ┗ given(seatRepository.existsById(seatId)).willReturn(false);
                    """);
            // when
            Throwable result = catchThrowable(() -> seatService.updateSeatFacility(seatId, anyString()));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.updateSeatFacility(seatId, anyString()));
                    """);
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(ObjectNotFoundException.class);
                    """);
        }

        @Test
        @Order(5)
        @DisplayName("updateSeatIsEmpty Test")
        void updateSeatIsEmptyTest(){
            log.info("updateSeatIsEmpty Test");
            // given
            given(seatRepository.existsById(anyString())).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┗ given(seatRepository.existsById(anyString())).willReturn(true);
                    """);
            // when
            seatService.updateSeatIsEmpty(anyString(), true);

            log.info("""
                    
                    \twhen
                    \t  ┗ seatService.updateSeatIsEmpty(anyString(), true);
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).updateSeatIsEmpty(anyString(), anyBoolean()),
                    () -> assertThatCode(() -> seatService.updateSeatIsEmpty(anyString(), true)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).updateSeatIsEmpty(anyString(), anyBoolean());
                    \t  ┗ assertThatCode(() -> seatService.updateSeatIsEmpty(anyString(), true)).doesNotThrowAnyException();
                    """);
        }

        @Test
        @Order(6)
        @DisplayName("updateSeatIsEmpty Test(Not Exist)")
        void updateSeatIsEmptyTestWithNotExist(){
            log.info("updateSeatIsEmpty Test(Not Exist)");
            // given
            given(seatRepository.existsById(anyString())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┗ given(seatRepository.existsById(anyString())).willReturn(false);
                    """);
            // when
            Throwable result = catchThrowable(() -> seatService.updateSeatIsEmpty(anyString(), true));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.updateSeatIsEmpty(anyString(), true);
                    """);
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(ObjectNotFoundException.class);
                    """);
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("deleteById Test")
        void deleteByIdTest(){
            log.info("deleteById Test");
            // given
            given(seatRepository.existsById(anyString())).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┗ given(seatRepository.existsById(anyString())).willReturn(true);
                    """);
            // when
            seatService.deleteById(anyString());

            log.info("""
                    
                    \twhen
                    \t  ┗ seatService.deleteById(anyString());
                    """);
            // then
            assertAll(
                    () -> verify(seatRepository, times(1)).deleteById(anyString()),
                    () -> assertThatCode(() -> seatService.deleteById(anyString())).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ verify(seatRepository, times(1)).deleteById(anyString());
                    \t  ┗ assertThatCode(() -> seatService.deleteById(anyString())).doesNotThrowAnyException();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("deleteById Test(Not Exist)")
        void deleteByIdTestWithNotExist(){
            log.info("deleteById Test(Not Exist)");
            // given
            given(seatRepository.existsById(anyString())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┗ given(seatRepository.existsById(anyString())).willReturn(false);
                    """);
            // when
            Throwable result = catchThrowable(() -> seatService.deleteById(anyString()));

            log.info("""
                    
                    \twhen
                    \t  ┗ result = catchThrowable(() -> seatService.deleteById(anyString());
                    """);
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(ObjectNotFoundException.class);
                    """);
        }
    }
}
