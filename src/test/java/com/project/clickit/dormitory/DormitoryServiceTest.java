package com.project.clickit.dormitory;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.dormitory.DormitoryNotFoundException;
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

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Nested
    @DisplayName("isExist Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IsExist{
        @Test
        @Order(1)
        @DisplayName("isExist Test")
        void isExistTest() {
            log.info("기숙사 아이디 중복 체크 테스트: false(중복 x)");
            // given
            String dormitoryId = "dor_100";
            given(dormitoryRepository.existsById(dormitoryId)).willReturn(false);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryId: " + dormitoryId +
                    "\n\t  ┗ given(dormitoryRepository.existsById(dormitoryId)).willReturn(false)\n");
            // when
            Boolean result = dormitoryService.isExist(dormitoryId);

            log.info("""

                \twhen
                \t  ┗ Boolean result = dormitoryService.isExist(dormitoryId)
                """);
            // then
            assertThat(result).isFalse();

            log.info("""

                \tthen
                \t  ┗ assertThat(result).isFalse()
                """);
        }

        @Test
        @DisplayName("isExist Test(중복된 아이디)")
        void isExistTestDuplicated() {
            log.info("isExist Test(중복된 아이디)");
            // given
            String dormitoryId = "dor_1";
            given(dormitoryRepository.existsById(dormitoryId)).willReturn(true);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryId: " + dormitoryId +
                    "\n\t  ┗ given(dormitoryRepository.existsById(dormitoryId)).willReturn(true)\n");
            // when
            Boolean result = dormitoryService.isExist(dormitoryId);

            log.info("""

                \twhen
                \t  ┗ Boolean result = dormitoryService.isExist(dormitoryId)
                """);
            // then
            assertThat(result).isTrue();

            log.info("""

                \tthen
                \t  ┗ assertThat(result).isTrue()
                """);
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

            given(dormitoryRepository.existsById(dormitoryDTO.getId())).willReturn(false);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryDTO" +
                    "\n\t  ┗ id: " + dormitoryDTO.getId() +
                    "\n\t  ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┗ given(dormitoryRepository.existsById(dormitoryEntity.getId())).willReturn(false)\n");
            // when
            dormitoryService.createDormitory(dormitoryDTO);

            log.info("""

                \twhen
                \t  ┗ dormitoryService.createDormitory(dormitoryEntity.toDTO())
                """);
            // then
            assertAll(
                    () -> verify(dormitoryRepository, times(1)).save(any(DormitoryEntity.class)),
                    () -> assertThatCode(() -> dormitoryService.createDormitory(dormitoryDTO)).doesNotThrowAnyException()
            );
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ duplicatedDormitoryDTO" +
                    "\n\t  ┗ id: " + duplicatedDormitoryDTO.getId() +
                    "\n\t  ┗ name: " + duplicatedDormitoryDTO.getName() + "\n");
            // when
            when(dormitoryRepository.existsById(duplicatedDormitoryDTO.getId())).thenReturn(true);

            Throwable result = catchThrowable(() -> dormitoryService.createDormitory(duplicatedDormitoryDTO));

            log.info("""

                \twhen
                \t  ┗ when(dormitoryRepository.existsById(duplicatedDormitoryDTO.getId())).thenReturn(true)
                \t  ┗ Throwable result = catchThrowable(() -> dormitoryService.createDormitory(duplicatedDormitoryDTO))
                """);
            // then
            assertThat(result).isInstanceOf(DuplicatedIdException.class)
                    .hasMessage("이미 존재하는 아이디입니다.");

            log.info("""
                \tthen
                \t  ┗ assertThat(result).isInstanceOf(DuplicatedIdException.class)
                \t  ┗ assertThat(result).hasMessage("이미 존재하는 아이디입니다.")
                """);
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
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .name("test_dormitory_name")
                    .build();

            given(dormitoryRepository.findAll()).willReturn(List.of(dormitoryEntity));

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryEntity" +
                    "\n\t  ┗ id: " + dormitoryEntity.getId() +
                    "\n\t  ┗ name: " + dormitoryEntity.getName() +
                    "\n\t  ┗ given(dormitoryRepository.findAll()).willReturn(List.of(dormitoryEntity))\n");
            // when
            Page<DormitoryDTO> result = dormitoryService.getAll(Pageable.unpaged());

            log.info("""

                    \twhen
                    \t  ┗ Page<DormitoryDTO> result = dormitoryService.getAll(Pageable.unpaged())
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            assertThat(result).isInstanceOf(Page.class);
            assertThatIterable(result).hasSize(1);

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isNotEmpty()
                    \t  ┗ assertThat(result).isInstanceOf(Page.class)
                    \t  ┗ assertThatIterable(result).hasSize(1)
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("getById Test")
        void getByIdTest(){
            log.info("getById Test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .name("test_dormitory_name")
                    .build();

            given(dormitoryRepository.existsById(dormitoryEntity.getId())).willReturn(true);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryEntity" +
                    "\n\t  ┗ id: " + dormitoryEntity.getId() +
                    "\n\t  ┗ name: " + dormitoryEntity.getName() +
                    "\n\t  ┗ given(dormitoryRepository.existsById(dormitoryEntity.getId())).willReturn(true)\n");
            // when
            when(dormitoryRepository.findByDormitoryId(dormitoryEntity.getId())).thenReturn(dormitoryEntity);

            DormitoryDTO result = dormitoryService.findById(dormitoryEntity.getId());

            log.info("""

                    \twhen
                    \t  ┗ when(dormitoryRepository.findByDormitoryId(dormitoryEntity.getId())).thenReturn(dormitoryEntity)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(dormitoryEntity.getId());
            assertThat(result.getName()).isEqualTo(dormitoryEntity.getName());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(dormitoryEntity.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(dormitoryEntity.getName())
                    """);
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

            when(dormitoryRepository.findByDormitoryName(dormitoryEntity.getName(), Pageable.unpaged()))
                    .thenReturn(dormitoryEntityPage);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryEntity" +
                    "\n\t\t  ┗ id: " + dormitoryEntity.getId() +
                    "\n\t\t  ┗ name: " + dormitoryEntity.getName() +
                    "\n\t  ┗ Page<DormitoryEntity> dormitoryEntityPage = Page.empty()" +
                    "\n\t  ┗ when(dormitoryRepository.findByDormitoryName(dormitoryEntity.getName(), Pageable.unpaged())).thenReturn(dormitoryEntityPage)\n");
            // when
            Page<DormitoryDTO> result = dormitoryService.findByName(dormitoryEntity.getName(), Pageable.unpaged());

            log.info("""

                    \twhen
                    \t  ┗ Page<DormitoryDTO> result = dormitoryService.findByName(dormitoryEntity.getName(), Pageable.unpaged())
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Page.class);

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result).isInstanceOf(Page.class)
                    """);
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryDTO" +
                    "\n\t\t  ┗ id: " + dormitoryDTO.getId() +
                    "\n\t\t  ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┗ given(dormitoryRepository.existsById(dormitoryDTO.getId())).willReturn(true)\n");
            // when
            dormitoryService.updateDormitory(dormitoryDTO);

            log.info("""

                \twhen
                \t  ┗ dormitoryService.updateDormitory(dormitoryDTO)
                """);
            // then
            assertAll(
                    () -> verify(dormitoryRepository, times(1)).save(any(DormitoryEntity.class)),
                    () -> assertThatCode(() -> dormitoryService.updateDormitory(dormitoryDTO)).doesNotThrowAnyException()
            );

            log.info("""

                \tthen
                \t  ┗ verify(dormitoryRepository, times(1)).save(any(DormitoryEntity.class))
                \t  ┗ assertThatCode(() -> dormitoryService.updateDormitory(dormitoryDTO)).doesNotThrowAnyException()
                """);
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryDTO" +
                    "\n\t\t  ┗ id: " + dormitoryDTO.getId() +
                    "\n\t\t  ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┗ given(dormitoryRepository.existsById(dormitoryDTO.getId())).willReturn(false)\n");
            // when
            Throwable result = catchThrowable(() -> dormitoryService.updateDormitory(dormitoryDTO));

            log.info("""

                \twhen
                \t  ┗ Throwable result = catchThrowable(() -> dormitoryService.updateDormitory(dormitoryDTO))
                """);
            // then
            assertThat(result).isInstanceOf(DormitoryNotFoundException.class)
                    .hasMessage(ErrorCode.DORMITORY_NOT_FOUND.getMessage());

            log.info("""
                
                \tthen
                \t  ┗ assertThat(result).isInstanceOf(DormitoryNotFoundException.class)
                \t  ┗ assertThat(result).hasMessage(ErrorCode.DORMITORY_NOT_FOUND.getMessage())
                """);
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

            when(dormitoryRepository.existsById(dormitoryId)).thenReturn(true);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryId: " + dormitoryId +
                    "\n\t  ┗ when(dormitoryRepository.existsById(dormitoryId)).thenReturn(true)\n");
            // when
            dormitoryService.deleteById(dormitoryId);

            // then
            assertAll(
                    () -> verify(dormitoryRepository, times(1)).deleteById(dormitoryId),
                    () -> assertThatCode(() -> dormitoryService.deleteById(dormitoryId)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┗ verify(dormitoryRepository, times(1)).deleteById(dormitoryId)
                    \t  ┗ assertThatCode(() -> dormitoryService.deleteById(dormitoryId)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @DisplayName("deleteById Test (존재하지 않는 기숙사)")
        void deleteByIdTestNotFound(){
            log.info("deleteById Test (존재하지 않는 기숙사)");
            // given
            String dormitoryId = "dor_3";

            when(dormitoryRepository.existsById(dormitoryId)).thenReturn(false);

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryId: " + dormitoryId +
                    "\n\t  ┗ when(dormitoryRepository.existsById(dormitoryId)).thenReturn(false)\n");
            // when
            Throwable result = catchThrowable(() -> dormitoryService.deleteById(dormitoryId));

            log.info("""

                    \twhen
                    \t  ┗ Throwable result = catchThrowable(() -> dormitoryService.deleteById(dormitoryId))
                    """);
            // then
            assertThat(result).isInstanceOf(DormitoryNotFoundException.class)
                    .hasMessage(ErrorCode.DORMITORY_NOT_FOUND.getMessage());

            log.info("""
                
                    \tthen
                    \t  ┗ assertThat(result).isInstanceOf(DormitoryNotFoundException.class)
                    \t  ┗ assertThat(result).hasMessage(ErrorCode.DORMITORY_NOT_FOUND.getMessage())
                    """);
        }
    }
}
