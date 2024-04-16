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

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id +"\n");
            // when
            Boolean result = dormitoryRepository.existsById(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ Boolean result = dormitoryRepository.existsById(id)\n");
            // then
            assertThat(result).isTrue();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isTrue()\n");
        }

        @Test
        @Order(2)
        @DisplayName("existsById Test - False")
        void existsByIdTestFalse(){
            log.info("existsById Test - False");
            // given
            String id = "dor_100";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id + "\n");
            // when
            Boolean result = dormitoryRepository.existsById(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ Boolean result = dormitoryRepository.existsById(id)\n");
            // then
            assertThat(result).isFalse();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isFalse()\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ DormitoryEntity" +
                    "\n\t\t  ┗ id: " + dormitoryEntity.getId() +
                    "\n\t\t  ┗ name: " + dormitoryEntity.getName() + "\n");
            // when
            DormitoryEntity savedDormitoryEntity = dormitoryRepository.save(dormitoryEntity);

            log.info("\n\twhen" +
                    "\n\t  ┗ DormitoryEntity savedDormitoryEntity = dormitoryRepository.save(dormitoryEntity)\n");
            // then
            assertThat(savedDormitoryEntity).isNotNull();
            assertThat(savedDormitoryEntity.getId()).isEqualTo(dormitoryEntity.getId());
            assertThat(savedDormitoryEntity.getName()).isEqualTo(dormitoryEntity.getName());

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(savedDormitoryEntity).isNotNull()" +
                    "\n\t  ┗ assertThat(savedDormitoryEntity.getId()).isEqualTo(dormitoryEntity.getId())" +
                    "\n\t  ┗ assertThat(savedDormitoryEntity.getName()).isEqualTo(dormitoryEntity.getName())\n");
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

            // when
            List<DormitoryEntity> dormitoryEntityList = dormitoryRepository.findAll();

            log.info("\n\twhen" +
                    "\n\t  ┗ List<DormitoryEntity> dormitoryEntityList = dormitoryRepository.findAll()\n");
            // then
            assertThat(dormitoryEntityList).isNotNull();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(dormitoryEntityList).isNotNull()\n");
        }

        @Test
        @Order(2)
        @DisplayName("findById test")
        void findByIdTest(){
            log.info("findById test");
            // given
            String id = "dor_1";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id + "\n");
            // when
            DormitoryEntity result = dormitoryRepository.findByDormitoryId(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ Optional<DormitoryEntity> result = dormitoryRepository.findById(id)\n");
            // then
            assertThat(result).isNotNull();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isNotNull()\n");
        }

        @Test
        @Order(3)
        @DisplayName("findByName test")
        void findByNameTest(){
            log.info("findByName test");
            // given
            String name = "테스트";

            log.info("\n\tgiven" +
                    "\n\t  ┗ name: " + name + "\n");
            // when
            Page<DormitoryEntity> result = dormitoryRepository.findByDormitoryName(name, Pageable.unpaged());

            log.info("\n\twhen" +
                    "\n\t  ┗ Page<DormitoryEntity> result = dormitoryRepository.findByDormitoryName(name, Pageable.unpaged())\n");
            // then
            assertThat(result).isNotNull();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isNotNull()\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ DormitoryEntity" +
                    "\n\t\t  ┗ id: " + dormitoryEntity.getId() +
                    "\n\t\t  ┗ name: " + dormitoryEntity.getName() +
                    "\n\t  ┗ DormitoryEntity originalDormitoryEntity" +
                    "\n\t\t  ┗ id: " + originalDormitoryEntity.getId() +
                    "\n\t\t  ┗ name: " + originalDormitoryEntity.getName() + "\n");
            // when
            DormitoryEntity result = dormitoryRepository.save(dormitoryEntity);

            log.info("\n\twhen" +
                    "\n\t  ┗ DormitoryEntity result = dormitoryRepository.save(dormitoryEntity)\n");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(dormitoryEntity.getId());
            assertThat(result.getName()).isEqualTo(dormitoryEntity.getName());
            assertThat(result.getName()).isNotEqualTo(originalDormitoryEntity.getName());

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isNotNull()" +
                    "\n\t  ┗ assertThat(result.getId()).isEqualTo(dormitoryEntity.getId())" +
                    "\n\t  ┗ assertThat(result.getName()).isEqualTo(dormitoryEntity.getName())" +
                    "\n\t  ┗ assertThat(result.getName()).isNotEqualTo(originalDormitoryEntity.getName())\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id + "\n");
            // when
            dormitoryRepository.deleteById(id);

            DormitoryEntity result = dormitoryRepository.findByDormitoryId(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ dormitoryRepository.deleteById(id)" +
                    "\n\t  ┗ DormitoryEntity result = dormitoryRepository.findByDormitoryId(id)\n");
            // then
            assertThat(result).isNull();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isNull()\n");
        }
    }
}
