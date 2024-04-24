package com.project.clickit.dormitory;

import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.repository.DormitoryRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@DisplayName("DormitoryRepository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DormitoryRepositoryTest {
    @Autowired
    private DormitoryRepository dormitoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("existById Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ExistsById{
        @Test
        @Order(1)
        @DisplayName("existsBYId Test")
        void existsByIdTest(){
            log.info("existsById Test");
            // given
            String id = "dor_1";

            log.info("existsById test given: ✔");
            // when
            Boolean result = dormitoryRepository.existsById(id);

            log.info("existsById test given: ✔");
            // then
            assertThat(result).isTrue();

            log.info("existsById test given: ✔");

        }

        @Test
        @Order(2)
        @DisplayName("existsById Test - False")
        void existsByIdTestFalse(){
            log.info("existsById Test - False");
            // given
            String id = "dor_100";

            log.info("existsById test - False | given: ✔");
            // when
            Boolean result = dormitoryRepository.existsById(id);

            log.info("existsById test - False | when: ✔");
            // then
            assertThat(result).isFalse();

            log.info("existsById test - False | then: ✔");
        }
    }

    @Nested
    @DisplayName("Create")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Create{
        @Test
        @Order(1)
        @DisplayName("Create test")
        void createTest(){
            log.info("Create test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_100")
                    .name("test")
                    .build();

            log.info("Create test given: ✔");
            // when
            DormitoryEntity savedDormitoryEntity = dormitoryRepository.save(dormitoryEntity);

            log.info("Create test when: ✔");
            // then
            assertThat(savedDormitoryEntity).isNotNull();
            assertThat(savedDormitoryEntity.getId()).isEqualTo(dormitoryEntity.getId());
            assertThat(savedDormitoryEntity.getName()).isEqualTo(dormitoryEntity.getName());

            log.info("Create test then: ✔");
        }
    }

    @Nested
    @DisplayName("Read")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("getAll test")
        void getAllTest(){
            log.info("getAll test");
            // given

            log.info("getAll test given: ✔");
            // when
            List<DormitoryEntity> dormitoryEntityList = dormitoryRepository.findAll();

            log.info("getAll test when: ✔");
            // then
            assertThat(dormitoryEntityList).isNotNull();

            log.info("getAll test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findById test")
        void findByIdTest(){
            log.info("findById test");
            // given
            String id = "dor_1";

            log.info("findById test given: ✔");
            // when
            DormitoryEntity result = dormitoryRepository.findByDormitoryId(id);

            log.info("findById test when: ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findById test then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByName test")
        void findByNameTest(){
            log.info("findByName test");
            // given
            String name = "테스트";

            log.info("findByName test given: ✔");
            // when
            Page<DormitoryEntity> result = dormitoryRepository.findByDormitoryName(name, Pageable.unpaged());

            log.info("findByName test when: ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findByName test then: ✔");
        }
    }

    @Nested
    @DisplayName("Update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Update{
        @Test
        @Order(1)
        @DisplayName("update test")
        void updateTest(){
            log.info("update test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .name("update")
                    .build();

            DormitoryEntity originalDormitoryEntity = dormitoryRepository.findByDormitoryId(dormitoryEntity.getId());

            entityManager.clear();

            log.info("update test given: ✔");
            // when
            DormitoryEntity result = dormitoryRepository.save(dormitoryEntity);

            log.info("update test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(dormitoryEntity.getId());
            assertThat(result.getName()).isEqualTo(dormitoryEntity.getName());
            assertThat(result.getName()).isNotEqualTo(originalDormitoryEntity.getName());

            log.info("update test then: ✔");
        }
    }

    @Nested
    @DisplayName("Delete")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Delete{
        @Test
        @Order(1)
        @DisplayName("deleteById test")
        void deleteByIdTest(){
            log.info("deleteById test");
            // given
            String id = "dor_1";

            log.info("deleteById test given: ✔");
            // when
            dormitoryRepository.deleteById(id);

            log.info("deleteById test when: ✔");
            // then
            DormitoryEntity result = dormitoryRepository.findByDormitoryId(id);

            assertThat(result).isNull();

            log.info("deleteById test then: ✔");
        }
    }
}
