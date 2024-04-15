package com.project.clickit.dormitory;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.repository.DormitoryRepository;
import com.project.clickit.service.DormitoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@DisplayName("DormitoryService 테스트")
@ExtendWith({MockitoExtension.class})
public class DormitoryServiceTest {
    @Mock
    private DormitoryRepository dormitoryRepository;

    @InjectMocks
    private DormitoryService dormitoryService;

    private DormitoryDTO dormitoryDTO;

    private DormitoryEntity dormitoryEntity;

    @BeforeEach
    void setUp() {
        dormitoryDTO = DormitoryDTO.builder()
                .id("test_dormitory_id")
                .name("test_dormitory_name")
                .build();

        dormitoryEntity = DormitoryEntity.builder()
                .id("test_dormitory_id")
                .name("test_dormitory_name")
                .build();
    }

    @Test
    @DisplayName("기숙사 아이디 중복 체크 테스트")
    void duplicateCheckTest() {
        log.info("기숙사 아이디 중복 체크 테스트: false(중복 x)");
        // given
        given(dormitoryRepository.existsById(dormitoryDTO.getId())).willReturn(false);

        // when
        Boolean result = dormitoryService.isExist(dormitoryDTO.getId());
        log.info("result: {}", result);
        
        // then
        assertThat(result).isFalse();
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("기숙사 아이디 중복 체크 테스트")
    void duplicateCheckTest2() {
        log.info("기숙사 아이디 중복 체크 테스트: true(중복 o)");
        // given
        given(dormitoryRepository.existsById("dor_1")).willReturn(true);

        // when
        Boolean result = dormitoryService.isExist("dor_1");
        log.info("result: {}", result);

        // then
        assertThat(result).isTrue();
        log.info("테스트 종료");
    }

    @Nested
    @DisplayName("기숙사 생성 기능 테스트")
    class DormitoryCreateTest{
        @Test
        @DisplayName("기숙사 생성 테스트")
        void createDormitoryTest() {
            log.info("기숙사 생성 테스트");
            // when
            dormitoryService.createDormitory(dormitoryDTO);

            // then
            verify(dormitoryRepository, atLeastOnce()).save(any(DormitoryEntity.class));
        }

        @Test
        @DisplayName("기숙사 생성 테스트(중복된 아이디)")
        void createDormitoryTest2(){
            log.info("기숙사 생성 테스트(중복된 아이디)");

            // given
            DormitoryDTO duplicatedDormitoryDTO = DormitoryDTO.builder()
                    .id("dor_1")
                    .name("test_dormitory_name")
                    .build();

            // when
            given(dormitoryRepository.existsById(duplicatedDormitoryDTO.getId()))
                    .willReturn(true);

            log.info("DuplicatedIdException 발생 예상");
            // then
            assertThatRuntimeException()
                    .isThrownBy(() -> dormitoryService.createDormitory(duplicatedDormitoryDTO))
                    .withMessage("이미 존재하는 아이디입니다.");
            log.info("DuplicatedIdException 발생 확인");
            log.info("테스트 종료");
        }
    }

    @Nested
    @DisplayName("기숙사 조회 기능 테스트")
    class DormitoryReadTest{
        @Test
        @DisplayName("기숙사 전체 조회 테스트")
        void getAllTest(){
            log.info("기숙사 전체 조회 테스트");
            // given

            // when
            when(dormitoryRepository.findAll()).thenReturn(List.of(dormitoryEntity));

            // then
            assertThat(dormitoryService.getAll(Pageable.unpaged())).isNotNull();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("기숙사 아이디로 조회 테스트")
        void getByIdTest(){
            log.info("기숙사 아이디로 조회 테스트");
            // given

            // when
            when(dormitoryRepository.findByDormitoryId(dormitoryDTO.getId())).thenReturn(dormitoryEntity);

            // then
            assertThat(dormitoryService.findById(dormitoryDTO.getId())).isNotNull();
            log.info("테스트 종료");
        }
    }

    @Nested
    @DisplayName("기숙사 수정 기능 테스트")
    class DormitoryUpdateTest{
        @Test
        @DisplayName("기숙사 이름 수정 테스트")
        void updateDormitoryNameTest(){
            log.info("기숙사 이름 수정 테스트");
            // given
            String dormitoryId = "dor_1";
            String updateName = "update_dormitory_name";

            // when
            doNothing().when(dormitoryRepository).updateDormitoryName(dormitoryId, updateName);

            // then
            assertThatCode(() -> dormitoryService.updateDormitoryName(dormitoryId, updateName))
                    .doesNotThrowAnyException();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("기숙사 이름 수정 테스트(존재하지 않는 기숙사)")
        void updateDormitoryNameTest2(){
            log.info("기숙사 이름 수정 테스트(존재하지 않는 기숙사)");
            // given
            String dormitoryId = "dor_3";
            String updateName = "update_dormitory_name";

            // when
            given(dormitoryRepository.existsById(dormitoryId)).willReturn(false);

            log.info("DormitoryNotFoundException 발생 예상");
            // then
            assertThatRuntimeException()
                    .isThrownBy(() -> dormitoryService.updateDormitoryName(dormitoryId, updateName))
                    .withMessage(ErrorCode.DORMITORY_NOT_FOUND.getMessage());
            log.info("DormitoryNotFoundException 발생 확인");
            log.info("테스트 종료");
        }
    }

    @Nested
    @DisplayName("기숙사 삭제 기능 테스트")
    class DormitoryDeleteTest{
        @Test
        @DisplayName("기숙사 삭제 테스트")
        void deleteByIdTest(){
            log.info("기숙사 삭제 테스트");
            // given
            String dormitoryId = "dor_1";

            // when
            when(dormitoryRepository.existsById(dormitoryId)).thenReturn(true);

            // then
            assertThatCode(() -> dormitoryService.deleteById(dormitoryId))
                    .doesNotThrowAnyException();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("기숙사 삭제 테스트(존재하지 않는 기숙사)")
        void deleteByIdTest2(){
            log.info("기숙사 삭제 테스트(존재하지 않는 기숙사)");
            // given
            String dormitoryId = "dor_3";

            // when
            when(dormitoryRepository.existsById(dormitoryId)).thenReturn(false);

            log.info("DormitoryNotFoundException 발생 예상");
            // then
            assertThatRuntimeException()
                    .isThrownBy(() -> dormitoryService.deleteById(dormitoryId))
                    .withMessage(ErrorCode.DORMITORY_NOT_FOUND.getMessage());
            log.info("DormitoryNotFoundException 발생 확인");
            log.info("테스트 종료");
        }
    }
}
