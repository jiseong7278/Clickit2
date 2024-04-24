package com.project.clickit.facility;

import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.repository.FacilityRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIterable;

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
            String id = "dor_1_badminton";

            log.info("existsById test given: ✔");
            // when
            Boolean result = facilityRepository.existsById(id);

            log.info("existsById test when: ✔");
            // then
            assertThat(result).isTrue();

            log.info("existsById test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("existsById test - False")
        void existsByIdTestFalse(){
            log.info("existsById test - False");
            // given
            String id = "fac_100";

            log.info("existsById test - False | given: ✔");
            // when
            Boolean result = facilityRepository.existsById(id);

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
        @DisplayName("create test")
        void createTest(){
            log.info("create test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .build();

            FacilityEntity facilityEntity = FacilityEntity.builder()
                    .id("fac_create")
                    .name("fac_name")
                    .info("fac_info")
                    .open(10)
                    .close(22)
                    .capacity(100)
                    .terms("fac_terms")
                    .dormitoryEntity(dormitoryEntity)
                    .build();

            log.info("create test given: ✔");
            // when
            FacilityEntity result = facilityRepository.save(facilityEntity);

            log.info("create test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(facilityEntity.getId());
            assertThat(result.getName()).isEqualTo(facilityEntity.getName());

            log.info("create test then: ✔");
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
            Page<FacilityEntity> result = facilityRepository.findAll(Pageable.unpaged());

            log.info("getAll test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThatIterable(result).isNotEmpty();
            assertThat(result).isInstanceOf(Page.class);

            log.info("getAll test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findById test")
        void findByIdTest(){
            log.info("findById test");
            // given
            String id = "dor_1_badminton";

            log.info("findById test given: ✔");
            // when
            FacilityEntity result = facilityRepository.findByFacilityId(id);

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
            String name = "탁구장";

            log.info("findByName test given: ✔");
            // when
            Page<FacilityEntity> result = facilityRepository.findByFacilityName(name, Pageable.unpaged());

            log.info("findByName test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Page.class);

            log.info("findByName test then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByDormitoryId test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId test");
            // given
            String dormitoryId = "dor_1";

            log.info("findByDormitoryId test given: ✔");
            // when
            Page<FacilityEntity> result = facilityRepository.findByDormitoryId(dormitoryId, Pageable.unpaged());

            log.info("findByDormitoryId test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Page.class);

            log.info("findByDormitoryId test then: ✔");
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
            FacilityEntity originalFacility = facilityRepository.findByFacilityId("dor_1_badminton");

            entityManager.clear();

            FacilityEntity updateFacility = FacilityEntity.builder()
                    .id(originalFacility.getId())
                    .name("update")
                    .info("update")
                    .open(10)
                    .close(22)
                    .capacity(100)
                    .terms("update")
                    .dormitoryEntity(originalFacility.getDormitoryEntity())
                    .build();

            log.info("update test given: ✔");
            // when
            FacilityEntity result = facilityRepository.save(updateFacility);

            log.info("update test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(updateFacility.getId());
            assertThat(result.getName()).isEqualTo(updateFacility.getName());
            assertThat(result.getName()).isNotEqualTo(originalFacility.getName());

            log.info("update test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("updateFacilityId test")
        void updateFacilityIdTest(){
            log.info("updateFacilityId test");
            // given
            FacilityEntity originalFacility = facilityRepository.findByFacilityId("dor_1_badminton");

            entityManager.clear();

            String id = "dor_1_badminton";
            String newId = "new_fac_id";

            log.info("updateFacilityId test given: ✔");
            // when
            facilityRepository.updateFacilityId(id, newId);

            log.info("updateFacilityId test when: ✔");
            // then
            FacilityEntity result = facilityRepository.findByFacilityId(newId);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(newId);
            assertThat(result.getId()).isNotEqualTo(originalFacility.getId());

            log.info("updateFacilityId test then: ✔");
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
            String id = "dor_1_badminton";

            log.info("deleteById test given: ✔");
            // when
            facilityRepository.deleteById(id);

            log.info("deleteById test when: ✔");
            // then
            FacilityEntity result = facilityRepository.findByFacilityId(id);

            assertThat(result).isNull();

            log.info("deleteById test then: ✔");
        }
    }
}
