package com.project.clickit.dormitory;

import com.project.clickit.repository.DormitoryRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

            // when

            // then
        }

        @Test
        @Order(2)
        @DisplayName("existsById Test - False")
        void existsByIdTestFalse(){
            log.info("existsById Test - False");
            // given

            // when

            // then
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

            // when

            // then
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

            // then
        }

        @Test
        @Order(2)
        @DisplayName("findById test")
        void findByIdTest(){
            log.info("findById test");
            // given

            // when

            // then
        }

        @Test
        @Order(3)
        @DisplayName("findByName test")
        void findByNameTest(){
            log.info("findByName test");
            // given

            // when

            // then
        }
    }

    @Nested
    @DisplayName("Update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Update{
        @Test
        @Order(1)
        @DisplayName("updateDormitoryName test")
        void updateDormitoryNameTest(){
            log.info("updateDormitoryName test");
            // given

            // when

            // then
        }

        @Test
        @Order(2)
        @DisplayName("update test")
        void updateTest(){
            log.info("update test");
            // given

            // when

            // then
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

            // when

            // then
        }

        @Test
        @Order(2)
        @DisplayName("delete test")
        void deleteTest(){
            log.info("delete test");
            // given

            // when

            // then
        }
    }
}
