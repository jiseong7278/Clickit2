package com.project.clickit.facility;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.facility.FacilityNotFoundException;
import com.project.clickit.repository.FacilityRepository;
import com.project.clickit.service.FacilityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@Slf4j
@DisplayName("FacilityService 테스트")
@ExtendWith({MockitoExtension.class})
public class FacilityServiceTest {
    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private FacilityService facilityService;

    @Nested
    @DisplayName("isExist Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class isExist{
        @Test
        @Order(1)
        @DisplayName("isExist Test")
        void isExistTest(){
            log.info("isExist Test");
            // given
            String facilityId = "dor_1_badminton";

            given(facilityRepository.existsById(facilityId)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ facilityId = {}
                    \t  ┗ given(facilityRepository.existsById(facilityId)).willReturn(true)
                    """, facilityId);
            // when
            Boolean result = facilityService.isExist(facilityId);

            log.info("""
                    
                    \twhen
                    \t  ┗ Boolean result = facilityService.isExist(facilityId)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Boolean.class),
                    () -> assertThat(result).isTrue(),
                    () -> then(facilityRepository).should().existsById(facilityId)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(Boolean.class)
                    \t  ┣ assertThat(result).isTrue()
                    \t  ┗ facilityRepository.should().existsById(facilityId)
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("시설 아이디 중복 체크 테스트")
        void isExistTestDuplicated(){
            log.info("시설 아이디 중복 체크 테스트: true(중복 o)");
            // given
            String facilityId = "never_used_id";

            given(facilityRepository.existsById(facilityId)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ facilityId = {}
                    \t  ┗ given(facilityRepository.existsById(facilityId)).willReturn(false)
                    """, facilityId);
            // when
            Boolean result = facilityService.isExist(facilityId);

            log.info("""
                    
                    \twhen
                    \t  ┗ Boolean result = facilityService.isExist(facilityId)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Boolean.class),
                    () -> assertThat(result).isFalse(),
                    () -> then(facilityRepository).should().existsById(facilityId)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(Boolean.class)
                    \t  ┣ assertThat(result).isFalse()
                    \t  ┗ facilityRepository.should().existsById(facilityId)
                    """);
        }
    }

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("createFacility Test")
        void createFacilityTest(){
            log.info("createFacility Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("dor_1")
                    .name("기숙사1")
                    .build();

            FacilityDTO facilityDTO = FacilityDTO.builder()
                    .id("dor_1_tennis")
                    .name("테니스장")
                    .info("테니스장입니다.")
                    .open(6)
                    .close(18)
                    .img("테니스장 이미지")
                    .terms("테니스장 이용 조건")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(facilityRepository.existsById(facilityDTO.getId())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ facilityDTO
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┣ name = {}
                    \t  ┃  ┣ info = {}
                    \t  ┃  ┣ open = {}
                    \t  ┃  ┣ close = {}
                    \t  ┃  ┣ img = {}
                    \t  ┃  ┣ terms = {}
                    \t  ┃  ┣ dormitoryDTO
                    \t  ┃  ┃  ┣ id = {}
                    \t  ┃  ┃  ┗ name = {}
                    \t  ┗ given(facilityRepository.existsById(facilityDTO.getId())).willReturn(false)
                    """, facilityDTO.getId(), facilityDTO.getName(), facilityDTO.getInfo(),
                    facilityDTO.getOpen(), facilityDTO.getClose(), facilityDTO.getImg(),
                    facilityDTO.getTerms(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            facilityService.createFacility(facilityDTO);

            log.info("""
                    
                    \twhen
                    \t  ┗ facilityService.createFacility(facilityDTO)
                    """);
            // then
            assertAll(
                    () -> then(facilityRepository).should().save(any(FacilityEntity.class)),
                    () -> assertThatCode(() -> facilityService.createFacility(facilityDTO)).doesNotThrowAnyException()
            );
        }

        @Test
        @Order(2)
        @DisplayName("createFacility Test (중복된 아이디)")
        void createFacilityTestWithDuplicatedId(){
            log.info("createFacility Test (중복된 아이디)");
            // given
            FacilityDTO facilityDTO = FacilityDTO.builder()
                    .id("dor_1_badminton")
                    .build();

            given(facilityRepository.existsById(facilityDTO.getId())).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ facilityDTO
                    \t  ┃  ┗ id = {}
                    \t  ┗ given(facilityRepository.existsById(facilityDTO.getId())).willReturn(true)
                    """, facilityDTO.getId());
            // when
            Throwable result = catchThrowable(()->facilityService.createFacility(facilityDTO));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(()->facilityService.createFacility(facilityDTO))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class),
                    () -> assertThat(result).hasMessageContaining("이미 존재하는 아이디입니다."),
                    () -> then(facilityRepository).should(never()).save(any(FacilityEntity.class))
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(DuplicatedIdException.class)
                    \t  ┣ assertThat(result).hasMessageContaining("이미 존재하는 아이디입니다.")
                    \t  ┗ facilityRepository.should(never()).save(any(FacilityEntity.class)
                    """);
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadTest {
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest(){
            log.info("getAll Test");
            // given
            when(facilityRepository.findAll(Pageable.unpaged())).thenReturn(Page.empty());

            log.info("""
                    
                    \tgiven
                    \t  ┗ when(facilityRepository.findAll(Pageable.unpaged())).thenReturn(Page.empty())
                    """);
            // when
            Page<FacilityDTO> result = facilityService.getAll(Pageable.unpaged());

            log.info("""
                    
                    \twhen
                    \t  ┗ Page<FacilityDTO> result = facilityService.getAll(Pageable.unpaged())
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(Page.class)
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest() {
            log.info("findById Test");
            // given
            String facilityId = "dor_1_badminton";

            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .name("기숙사1")
                    .build();

            given(facilityRepository.existsById(facilityId)).willReturn(true);

            when(facilityRepository.findByFacilityId(facilityId)).thenReturn(FacilityEntity.builder().dormitoryEntity(dormitoryEntity).build());

            log.info("""
                    
                    \tgiven
                    \t  ┣ facilityId = {}
                    \t  ┣ DormitoryEntity
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┗ name = {}
                    \t  ┣ given(facilityRepository.existsById(facilityId)).willReturn(true)
                    \t  ┗ when(facilityRepository.findByFacilityId(facilityId)).thenReturn(FacilityEntity.builder().build())
                    """, facilityId, dormitoryEntity.getId(), dormitoryEntity.getName());
            // when
            FacilityDTO result = facilityService.findById(facilityId);

            log.info("""
                    
                    \twhen
                    \t  ┗ FacilityDTO result = facilityService.findById(facilityId)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(FacilityDTO.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(FacilityDTO.class)
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("findById Test (존재하지 않는 아이디)")
        void findByIdTestWithNotExistId() {
            log.info("findById Test (존재하지 않는 아이디)");
            // given
            String facilityId = "never_used_id";

            when(facilityRepository.existsById(facilityId)).thenReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ facilityId = {}
                    \t  ┗ when(facilityRepository.existsById(facilityId)).thenReturn(false)
                    """, facilityId);
            // when
            Throwable result = catchThrowable(() -> facilityService.findById(facilityId));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> facilityService.findById(facilityId))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(FacilityNotFoundException.class),
                    () -> then(facilityRepository).should(never()).findByFacilityId(facilityId)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(FacilityNotFoundException.class)
                    \t  ┣ assertThat(result).hasMessageContaining("시설을 찾을 수 없습니다.")
                    \t  ┗ facilityRepository.should(never()).findByFacilityId(facilityId)
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("findByName Test")
        void findByNameTest(){
            log.info("findByName Test");
            // given
            String facilityName = "탁구";

            when(facilityRepository.findByFacilityName(facilityName, Pageable.unpaged())).thenReturn(Page.empty());

            log.info("""
                    
                    \tgiven
                    \t  ┗ when(facilityRepository.findByFacilityName(facilityName, Pageable.unpaged())).thenReturn(Page.empty())
                    """);
            // when
            Page<FacilityDTO> result = facilityService.findByName(facilityName, Pageable.unpaged());

            log.info("""
                    
                    \twhen
                    \t  ┗ Page<FacilityDTO> result = facilityService.findByName(facilityName, Pageable.unpaged())
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(Page.class)
                    """);
        }

        @Test
        @Order(5)
        @DisplayName("findByDormitoryId Test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId Test");
            // given
            String dormitoryId = "dor_1";

            when(facilityRepository.findByDormitoryId(dormitoryId, Pageable.unpaged())).thenReturn(Page.empty());

            log.info("""
                    
                    \tgiven
                    \t  ┗ when(facilityRepository.findByDormitoryId(dormitoryId, Pageable.unpaged())).thenReturn(Page.empty())
                    """);
            // when
            Page<FacilityDTO> result = facilityService.findByDormitoryId(dormitoryId, Pageable.unpaged());

            log.info("""
                    
                    \twhen
                    \t  ┗ Page<FacilityDTO> result = facilityService.findByDormitoryId(dormitoryId, Pageable.unpaged())
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(Page.class)
                    """);
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("updateFacility Test")
        void updateFacilityTest(){
            log.info("updateFacility Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("dor_1")
                    .name("기숙사1")
                    .build();

            FacilityDTO updateFacility = FacilityDTO.builder()
                    .id("dor_1_pingpong")
                    .name("update_fac")
                    .info("update_test_info")
                    .open(6)
                    .close(18)
                    .img("update_test_img")
                    .terms("update_test_terms")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(facilityRepository.existsById(updateFacility.getId())).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ updateFacility
                    \t  ┃  ┣ id = {}
                    \t  ┃  ┣ name = {}
                    \t  ┃  ┣ info = {}
                    \t  ┃  ┣ open = {}
                    \t  ┃  ┣ close = {}
                    \t  ┃  ┣ img = {}
                    \t  ┃  ┣ terms = {}
                    \t  ┃  ┣ dormitoryDTO
                    \t  ┃  ┃  ┣ id = {}
                    \t  ┃  ┃  ┗ name = {}
                    \t  ┗ given(facilityRepository.existsById(updateFacility.getId())).willReturn(true)
                    """, updateFacility.getId(), updateFacility.getName(), updateFacility.getInfo(),
                    updateFacility.getOpen(), updateFacility.getClose(), updateFacility.getImg(),
                    updateFacility.getTerms(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            facilityService.updateFacility(updateFacility);

            log.info("""
                    
                    \twhen
                    \t  ┗ facilityService.updateFacility(updateFacility)
                    """);
            // then
            assertAll(
                    () -> then(facilityRepository).should().save(any(FacilityEntity.class)),
                    () -> verify(facilityRepository, times(1)).save(any(FacilityEntity.class)),
                    () -> assertThatCode(() -> facilityService.updateFacility(updateFacility)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ facilityRepository.should().save(any(FacilityEntity.class))
                    \t  ┣ verify(facilityRepository, times(1)).save(any(FacilityEntity.class))
                    \t  ┗ assertThatCode(() -> facilityService.updateFacility(updateFacility)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("updateFacility Test (존재하지 않는 아이디)")
        void updateFacilityTestWithNotExistId(){
            log.info("updateFacility Test (존재하지 않는 아이디)");
            // given
            FacilityDTO updateFacility = FacilityDTO.builder()
                    .id("never_used_id")
                    .build();

            given(facilityRepository.existsById(updateFacility.getId())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ updateFacility
                    \t  ┃  ┗ id = {}
                    \t  ┗ given(facilityRepository.existsById(updateFacility.getId())).willReturn(false)
                    """, updateFacility.getId());
            // when
            Throwable result = catchThrowable(() -> facilityService.updateFacility(updateFacility));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> facilityService.updateFacility(updateFacility))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(FacilityNotFoundException.class),
                    () -> then(facilityRepository).should(never()).save(any(FacilityEntity.class))
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(FacilityNotFoundException.class)
                    \t  ┣ assertThat(result).hasMessageContaining("시설을 찾을 수 없습니다.")
                    \t  ┗ facilityRepository.should(never()).save(any(FacilityEntity.class)
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("updateFacilityId Test")
        void updateFacilityIdTest() {
            log.info("updateFacilityId Test");
            // given
            String id = "dor_1_badminton";

            String newId = "newId";

            given(facilityRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┣ newId = {}
                    \t  ┗ given(facilityRepository.existsById(id)).willReturn(true)
                    """, id, newId);
            // when
            facilityService.updateFacilityId(id, newId);

            log.info("""
                    
                    \twhen
                    \t  ┗ facilityService.updateFacilityId(id, newId)
                    """);
            //then
            assertAll(
                    () -> then(facilityRepository).should().updateFacilityId(id, newId),
                    () -> verify(facilityRepository, times(1)).updateFacilityId(id, newId),
                    () -> assertThatCode(() -> facilityService.updateFacilityId(id, newId)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ facilityRepository.should().updateFacilityId(id, newId)
                    \t  ┣ verify(facilityRepository, times(1)).updateFacilityId(id, newId)
                    \t  ┗ assertThatCode(() -> facilityService.updateFacilityId(id, newId)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("updateFacilityId Test (존재하지 않는 아이디)")
        void updateFacilityIdTestWithNotExistId(){
            log.info("updateFacilityId Test (존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            String newId = "newId";

            given(facilityRepository.existsById(id)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┣ newId = {}
                    \t  ┗ given(facilityRepository.existsById(id)).willReturn(false)
                    """, id, newId);
            // when
            Throwable result = catchThrowable(() -> facilityService.updateFacilityId(id, newId));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> facilityService.updateFacilityId(id, newId))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(FacilityNotFoundException.class),
                    () -> then(facilityRepository).should(never()).updateFacilityId(id, newId)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(FacilityNotFoundException.class)
                    \t  ┣ assertThat(result).hasMessageContaining("시설을 찾을 수 없습니다.")
                    \t  ┗ facilityRepository.should(never()).updateFacilityId(id, newId)
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
            String id = "dor_1_badminton";

            given(facilityRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┗ given(facilityRepository.existsById(id)).willReturn(true)
                    """, id);
            // when
            facilityService.deleteById(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ facilityService.deleteById(id)
                    """);
            // then
            assertAll(
                    () -> then(facilityRepository).should().deleteById(id),
                    () -> verify(facilityRepository, times(1)).deleteById(id),
                    () -> assertThatCode(() -> facilityService.deleteById(id)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ facilityRepository.should().deleteById(id)
                    \t  ┣ verify(facilityRepository, times(1)).deleteById(id)
                    \t  ┗ assertThatCode(() -> facilityService.deleteById(id)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("deleteById Test (존재하지 않는 아이디)")
        void deleteByIdTestWithNotExistId(){
            log.info("deleteById Test (존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            given(facilityRepository.existsById(id)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id = {}
                    \t  ┗ given(facilityRepository.existsById(id)).willReturn(false)
                    """, id);
            // when
            Throwable result = catchThrowable(() -> facilityService.deleteById(id));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> facilityService.deleteById(id))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(FacilityNotFoundException.class),
                    () -> then(facilityRepository).should(never()).deleteById(id)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(FacilityNotFoundException.class)
                    \t  ┣ assertThat(result).hasMessageContaining("시설을 찾을 수 없습니다.")
                    \t  ┗ facilityRepository.should(never()).deleteById(id)
                    """);
        }
    }
}
