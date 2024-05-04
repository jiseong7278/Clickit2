//package com.project.clickit.facility;
//
//import com.project.clickit.dto.DormitoryDTO;
//import com.project.clickit.dto.FacilityDTO;
//import com.project.clickit.entity.DormitoryEntity;
//import com.project.clickit.entity.FacilityEntity;
//import com.project.clickit.exceptions.common.DuplicatedIdException;
//import com.project.clickit.exceptions.common.ObjectNotFoundException;
//import com.project.clickit.repository.FacilityRepository;
//import com.project.clickit.service.FacilityService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@Slf4j
//@DisplayName("FacilityService 테스트")
//@ExtendWith({MockitoExtension.class})
//public class FacilityServiceTest {
//    @Mock
//    private FacilityRepository facilityRepository;
//
//    @InjectMocks
//    private FacilityService facilityService;
//
//    @Nested
//    @DisplayName("isExist Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class isExist{
//        @Test
//        @Order(1)
//        @DisplayName("isExist Test")
//        void isExistTest(){
//            log.info("isExist Test");
//            // given
//            String facilityId = "dor_1_badminton";
//
//            given(facilityRepository.existsById(anyString())).willReturn(true);
//
//            log.info("isExist Test given: ✔");
//            // when
//            Boolean result = facilityService.isExist(facilityId);
//
//            log.info("isExist Test when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(Boolean.class),
//                    () -> assertThat(result).isTrue(),
//                    () -> then(facilityRepository).should().existsById(anyString())
//            );
//
//            log.info("isExist Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("isExist Test: true(중복 o)")
//        void isExistTestDuplicated(){
//            log.info("isExist Test: true(중복 o)");
//            // given
//            String facilityId = "never_used_id";
//
//            given(facilityRepository.existsById(anyString())).willReturn(false);
//
//            log.info("isExist Test: true(중복 o) | given: ✔");
//            // when
//            Boolean result = facilityService.isExist(facilityId);
//
//            log.info("isExist Test: true(중복 o) | when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(Boolean.class),
//                    () -> assertThat(result).isFalse(),
//                    () -> then(facilityRepository).should().existsById(anyString())
//            );
//
//            log.info("isExist Test: true(중복 o) | then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Create Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class CreateTest{
//        @Test
//        @Order(1)
//        @DisplayName("createFacility Test")
//        void createFacilityTest(){
//            log.info("createFacility Test");
//            // given
//            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
//                    .id("dor_1")
//                    .name("기숙사1")
//                    .build();
//
//            FacilityDTO facilityDTO = FacilityDTO.builder()
//                    .id("dor_1_tennis")
//                    .name("테니스장")
//                    .info("테니스장입니다.")
//                    .open(6)
//                    .close(18)
//                    .img("테니스장 이미지")
//                    .terms("테니스장 이용 조건")
//                    .dormitoryDTO(dormitoryDTO)
//                    .build();
//
//            given(facilityRepository.existsById(anyString())).willReturn(false);
//
//            log.info("createFacility Test given: ✔");
//            // when
//            facilityService.createFacility(facilityDTO);
//
//            log.info("createFacility Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(facilityRepository).should().save(any(FacilityEntity.class)),
//                    () -> assertThatCode(() -> facilityService.createFacility(facilityDTO)).doesNotThrowAnyException()
//            );
//
//            log.info("createFacility Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("createFacility Test (중복된 아이디)")
//        void createFacilityTestWithDuplicatedId(){
//            log.info("createFacility Test (중복된 아이디)");
//            // given
//            FacilityDTO facilityDTO = FacilityDTO.builder()
//                    .id("dor_1_badminton")
//                    .build();
//
//            given(facilityRepository.existsById(anyString())).willReturn(true);
//
//            log.info("createFacility Test (중복된 아이디) | given: ✔");
//            // when
//            Throwable result = catchThrowable(()->facilityService.createFacility(facilityDTO));
//
//            log.info("createFacility Test (중복된 아이디) | when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class),
//                    () -> then(facilityRepository).shouldHaveNoMoreInteractions()
//            );
//
//            log.info("createFacility Test (중복된 아이디) | then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Read Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class ReadTest {
//        @Test
//        @Order(1)
//        @DisplayName("getAll Test")
//        void getAllTest(){
//            log.info("getAll Test");
//            // given
//            given(facilityRepository.findAll(any(Pageable.class))).willReturn(Page.empty());
//
//            log.info("getAll Test given: ✔");
//            // when
//            Page<FacilityDTO> result = facilityService.getAll(Pageable.unpaged());
//
//            log.info("getAll Test when: ✔");
//            // then
//            assertAll(
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
//        void findByIdTest() {
//            log.info("findById Test");
//            // given
//            String facilityId = "dor_1_badminton";
//
//            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
//                    .id("dor_1")
//                    .name("기숙사1")
//                    .build();
//
//            given(facilityRepository.existsById(anyString())).willReturn(true);
//
//            given(facilityRepository.findByFacilityId(anyString())).willReturn(FacilityEntity.builder().dormitoryEntity(dormitoryEntity).build());
//
//            log.info("findById Test given: ✔");
//            // when
//            FacilityDTO result = facilityService.findById(facilityId);
//
//            log.info("findById Test when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(FacilityDTO.class)
//            );
//
//            log.info("findById Test then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("findById Test (존재하지 않는 아이디)")
//        void findByIdTestWithNotExistId() {
//            log.info("findById Test (존재하지 않는 아이디)");
//            // given
//            String facilityId = "never_used_id";
//
//            given(facilityRepository.existsById(anyString())).willReturn(false);
//
//            log.info("findById Test (존재하지 않는 아이디) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> facilityService.findById(facilityId));
//
//            log.info("findById Test (존재하지 않는 아이디) | when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isInstanceOf(ObjectNotFoundException.class),
//                    () -> then(facilityRepository).shouldHaveNoMoreInteractions()
//            );
//
//            log.info("findById Test (존재하지 않는 아이디) | then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("findByName Test")
//        void findByNameTest(){
//            log.info("findByName Test");
//            // given
//            String facilityName = "탁구";
//
//            given(facilityRepository.findByFacilityName(anyString(), any(Pageable.class))).willReturn(Page.empty());
//
//            log.info("findByName Test given: ✔");
//            // when
//            Page<FacilityDTO> result = facilityService.findByName(facilityName, Pageable.unpaged());
//
//            log.info("findByName Test when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(Page.class)
//            );
//
//            log.info("findByName Test then: ✔");
//        }
//
//        @Test
//        @Order(5)
//        @DisplayName("findByDormitoryId Test")
//        void findByDormitoryIdTest(){
//            log.info("findByDormitoryId Test");
//            // given
//            String dormitoryId = "dor_1";
//
//            given(facilityRepository.findByDormitoryId(anyString(), any(Pageable.class))).willReturn(Page.empty());
//
//            log.info("findByDormitoryId Test given: ✔");
//            // when
//            Page<FacilityDTO> result = facilityService.findByDormitoryId(dormitoryId, Pageable.unpaged());
//
//            log.info("findByDormitoryId Test when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isNotNull(),
//                    () -> assertThat(result).isInstanceOf(Page.class)
//            );
//
//            log.info("findByDormitoryId Test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Update Test")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class UpdateTest{
//        @Test
//        @Order(1)
//        @DisplayName("updateFacility Test")
//        void updateFacilityTest(){
//            log.info("updateFacility Test");
//            // given
//            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
//                    .id("dor_1")
//                    .name("기숙사1")
//                    .build();
//
//            FacilityDTO updateFacility = FacilityDTO.builder()
//                    .id("dor_1_pingpong")
//                    .name("update_fac")
//                    .info("update_test_info")
//                    .open(6)
//                    .close(18)
//                    .img("update_test_img")
//                    .terms("update_test_terms")
//                    .dormitoryDTO(dormitoryDTO)
//                    .build();
//
//            given(facilityRepository.existsById(anyString())).willReturn(true);
//
//            log.info("updateFacility Test given: ✔");
//            // when
//            facilityService.updateFacility(updateFacility);
//
//            log.info("updateFacility Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(facilityRepository).should().save(any(FacilityEntity.class)),
//                    () -> assertThatCode(() -> facilityService.updateFacility(updateFacility)).doesNotThrowAnyException()
//            );
//
//            log.info("updateFacility Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("updateFacility Test (존재하지 않는 아이디)")
//        void updateFacilityTestWithNotExistId(){
//            log.info("updateFacility Test (존재하지 않는 아이디)");
//            // given
//            FacilityDTO updateFacility = FacilityDTO.builder()
//                    .id("never_used_id")
//                    .build();
//
//            given(facilityRepository.existsById(anyString())).willReturn(false);
//
//            log.info("updateFacility Test (존재하지 않는 아이디) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> facilityService.updateFacility(updateFacility));
//
//            log.info("updateFacility Test (존재하지 않는 아이디) | when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isInstanceOf(ObjectNotFoundException.class),
//                    () -> then(facilityRepository).shouldHaveNoMoreInteractions()
//            );
//
//            log.info("updateFacility Test (존재하지 않는 아이디) | then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("updateFacilityId Test")
//        void updateFacilityIdTest() {
//            log.info("updateFacilityId Test");
//            // given
//            String id = "dor_1_badminton";
//
//            String newId = "newId";
//
//            given(facilityRepository.existsById(anyString())).willReturn(true);
//
//            log.info("updateFacilityId Test given: ✔");
//            // when
//            facilityService.updateFacilityId(id, newId);
//
//            log.info("updateFacilityId Test when: ✔");
//            //then
//            assertAll(
//                    () -> then(facilityRepository).should().updateFacilityId(anyString(), anyString()),
//                    () -> assertThatCode(() -> facilityService.updateFacilityId(anyString(), newId)).doesNotThrowAnyException()
//            );
//
//            log.info("updateFacilityId Test then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("updateFacilityId Test (존재하지 않는 아이디)")
//        void updateFacilityIdTestWithNotExistId(){
//            log.info("updateFacilityId Test (존재하지 않는 아이디)");
//            // given
//            String id = "never_used_id";
//
//            String newId = "newId";
//
//            given(facilityRepository.existsById(anyString())).willReturn(false);
//
//            log.info("updateFacilityId Test (존재하지 않는 아이디) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> facilityService.updateFacilityId(id, newId));
//
//            log.info("updateFacilityId Test (존재하지 않는 아이디) | when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isInstanceOf(ObjectNotFoundException.class),
//                    () -> then(facilityRepository).shouldHaveNoMoreInteractions()
//            );
//
//            log.info("updateFacilityId Test (존재하지 않는 아이디) | then: ✔");
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
//            String id = "dor_1_badminton";
//
//            given(facilityRepository.existsById(anyString())).willReturn(true);
//
//            log.info("deleteById Test given: ✔");
//            // when
//            facilityService.deleteById(id);
//
//            log.info("deleteById Test when: ✔");
//            // then
//            assertAll(
//                    () -> then(facilityRepository).should().deleteById(anyString()),
//                    () -> assertThatCode(() -> facilityService.deleteById(anyString())).doesNotThrowAnyException()
//            );
//
//            log.info("deleteById Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("deleteById Test (존재하지 않는 아이디)")
//        void deleteByIdTestWithNotExistId(){
//            log.info("deleteById Test (존재하지 않는 아이디)");
//            // given
//            String id = "never_used_id";
//
//            given(facilityRepository.existsById(anyString())).willReturn(false);
//
//            log.info("deleteById Test (존재하지 않는 아이디) | given: ✔");
//            // when
//            Throwable result = catchThrowable(() -> facilityService.deleteById(id));
//
//            log.info("deleteById Test (존재하지 않는 아이디) | when: ✔");
//            // then
//            assertAll(
//                    () -> assertThat(result).isInstanceOf(ObjectNotFoundException.class),
//                    () -> then(facilityRepository).shouldHaveNoMoreInteractions()
//            );
//
//            log.info("deleteById Test (존재하지 않는 아이디) | then: ✔");
//        }
//    }
//}
