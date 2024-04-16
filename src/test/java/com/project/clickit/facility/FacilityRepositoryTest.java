package com.project.clickit.facility;

import com.project.clickit.repository.FacilityRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@DisplayName("FacilityRepository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FacilityRepositoryTest {
    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("existsById test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ExistsById{
        @Test
        @Order(1)
        @DisplayName("existsById test")
        void existsByIdTest(){
            log.info("existsById test");
            // given

            // when

            // then
        }

        @Test
        @Order(2)
        @DisplayName("existsById test - False")
        void existsByIdTestFalse(){
            log.info("existsById test - False");
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
        @DisplayName("create test")
        void createTest(){
            log.info("create test");
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

        @Test
        @Order(4)
        @DisplayName("findByDormitoryId test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId test");
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
        @DisplayName("update test")
        void updateTest(){
            log.info("update test");
            // given

            // when

            // then
        }
    }
}
