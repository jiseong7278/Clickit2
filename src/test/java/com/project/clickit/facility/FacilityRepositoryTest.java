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

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id +"\n");
            // when
            Boolean result = facilityRepository.existsById(id);

            log.info("""

                    \twhen
                    \t  ┗ Boolean result = facilityRepository.existsById(id)
                    """);
            // then
            assertThat(result).isTrue();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isTrue()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("existsById test - False")
        void existsByIdTestFalse(){
            log.info("existsById test - False");
            // given
            String id = "fac_100";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id +"\n");
            // when
            Boolean result = facilityRepository.existsById(id);

            log.info("""

                    \twhen
                    \t  ┗ Boolean result = facilityRepository.existsById(id)
                    """);
            // then
            assertThat(result).isFalse();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isFalse()
                    """);
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ facilityEntity" +
                    "\n\t\t  ┗ id: " + facilityEntity.getId() +
                    "\n\t\t  ┗ name: " + facilityEntity.getName() +
                    "\n\t\t  ┗ info: " + facilityEntity.getInfo() +
                    "\n\t\t  ┗ open: " + facilityEntity.getOpen() +
                    "\n\t\t  ┗ close: " + facilityEntity.getClose() +
                    "\n\t\t  ┗ capacity: " + facilityEntity.getCapacity() +
                    "\n\t\t  ┗ terms: " + facilityEntity.getTerms() +
                    "\n\t\t  ┗ dormitoryEntity " +
                    "\n\t\t\t  ┗ id: " + facilityEntity.getDormitoryEntity().getId() + "\n");
            // when
            FacilityEntity result = facilityRepository.save(facilityEntity);

            log.info("""

                    \twhen
                    \t  ┗ FacilityEntity result = facilityRepository.save(facilityEntity)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(facilityEntity.getId());
            assertThat(result.getName()).isEqualTo(facilityEntity.getName());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(facilityEntity.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(facilityEntity.getName())
                    """);
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
            Page<FacilityEntity> result = facilityRepository.findAll(Pageable.unpaged());

            log.info("""

                    \twhen
                    \t  ┗ Page<FacilityEntity> result = facilityRepository.findAll(Pageable.unpaged())
                    """);
            // then
            assertThat(result).isNotNull();
            assertThatIterable(result).isNotEmpty();
            assertThat(result).isInstanceOf(Page.class);

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThatIterable(result).isNotEmpty()
                    \t  ┗ assertThat(result).isInstanceOf(Page.class)
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("findById test")
        void findByIdTest(){
            log.info("findById test");
            // given
            String id = "dor_1_badminton";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id + "\n");
            // when
            FacilityEntity result = facilityRepository.findByFacilityId(id);

            log.info("""

                    \twhen
                    \t  ┗ FacilityEntity result = facilityRepository.findByFacilityId(id)
                    """);
            // then
            assertThat(result).isNotNull();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("findByName test")
        void findByNameTest(){
            log.info("findByName test");
            // given
            String name = "탁구장";

            log.info("\n\tgiven" +
                    "\n\t  ┗ name: " + name + "\n");
            // when
            FacilityEntity result = facilityRepository.findByFacilityName(name);

            log.info("""

                    \twhen
                    \t  ┗ FacilityEntity result = facilityRepository.findByFacilityName(name)
                    """);
            // then
            assertThat(result).isNotNull();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("findByDormitoryId test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId test");
            // given
            String dormitoryId = "dor_1";

            log.info("\n\tgiven" +
                    "\n\t  ┗ dormitoryId: " + dormitoryId + "\n");
            // when
            Page<FacilityEntity> result = facilityRepository.findByDormitoryId(dormitoryId, Pageable.unpaged());

            log.info("""

                    \twhen
                    \t  ┗ Page<FacilityEntity> result = facilityRepository.findByDormitoryId(dormitoryId, Pageable.unpaged())
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ FacilityEntity originalFacility" +
                    "\n\t\t  ┗ id: " + originalFacility.getId() +
                    "\n\t\t  ┗ name: " + originalFacility.getName() +
                    "\n\t\t  ┗ info: " + originalFacility.getInfo() +
                    "\n\t\t  ┗ open: " + originalFacility.getOpen() +
                    "\n\t\t  ┗ close: " + originalFacility.getClose() +
                    "\n\t\t  ┗ capacity: " + originalFacility.getCapacity() +
                    "\n\t\t  ┗ terms: " + originalFacility.getTerms() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id: " + originalFacility.getDormitoryEntity().getId() +
                    "\n\t  ┗ FacilityEntity updateFacility" +
                    "\n\t\t  ┗ id: " + updateFacility.getId() +
                    "\n\t\t  ┗ name: " + updateFacility.getName() +
                    "\n\t\t  ┗ info: " + updateFacility.getInfo() +
                    "\n\t\t  ┗ open: " + updateFacility.getOpen() +
                    "\n\t\t  ┗ close: " + updateFacility.getClose() +
                    "\n\t\t  ┗ capacity: " + updateFacility.getCapacity() +
                    "\n\t\t  ┗ terms: " + updateFacility.getTerms() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id: " + updateFacility.getDormitoryEntity().getId() + "\n");
            // when
            FacilityEntity result = facilityRepository.save(updateFacility);

            log.info("""

                    \twhen
                    \t  ┗ FacilityEntity result = facilityRepository.save(updateFacility)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(updateFacility.getId());
            assertThat(result.getName()).isEqualTo(updateFacility.getName());
            assertThat(result.getName()).isNotEqualTo(originalFacility.getName());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(updateFacility.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(updateFacility.getName())
                    \t  ┗ assertThat(result.getName()).isNotEqualTo(originalFacility.getName())
                    """);
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ FacilityEntity originalFacility" +
                    "\n\t\t  ┗ id: " + originalFacility.getId() +
                    "\n\t  ┗ id: " + id +
                    "\n\t  ┗ newId: " + newId + "\n");
            // when
            facilityRepository.updateFacilityId(id, newId);

            FacilityEntity result = facilityRepository.findByFacilityId(newId);

            log.info("""

                    \twhen
                    \t  ┗ facilityRepository.updateFacilityId(id, newId)
                    \t  ┗ FacilityEntity result = facilityRepository.findByFacilityId(newId)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(newId);

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(newId)
                    """);
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

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id + "\n");
            // when
            facilityRepository.deleteById(id);

            FacilityEntity result = facilityRepository.findByFacilityId(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ facilityRepository.deleteById(id)" +
                    "\n\t  ┗ FacilityEntity result = facilityRepository.findByFacilityId(id)\n");
            // then
            assertThat(result).isNull();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isNull()\n");
        }
    }
}
