package com.project.clickit.dormitory;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.repository.DormitoryRepository;
import com.project.clickit.service.DormitoryService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Slf4j
@DisplayName("DormitoryService 테스트")
@ExtendWith({MockitoExtension.class})
public class DormitoryServiceTest {
    @Mock
    private DormitoryRepository dormitoryRepository;

    @InjectMocks
    private DormitoryService dormitoryService;

    @Nested
    @DisplayName("isExist Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IsExist{
        @Test
        @Order(1)
        @DisplayName("isExist Test false(중복 x)")
        void isExistTest() {
            log.info("isExist test false(중복 x)");
            // given
            String dormitoryId = "dor_100";
            given(dormitoryRepository.existsById(anyString())).willReturn(false);

            log.info("isExist test false | given: ✔");
            // when
            Boolean result = dormitoryService.isExist(dormitoryId);

            log.info("isExist test false | when: ✔");
            // then
            assertThat(result).isFalse();

            log.info("isExist test false | then: ✔");
        }

        @Test
        @DisplayName("isExist Test true(중복된 아이디)")
        void isExistTestDuplicated() {
            log.info("isExist Test true(중복된 아이디)");
            // given
            String dormitoryId = "dor_1";
            given(dormitoryRepository.existsById(anyString())).willReturn(true);

            log.info("isExist Test true(중복된 아이디) | given: ✔");
            // when
            Boolean result = dormitoryService.isExist(dormitoryId);

            log.info("isExist Test true(중복된 아이디) | when: ✔");
            // then
            assertThat(result).isTrue();

            log.info("isExist Test true(중복된 아이디) | then: ✔");
        }
    }

    @Nested
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Create{
        @Test
        @Order(1)
        @DisplayName("Create Test")
        void createDormitoryTest() {
            log.info("Create Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("dor_100")
                    .name("test_dormitory_name")
                    .build();

            given(dormitoryRepository.existsById(anyString())).willReturn(false);

            log.info("Create Test | given: ✔");
            // when
            dormitoryService.createDormitory(dormitoryDTO);

            log.info("Create Test | when: ✔");
            // then
            assertAll(
                    () -> then(dormitoryRepository).should().save(any(DormitoryEntity.class)),
                    () -> assertThatCode(() -> dormitoryService.createDormitory(dormitoryDTO)).doesNotThrowAnyException()
            );

            log.info("Create Test | then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("Create Test(중복된 아이디)")
        void createDormitoryTestDuplicated(){
            log.info("Create Test(중복된 아이디)");

            // given
            DormitoryDTO duplicatedDormitoryDTO = DormitoryDTO.builder()
                    .id("dor_1")
                    .name("test_dormitory_name")
                    .build();

            given(dormitoryRepository.existsById(anyString())).willReturn(true);

            log.info("Create Test(중복된 아이디) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> dormitoryService.createDormitory(duplicatedDormitoryDTO));

            log.info("Create Test(중복된 아이디) | when: ✔");
            // then
            assertThat(result).isInstanceOf(DuplicatedIdException.class);

            log.info("Create Test(중복된 아이디) | then: ✔");
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest(){
            log.info("getAll Test");
            // given
            Page<DormitoryEntity> dormitoryEntityPage = Page.empty();

            given(dormitoryRepository.findAll(Pageable.unpaged())).willReturn(dormitoryEntityPage);

            log.info("getAll Test | given: ✔");
            // when
            Page<DormitoryDTO> result = dormitoryService.getAll(Pageable.unpaged());

            log.info("getAll Test | when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Page.class);

            log.info("getAll Test | then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest(){
            log.info("findById Test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .name("test_dormitory_name")
                    .build();

            given(dormitoryRepository.existsById(anyString())).willReturn(true);

            given(dormitoryRepository.findByDormitoryId(anyString())).willReturn(dormitoryEntity);

            log.info("findById Test | given: ✔");
            // when
            DormitoryDTO result = dormitoryService.findById(dormitoryEntity.getId());

            log.info("findById Test | when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(dormitoryEntity.getId());
            assertThat(result.getName()).isEqualTo(dormitoryEntity.getName());

            log.info("findById Test | then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByName Test")
        void findByNameTest(){
            log.info("findByName Test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .name("test_dormitory_name")
                    .build();

            Page<DormitoryEntity> dormitoryEntityPage = Page.empty();

            given(dormitoryRepository.findByDormitoryName(anyString(), any(Pageable.class))).willReturn(dormitoryEntityPage);

            log.info("findByName Test | given: ✔");
            // when
            Page<DormitoryDTO> result = dormitoryService.findByName(dormitoryEntity.getName(), Pageable.unpaged());

            log.info("findByName Test | when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Page.class);

            log.info("findByName Test | then: ✔");
        }
    }

    @Nested
    @DisplayName("Update Test")
    class Update{
        @Test
        @DisplayName("updateDormitoryName Test")
        void updateDormitoryNameTest(){
            log.info("updateDormitoryName Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("dor_1")
                    .name("update")
                    .build();

            given(dormitoryRepository.existsById(dormitoryDTO.getId())).willReturn(true);

            log.info("updateDormitoryName Test | given: ✔");
            // when
            dormitoryService.updateDormitory(dormitoryDTO);

            log.info("updateDormitoryName Test | when: ✔");
            // then
            assertAll(
                    () -> then(dormitoryRepository).should().save(any(DormitoryEntity.class)),
                    () -> assertThatCode(() -> dormitoryService.updateDormitory(dormitoryDTO)).doesNotThrowAnyException()
            );

        }

        @Test
        @DisplayName("updateDormitoryName Test(존재하지 않는 기숙사)")
        void updateDormitoryNameTestNotFound(){
            log.info("updateDormitoryName Test(존재하지 않는 기숙사)");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("dor_3")
                    .name("update")
                    .build();

            given(dormitoryRepository.existsById(dormitoryDTO.getId())).willReturn(false);

            log.info("updateDormitoryName Test(존재하지 않는 기숙사) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> dormitoryService.updateDormitory(dormitoryDTO));

            log.info("updateDormitoryName Test(존재하지 않는 기숙사) | when: ✔");
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("updateDormitoryName Test(존재하지 않는 기숙사) | then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete Test")
    class Delete{
        @Test
        @DisplayName("deleteById Test")
        void deleteByIdTest(){
            log.info("deleteById Test");
            // given
            String dormitoryId = "dor_1";

            given(dormitoryRepository.existsById(dormitoryId)).willReturn(true);

            log.info("deleteById Test | given: ✔");
            // when
            dormitoryService.deleteById(dormitoryId);

            log.info("deleteById Test | when: ✔");
            // then
            assertAll(
                    () -> then(dormitoryRepository).should().deleteById(dormitoryId),
                    () -> assertThatCode(() -> dormitoryService.deleteById(dormitoryId)).doesNotThrowAnyException()
            );

        }

        @Test
        @DisplayName("deleteById Test (존재하지 않는 기숙사)")
        void deleteByIdTestNotFound(){
            log.info("deleteById Test (존재하지 않는 기숙사)");
            // given
            String dormitoryId = "dor_3";

            given(dormitoryRepository.existsById(dormitoryId)).willReturn(false);

            log.info("deleteById Test (존재하지 않는 기숙사) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> dormitoryService.deleteById(dormitoryId));

            log.info("deleteById Test (존재하지 않는 기숙사) | when: ✔");
            // then
            assertThat(result).isInstanceOf(ObjectNotFoundException.class);

            log.info("deleteById Test (존재하지 않는 기숙사) | then: ✔");
        }
    }
}
