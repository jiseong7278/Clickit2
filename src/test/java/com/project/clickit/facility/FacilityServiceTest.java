package com.project.clickit.facility;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.FacilityDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.FacilityEntity;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
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

    private FacilityDTO facilityDTO;

    private FacilityDTO duplicateFacilityDTO;

    private DormitoryDTO dormitoryDTO;

    private DormitoryDTO duplicateDormitoryDTO;

    private FacilityEntity facilityEntity;

    private DormitoryEntity dormitoryEntity;

    @BeforeEach
    void setUp(){
        dormitoryDTO = DormitoryDTO.builder()
                .id("test_dormitory_id")
                .name("test_dormitory_name")
                .build();

        duplicateDormitoryDTO = DormitoryDTO.builder()
                .id("dor_1")
                .name("test_dormitory_name")
                .build();

        facilityDTO = FacilityDTO.builder()
                .id("test_facility_id")
                .name("test_facility_name")
                .info("test_facility_info")
                .open(9)
                .close(21)
                .img("test_facility_img")
                .terms("test_facility_terms")
                .dormitoryDTO(dormitoryDTO)
                .build();

        duplicateFacilityDTO = FacilityDTO.builder()
                .id("dor_1_pingpong")
                .name("test_facility_name")
                .info("test_facility_info")
                .open(9)
                .close(21)
                .img("test_facility_img")
                .terms("test_facility_terms")
                .dormitoryDTO(duplicateDormitoryDTO)
                .build();

        dormitoryEntity = DormitoryEntity.builder().id("dor_1").name("테스트 기숙사 1").build();

        facilityEntity = FacilityEntity.builder()
                .id("dor_1_pingpong")
                .name("test_name")
                .info("test_info")
                .open(9)
                .close(21)
                .img("test_facility_img")
                .terms("test_facility_terms")
                .dormitoryEntity(dormitoryEntity)
                .build();
    }

    @DisplayName("시설 아이디 중복 체크 테스트")
    @Test
    void isExistTest(){
        log.info("시설 아이디 중복 체크 테스트: false(중복 x)");
        // given
        given(facilityRepository.existsById(facilityDTO.getId())).willReturn(false);

        // when
        Boolean result = facilityService.isExist(facilityDTO.getId());

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("시설 아이디 중복 체크 테스트")
    @Test
    void isExistTest2(){
        log.info("시설 아이디 중복 체크 테스트: true(중복 o)");
        // given
        given(facilityRepository.existsById(duplicateFacilityDTO.getId())).willReturn(true);

        // when
        Boolean result = facilityService.isExist(duplicateFacilityDTO.getId());

        // then
        assertThat(result).isTrue();
    }

    @Nested
    @DisplayName("시설 생성 기능 테스트")
    class FacilityCreateTest{
        @Test
        @DisplayName("시설 생성 테스트")
        void createFacilityTest(){
            log.info("시설 생성 테스트");
            // given
            given(facilityRepository.existsById(facilityDTO.getId())).willReturn(false);

            // when
            facilityService.createFacility(facilityDTO);

            // then
            verify(facilityRepository, atLeastOnce()).save(any(FacilityEntity.class));
        }

        @Test
        @DisplayName("시설 생성 테스트(중복된 아이디)")
        void createFacilityTest2(){
            log.info("시설 생성 테스트(중복된 아이디)");
            // given
            given(facilityRepository.existsById(duplicateFacilityDTO.getId())).willReturn(true);

            // when
            assertThatThrownBy(() -> facilityService.createFacility(duplicateFacilityDTO))
                    .isInstanceOf(RuntimeException.class);

            // then
            verify(facilityRepository, never()).save(any(FacilityEntity.class));
        }
    }

    @Nested
    @DisplayName("시설 조회 기능 테스트")
    class FacilityReadTest{
        @Test
        @DisplayName("시설 전체 조회 테스트")
        void getAllTest(){
            log.info("시설 전체 조회 테스트");
            // given
            given(facilityRepository.findAll()).willReturn(new ArrayList<>());

            // when
            Page<FacilityDTO> result = facilityService.getAll(Pageable.ofSize(10));

            // then
            assertThat(result).isNotNull();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("시설 id로 조회 테스트")
        void findByIdTest() {
            log.info("시설 id로 조회 테스트");
            // given

            given(facilityRepository.findByFacilityId(duplicateFacilityDTO.getId())).willReturn(facilityEntity);

            // when
            FacilityDTO result = facilityService.findById(duplicateFacilityDTO.getId());

            // then
            assertThat(result).isNotNull();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("시설 id로 조회 테스트 - 실패")
        void findByIdTestFail() {
            log.info("시설 id로 조회 테스트 - 실패");
            // given
            given(facilityRepository.findByFacilityId(facilityDTO.getId())).willReturn(null);

            // when
            Throwable result = catchThrowable(() -> facilityService.findById(facilityDTO.getId()));

            // then
            assertThat(result).isInstanceOf(RuntimeException.class);
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("시설 이름으로 조회 테스트")
        void findByNameTest(){
            log.info("시설 이름으로 조회 테스트");
            // given
            given(facilityRepository.findByFacilityName(duplicateFacilityDTO.getName())).willReturn(facilityEntity);

            // when
            FacilityDTO result = facilityService.findByName(duplicateFacilityDTO.getName());

            // then
            assertThat(result).isNotNull();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("시설 이름으로 조회 테스트 - 실패")
        void findByNameTestFail(){
            log.info("시설 이름으로 조회 테스트 - 실패");
            // given
            given(facilityRepository.findByFacilityName(facilityDTO.getName())).willReturn(null);

            // when
            Throwable result = catchThrowable(() -> facilityService.findByName(facilityDTO.getName()));

            // then
            assertThat(result).isInstanceOf(RuntimeException.class);
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("기숙사 id로 시설 조회 테스트")
        void findByDormitoryIdTest(){
            log.info("기숙사 id로 시설 조회 테스트");
            // given
            given(facilityRepository.findByDormitoryId(duplicateDormitoryDTO.getId(), Pageable.ofSize(10))).willReturn(Page.empty());

            // when
            Page<FacilityDTO> result = facilityService.findByDormitoryId(duplicateDormitoryDTO.getId(), Pageable.ofSize(10));

            // then
            assertThat(result).isNotNull();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("기숙사 id로 시설 조회 테스트 - 실패")
        void findByDormitoryIdTestFail(){
            log.info("기숙사 id로 시설 조회 테스트 - 실패");
            // given
            given(facilityRepository.findByDormitoryId(dormitoryDTO.getId(), Pageable.ofSize(10))).willReturn(null);

            // when
            Throwable result = catchThrowable(() -> facilityService.findByDormitoryId(dormitoryDTO.getId(), Pageable.ofSize(10)));

            // then
            assertThat(result).isInstanceOf(RuntimeException.class);
            log.info("테스트 종료");
        }
    }

    @Nested
    @DisplayName("시설 수정 기능 테스트")
    class FacilityUpdateTest{
        @Test
        @DisplayName("시설 수정 테스트")
        void updateFacilityTest(){
            log.info("시설 수정 테스트");
            // given
            FacilityDTO updateFacility = FacilityDTO.builder()
                    .id("dor_1_pingpong")
                    .name("update_fac")
                    .info("update_test_info")
                    .open(6)
                    .close(18)
                    .img("update_test_img")
                    .terms("update_test_terms")
                    .build();

            given(facilityRepository.existsById(updateFacility.getId())).willReturn(true);

            // when
            facilityService.updateFacility(updateFacility);

            // then
            then(facilityRepository).should().save(updateFacility.toEntity());

            assertThatCode(() -> facilityService.updateFacility(updateFacility)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("시설 아이디 수정 테스트")
        void updateFacilityIdTest() {
            log.info("시설 아이디 수정 테스트");
            // given
            String newId = "newId";

            given(facilityRepository.existsById(duplicateFacilityDTO.getId())).willReturn(true);

            // when
            facilityService.updateFacilityId(duplicateFacilityDTO.getId(), newId);

            //then
            then(facilityRepository).should().updateFacilityId(duplicateFacilityDTO.getId(), newId);

            assertThatCode(() -> facilityService.updateFacilityId(duplicateFacilityDTO.getId(), newId)).doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("시설 삭제 테스트")
    void deleteByIdTest(){
        log.info("시설 삭제 테스트");
        // given
        given(facilityRepository.existsById(duplicateFacilityDTO.getId())).willReturn(true);

        // when
        facilityService.deleteById(duplicateFacilityDTO.getId());

        // then
        then(facilityRepository).should().deleteById(duplicateFacilityDTO.getId());

        assertThatCode(() -> facilityService.deleteById(duplicateFacilityDTO.getId())).doesNotThrowAnyException();
    }
}
