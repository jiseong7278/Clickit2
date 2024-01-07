package com.project.clickit.dormitory;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.repository.DormitoryRepository;
import com.project.clickit.service.DormitoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
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
        Boolean result = dormitoryService.duplicateCheck(dormitoryDTO.getId());
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
        Boolean result = dormitoryService.duplicateCheck("dor_1");
        log.info("result: {}", result);

        // then
        assertThat(result).isTrue();
        log.info("테스트 종료");
    }

    @Test
    @DisplayName("기숙사 생성 테스트")
    void createDormitoryTest() {
        log.info("기숙사 생성 테스트");
        // when
        dormitoryService.createDormitory(dormitoryDTO);

        // then
        verify(dormitoryRepository, atLeastOnce()).save(any(DormitoryEntity.class));
    }
}
