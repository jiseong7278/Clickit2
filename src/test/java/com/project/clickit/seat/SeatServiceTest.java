//package com.project.clickit.seat;
//
//import com.project.clickit.dto.FacilityDTO;
//import com.project.clickit.dto.SeatDTO;
//import com.project.clickit.entity.SeatEntity;
//import com.project.clickit.exceptions.common.DuplicatedIdException;
//import com.project.clickit.exceptions.common.ObjectNotFoundException;
//import com.project.clickit.repository.SeatRepository;
//import com.project.clickit.service.SeatService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.*;
//
//
//@Slf4j
//@DisplayName("SeatService Test")
//@ExtendWith({MockitoExtension.class})
//public class SeatServiceTest {
//    @Mock
//    private SeatRepository seatRepository;
//
//    @InjectMocks
//    private SeatService seatService;
//
//    @Nested
//    @DisplayName("isExist Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class isExistTest{
//        @Test
//        @Order(1)
//        @DisplayName("isExist Test")
//        void isExist(){
//            log.info("isExist Test");
//            // given
//            String id = "ping1_1";
//
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("isExist Test given: ✔");
//            // when
//            Boolean result = seatService.isExist(id);
//
//            log.info("isExist Test when: ✔");
//            // then
//            assertThat(result).isTrue();
//
//            log.info("isExist Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("isExist Test(Not Exist)")
//        void isExistTestNotExist(){
//            log.info("isExist Test(Not Exist)");
//            // given
//            String id = "ping10_10";
//
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("isExist Test(Not Exist) | given: ✔");
//            // when
//            Boolean result = seatService.isExist(id);
//
//            log.info("isExist Test(Not Exist) | when: ✔");
//            // then
//            assertThat(result).isFalse();
//
//            log.info("isExist Test(Not Exist) | then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Create Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class CreateTest{
//        @Test
//        @Order(1)
//        @DisplayName("create Test")
//        void createTest(){
//            log.info("create Test");
//            // given
//            FacilityDTO facilityDTO = FacilityDTO.builder()
//                    .id("dor_1_badminton")
//                    .name("배드민턴장")
//                    .build();
//
//            SeatDTO seatDTO = SeatDTO.builder()
//                    .id("test")
//                    .name("test")
//                    .time(1)
//                    .isEmpty(true)
//                    .facilityDTO(facilityDTO)
//                    .build();
//
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("create Test given: ✔");
//            // when
//            seatService.createSeat(seatDTO);
//
//            log.info("create Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().save(any(SeatEntity.class)),
//                    () -> assertThatCode(() -> seatService.createSeat(seatDTO)).doesNotThrowAnyException()
//            );
//
//            log.info("create Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("create Test(DuplicatedIdException)")
//        void createTestWithDuplicatedId(){
//            log.info("create Test(DuplicatedIdException)");
//            // given
//            String id = "bad1_1";
//
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("create Test(DuplicatedIdException) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.createSeat(SeatDTO.builder().id(id).build()));
//
//            log.info("create Test(DuplicatedIdException) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(DuplicatedIdException.class);
//
//            log.info("create Test(DuplicatedIdException) | then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("createList Test")
//        void createListTest(){
//            log.info("createList Test");
//            // given
//            FacilityDTO facilityDTO = FacilityDTO.builder()
//                    .id("dor_1_badminton")
//                    .name("배드민턴장")
//                    .build();
//
//            SeatDTO seatDTO1 = SeatDTO.builder()
//                    .id("test1")
//                    .name("test1")
//                    .time(1)
//                    .isEmpty(true)
//                    .facilityDTO(facilityDTO)
//                    .build();
//
//            SeatDTO seatDTO2 = SeatDTO.builder()
//                    .id("test2")
//                    .name("test2")
//                    .time(1)
//                    .isEmpty(true)
//                    .facilityDTO(facilityDTO)
//                    .build();
//
//            List<SeatDTO> dormitoryDTOList = List.of(seatDTO1, seatDTO2);
//
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("createList Test given: ✔");
//            // when
//            seatService.createList(dormitoryDTOList);
//
//            log.info("createList Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().saveAll(anyList()),
//                    () -> assertThatCode(() -> seatService.createList(dormitoryDTOList)).doesNotThrowAnyException()
//            );
//
//            log.info("createList Test then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("createList Test(DuplicatedIdException)")
//        void createListTestWithDuplicatedId() {
//            log.info("createList Test(DuplicatedIdException)");
//            // given
//            FacilityDTO facilityDTO = FacilityDTO.builder()
//                    .id("dor_1_badminton")
//                    .name("배드민턴장")
//                    .build();
//
//            SeatDTO seatDTO1 = SeatDTO.builder()
//                    .id("test1")
//                    .name("test1")
//                    .time(1)
//                    .isEmpty(true)
//                    .facilityDTO(facilityDTO)
//                    .build();
//
//            SeatDTO seatDTO2 = SeatDTO.builder()
//                    .id("test2")
//                    .name("test2")
//                    .time(1)
//                    .isEmpty(true)
//                    .facilityDTO(facilityDTO)
//                    .build();
//
//            List<SeatDTO> dormitoryDTOList = List.of(seatDTO1, seatDTO2);
//
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("createList Test(DuplicatedIdException) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.createList(dormitoryDTOList));
//
//            log.info("createList Test(DuplicatedIdException) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(DuplicatedIdException.class);
//
//            log.info("createList Test(DuplicatedIdException) | then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Read Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class ReadTest{
//        @Test
//        @Order(1)
//        @DisplayName("getAll Test")
//        void getAllTest(){
//            log.info("getAll Test");
//            // given
//            Page<SeatEntity> seatEntityPage = Page.empty();
//
//            given(seatRepository.findAll(any(Pageable.class))).willReturn(seatEntityPage);
//
//            log.info("getAll Test given: ✔");
//            // when
//            Page<SeatDTO> result = seatService.getAll(Pageable.unpaged());
//
//            log.info("getAll Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().findAll(any(Pageable.class)),
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(Page.class)
//            );
//
//            log.info("getAll Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("findById Test")
//        void findByIdTest(){
//            log.info("findById Test");
//            // given
//
//            SeatEntity seatEntity = SeatEntity.builder().build();
//
//            given(seatRepository.existsById(anyString())).willReturn(true);
//            given(seatRepository.findBySeatId(anyString())).willReturn(seatEntity);
//
//            log.info("findById Test given: ✔");
//            // when
//            SeatDTO result = seatService.findById(anyString());
//
//            log.info("findById Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().existsById(anyString()),
//                    () -> then(seatRepository).should().findBySeatId(anyString()),
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(SeatDTO.class)
//            );
//
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("findById Test(Not Exist)")
//        void findByIdTestWithNotExist(){
//            log.info("findById Test(Not Exist)");
//            // given
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("findById Test(Not Exist) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.findById(anyString()));
//
//            log.info("findById Test(Not Exist) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(ObjectNotFoundException.class);
//
//            log.info("findById Test(Not Exist) | then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("findByFacilityId Test")
//        void findByFacilityIdTest(){
//            log.info("findByFacilityId Test");
//            // given
//            String id = "dor_1_badminton";
//
//            Page<SeatEntity> seatEntityPage = Page.empty();
//
//            given(seatRepository.findByFacilityId(anyString(), any(Pageable.class))).willReturn(seatEntityPage);
//
//            log.info("findByFacilityId Test given: ✔");
//            // when
//            Page<SeatDTO> result = seatService.findByFacilityId(id, Pageable.unpaged());
//
//            log.info("findByFacilityId Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().findByFacilityId(id, Pageable.unpaged()),
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(Page.class)
//            );
//
//            log.info("findByFacilityId Test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Update Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class UpdateTest{
//        @Test
//        @Order(1)
//        @DisplayName("updateSeat Test")
//        void updateSeatTest(){
//            log.info("updateSeat Test");
//            // given
//            FacilityDTO facilityDTO = FacilityDTO.builder()
//                    .id("dor_1_badminton")
//                    .name("배드민턴장")
//                    .build();
//
//            SeatDTO seatDTO = SeatDTO.builder()
//                    .id("test")
//                    .name("test")
//                    .time(1)
//                    .isEmpty(true)
//                    .facilityDTO(facilityDTO)
//                    .build();
//
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("updateSeat Test given: ✔");
//            // when
//            seatService.updateSeat(seatDTO);
//
//            log.info("updateSeat Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().save(any(SeatEntity.class)),
//                    () -> assertThatCode(() -> seatService.updateSeat(seatDTO)).doesNotThrowAnyException()
//            );
//
//            log.info("updateSeat Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("updateSeat Test(Not Exist)")
//        void updateSeatTestWithNotExist(){
//            log.info("updateSeat Test(Not Exist)");
//            // given
//            SeatDTO seatDTO = SeatDTO.builder()
//                            .id("test")
//                            .build();
//
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("updateSeat Test(Not Exist) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.updateSeat(seatDTO));
//
//            log.info("updateSeat Test(Not Exist) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(ObjectNotFoundException.class);
//
//            log.info("updateSeat Test(Not Exist) | then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("updateSeatFacility Test")
//        void updateSeatFacilityTest(){
//            log.info("updateSeatFacility Test");
//            // given
//            String seatId = "test";
//
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("updateSeatFacility Test given: ✔");
//            // when
//            seatService.updateSeatFacility(seatId, anyString());
//
//            log.info("updateSeatFacility Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().updateSeatFacility(anyString(), anyString()),
//                    () -> assertThatCode(() -> seatService.updateSeatFacility(seatId, anyString())).doesNotThrowAnyException()
//            );
//
//            log.info("updateSeatFacility Test then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("updateSeatFacility Test(Not Exist)")
//        void updateSeatFacilityTestWithNotExist(){
//            log.info("updateSeatFacility Test(Not Exist)");
//            // given
//            String seatId = "test";
//
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("updateSeatFacility Test(Not Exist) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.updateSeatFacility(seatId, anyString()));
//
//            log.info("updateSeatFacility Test(Not Exist) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(ObjectNotFoundException.class);
//
//            log.info("updateSeatFacility Test(Not Exist) | then: ✔");
//        }
//
//        @Test
//        @Order(5)
//        @DisplayName("updateSeatIsEmpty Test")
//        void updateSeatIsEmptyTest(){
//            log.info("updateSeatIsEmpty Test");
//            // given
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("updateSeatIsEmpty Test given: ✔");
//            // when
//            seatService.updateSeatIsEmpty(anyString(), true);
//
//            log.info("updateSeatIsEmpty Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().updateSeatIsEmpty(anyString(), anyBoolean()),
//                    () -> assertThatCode(() -> seatService.updateSeatIsEmpty(anyString(), true)).doesNotThrowAnyException()
//            );
//
//            log.info("updateSeatIsEmpty Test then: ✔");
//        }
//
//        @Test
//        @Order(6)
//        @DisplayName("updateSeatIsEmpty Test(Not Exist)")
//        void updateSeatIsEmptyTestWithNotExist(){
//            log.info("updateSeatIsEmpty Test(Not Exist)");
//            // given
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("updateSeatIsEmpty Test(Not Exist) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.updateSeatIsEmpty(anyString(), true));
//
//            log.info("updateSeatIsEmpty Test(Not Exist) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(ObjectNotFoundException.class);
//
//            log.info("updateSeatIsEmpty Test(Not Exist) | then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Delete Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class DeleteTest{
//        @Test
//        @Order(1)
//        @DisplayName("deleteById Test")
//        void deleteByIdTest(){
//            log.info("deleteById Test");
//            // given
//            given(seatRepository.existsById(anyString())).willReturn(true);
//
//            log.info("deleteById Test given: ✔");
//            // when
//            seatService.deleteById(anyString());
//
//            log.info("deleteById Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(seatRepository).should().deleteById(anyString()),
//                    () -> assertThatCode(() -> seatService.deleteById(anyString())).doesNotThrowAnyException()
//            );
//
//            log.info("deleteById Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("deleteById Test(Not Exist)")
//        void deleteByIdTestWithNotExist(){
//            log.info("deleteById Test(Not Exist)");
//            // given
//            given(seatRepository.existsById(anyString())).willReturn(false);
//
//            log.info("deleteById Test(Not Exist) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> seatService.deleteById(anyString()));
//
//            log.info("deleteById Test(Not Exist) | when: ✔");
//            // then
//            assertThat(result).isInstanceOf(ObjectNotFoundException.class);
//
//            log.info("deleteById Test(Not Exist) | then: ✔");
//        }
//    }
//}
