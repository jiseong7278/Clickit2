package com.project.clickit.seat;

import com.project.clickit.entity.FacilityEntity;
import com.project.clickit.entity.SeatEntity;
import com.project.clickit.repository.SeatRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIterable;

@Slf4j
@DataJpaTest
@DisplayName("SeatRepository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SeatRepositoryTest {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("existsById Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ExistsById{
        @Test
        @Order(1)
        @DisplayName("existsById Test")
        void existsByIdTest(){
            log.info("existsById Test");
            // given
            String id = "bad1_1";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id);
            // when
            Boolean result = seatRepository.existsById(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ result: " + result);
            // then
            assertThat(result).isTrue();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isTrue()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("existsById Test - False")
        void existsByIdTestFalse(){
            log.info("existsById Test - False");
            // given
            String id = "bad1_2";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id);
            // when
            Boolean result = seatRepository.existsById(id);

            log.info("\n\twhen" +
                    "\n\t  ┗ result: " + result);
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
        @DisplayName("Create Test")
        void createTest(){
            log.info("Create Test");
            // given
            FacilityEntity facilityEntity = FacilityEntity.builder()
                    .id("dor_1_badminton")
                    .build();

            SeatEntity seatEntity = SeatEntity.builder()
                    .id("bad10_10")
                    .name("배드민턴10")
                    .time(18)
                    .isEmpty(true)
                    .facilityEntity(facilityEntity)
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ SeatEntity" +
                    "\n\t    ┗ id: " + seatEntity.getId() +
                    "\n\t    ┗ name: " + seatEntity.getName() +
                    "\n\t    ┗ time: " + seatEntity.getTime() +
                    "\n\t    ┗ isEmpty: " + seatEntity.getIsEmpty() +
                    "\n\t    ┗ facilityEntity" +
                    "\n\t      ┗ id: " + seatEntity.getFacilityEntity().getId() +"\n");
            // when
            SeatEntity result = seatRepository.save(seatEntity);

            log.info("""

                    \twhen
                    \t  ┗ SeatEntity result = seatRepository.save(seatEntity)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(seatEntity.getId());
            assertThat(result.getName()).isEqualTo(seatEntity.getName());
            assertThat(result.getTime()).isEqualTo(seatEntity.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(seatEntity.getIsEmpty());
            assertThat(result.getFacilityEntity().getId()).isEqualTo(seatEntity.getFacilityEntity().getId());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(seatEntity.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(seatEntity.getName())
                    \t  ┗ assertThat(result.getTime()).isEqualTo(seatEntity.getTime())
                    \t  ┗ assertThat(result.getIsEmpty()).isEqualTo(seatEntity.getIsEmpty())
                    \t  ┗ assertThat(result.getFacilityEntity().getId()).isEqualTo(seatEntity.getFacilityEntity().getId())
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("CreateList Test")
        void CreateListTest(){
            log.info("CreateList Test");
            // given
            FacilityEntity facilityEntity = FacilityEntity.builder()
                    .id("dor_1_badminton")
                    .build();

            SeatEntity seatEntity1 = SeatEntity.builder()
                    .id("bad10_10")
                    .name("배드민턴10")
                    .time(18)
                    .isEmpty(true)
                    .facilityEntity(facilityEntity)
                    .build();

            SeatEntity seatEntity2 = SeatEntity.builder()
                    .id("bad10_11")
                    .name("배드민턴11")
                    .time(18)
                    .isEmpty(true)
                    .facilityEntity(facilityEntity)
                    .build();

            List<SeatEntity> seatEntityList = List.of(seatEntity1, seatEntity2);

            log.info("\n\tgiven" +
                    "\n\t  ┗ SeatEntityList" +
                    "\n\t    ┗ SeatEntity1" +
                    "\n\t      ┗ id: " + seatEntity1.getId() +
                    "\n\t      ┗ name: " + seatEntity1.getName() +
                    "\n\t      ┗ time: " + seatEntity1.getTime() +
                    "\n\t      ┗ isEmpty: " + seatEntity1.getIsEmpty() +
                    "\n\t      ┗ facilityEntity" +
                    "\n\t        ┗ id: " + seatEntity1.getFacilityEntity().getId() +
                    "\n\t    ┗ SeatEntity2" +
                    "\n\t      ┗ id: " + seatEntity2.getId() +
                    "\n\t      ┗ name: " + seatEntity2.getName() +
                    "\n\t      ┗ time: " + seatEntity2.getTime() +
                    "\n\t      ┗ isEmpty: " + seatEntity2.getIsEmpty() +
                    "\n\t      ┗ facilityEntity" +
                    "\n\t        ┗ id: " + seatEntity2.getFacilityEntity().getId() +"\n");
            // when
            List<SeatEntity> result = seatRepository.saveAll(seatEntityList);

            log.info("""

                    \twhen
                    \t  ┗ List<SeatEntity> result = seatRepository.saveAll(seatEntityList)
                    """);
            // then
            assertThatIterable(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(seatEntity1.getId());
            assertThat(result.get(0).getName()).isEqualTo(seatEntity1.getName());
            assertThat(result.get(0).getTime()).isEqualTo(seatEntity1.getTime());
            assertThat(result.get(0).getIsEmpty()).isEqualTo(seatEntity1.getIsEmpty());
            assertThat(result.get(0).getFacilityEntity().getId()).isEqualTo(seatEntity1.getFacilityEntity().getId());
            assertThat(result.get(1).getId()).isEqualTo(seatEntity2.getId());
            assertThat(result.get(1).getName()).isEqualTo(seatEntity2.getName());
            assertThat(result.get(1).getTime()).isEqualTo(seatEntity2.getTime());
            assertThat(result.get(1).getIsEmpty()).isEqualTo(seatEntity2.getIsEmpty());
            assertThat(result.get(1).getFacilityEntity().getId()).isEqualTo(seatEntity2.getFacilityEntity().getId());

            log.info("""

                    \tthen
                    \t  ┗ assertThatIterable(result).hasSize(2)
                    \t  ┗ assertThat(result.get(0).getId()).isEqualTo(seatEntity1.getId())
                    \t  ┗ assertThat(result.get(0).getName()).isEqualTo(seatEntity1.getName())
                    \t  ┗ assertThat(result.get(0).getTime()).isEqualTo(seatEntity1.getTime())
                    \t  ┗ assertThat(result.get(0).getIsEmpty()).isEqualTo(seatEntity1.getIsEmpty())
                    \t  ┗ assertThat(result.get(0).getFacilityEntity().getId()).isEqualTo(seatEntity1.getFacilityEntity().getId())
                    \t  ┗ assertThat(result.get(1).getId()).isEqualTo(seatEntity2.getId())
                    \t  ┗ assertThat(result.get(1).getName()).isEqualTo(seatEntity2.getName())
                    \t  ┗ assertThat(result.get(1).getTime()).isEqualTo(seatEntity2.getTime())
                    \t  ┗ assertThat(result.get(1).getIsEmpty()).isEqualTo(seatEntity2.getIsEmpty())
                    \t  ┗ assertThat(result.get(1).getFacilityEntity().getId()).isEqualTo(seatEntity2.getFacilityEntity().getId())
                    """);
        }
    }

    @Nested
    @DisplayName("Read")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest(){
            log.info("getAll Test");
            // given

            // when

            // then
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest(){

        }

        @Test
        @Order(3)
        @DisplayName("findByFacilityId Test")
        void findByFacilityIdTest(){
            log.info("findByFacilityId Test");
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
        @DisplayName("updateSeat Test")
        void updateSeatTest(){
            log.info("updateSeat Test");
            // given
            SeatEntity originalSeat = seatRepository.findBySeatId("bad1_1");

            entityManager.clear();

            SeatEntity updateSeat = SeatEntity.builder()
                    .id(originalSeat.getId())
                    .name("배드민턴1")
                    .time(18)
                    .isEmpty(false)
                    .facilityEntity(originalSeat.getFacilityEntity())
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ originalSeat" +
                    "\n\t    ┗ id: " + originalSeat.getId() +
                    "\n\t    ┗ name: " + originalSeat.getName() +
                    "\n\t    ┗ time: " + originalSeat.getTime() +
                    "\n\t    ┗ isEmpty: " + originalSeat.getIsEmpty() +
                    "\n\t    ┗ facilityEntity" +
                    "\n\t      ┗ id: " + originalSeat.getFacilityEntity().getId() +
                    "\n\t  ┗ updateSeat" +
                    "\n\t    ┗ id: " + updateSeat.getId() +
                    "\n\t    ┗ name: " + updateSeat.getName() +
                    "\n\t    ┗ time: " + updateSeat.getTime() +
                    "\n\t    ┗ isEmpty: " + updateSeat.getIsEmpty() +
                    "\n\t    ┗ facilityEntity" +
                    "\n\t      ┗ id: " + updateSeat.getFacilityEntity().getId() +"\n");
            // when
            SeatEntity result = seatRepository.save(updateSeat);

            entityManager.flush();

            entityManager.clear();

            log.info("""

                    \twhen
                    \t  ┗ SeatEntity result = seatRepository.save(updateSeat)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(updateSeat.getId());
            assertThat(result.getName()).isEqualTo(updateSeat.getName());
            assertThat(result.getTime()).isEqualTo(updateSeat.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(updateSeat.getIsEmpty());
            assertThat(result.getFacilityEntity().getId()).isEqualTo(updateSeat.getFacilityEntity().getId());
            assertThat(result.getTime()).isNotEqualTo(originalSeat.getTime());
            assertThat(result.getIsEmpty()).isNotEqualTo(originalSeat.getIsEmpty());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(updateSeat.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(updateSeat.getName())
                    \t  ┗ assertThat(result.getTime()).isEqualTo(updateSeat.getTime())
                    \t  ┗ assertThat(result.getIsEmpty()).isEqualTo(updateSeat.getIsEmpty())
                    \t  ┗ assertThat(result.getFacilityEntity().getId()).isEqualTo(updateSeat.getFacilityEntity().getId())
                    \t  ┗ assertThat(result.getTime()).isNotEqualTo(originalSeat.getTime())
                    \t  ┗ assertThat(result.getIsEmpty()).isNotEqualTo(originalSeat.getIsEmpty())
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("updateSeatFacility Test")
        void updateSeatFacilityTest(){
            log.info("updateSeatFacility Test");
            // given
            String facilityId = "dor_1_pingpong";
            String seatId = "bad1_1";

            SeatEntity originalSeat = seatRepository.findBySeatId(seatId);

            entityManager.clear();

            log.info("\n\tgiven" +
                    "\n\t  ┗ facilityId: " + facilityId +
                    "\n\t  ┗ seatId: " + seatId +
                    "\n\t  ┗ originalSeat" +
                    "\n\t    ┗ id: " + originalSeat.getId() +
                    "\n\t    ┗ name: " + originalSeat.getName() +
                    "\n\t    ┗ time: " + originalSeat.getTime() +
                    "\n\t    ┗ isEmpty: " + originalSeat.getIsEmpty() +
                    "\n\t    ┗ facilityEntity" +
                    "\n\t      ┗ id: " + originalSeat.getFacilityEntity().getId() +"\n");
            // when
            seatRepository.updateSeatFacility(seatId, facilityId);

            entityManager.flush();

            entityManager.clear();

            SeatEntity result = seatRepository.findBySeatId(seatId);

            log.info("""

                    \twhen
                    \t  ┗ seatRepository.updateSeatFacility(seatId, facilityId)
                    \t  ┗ SeatEntity result = seatRepository.findBySeatId(seatId)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalSeat.getId());
            assertThat(result.getName()).isEqualTo(originalSeat.getName());
            assertThat(result.getTime()).isEqualTo(originalSeat.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(originalSeat.getIsEmpty());
            assertThat(result.getFacilityEntity().getId()).isEqualTo(facilityId);

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(originalSeat.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(originalSeat.getName())
                    \t  ┗ assertThat(result.getTime()).isEqualTo(originalSeat.getTime())
                    \t  ┗ assertThat(result.getIsEmpty()).isEqualTo(originalSeat.getIsEmpty())
                    \t  ┗ assertThat(result.getFacilityEntity().getId()).isEqualTo(facilityId)
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("updateSeatIsEmpty Test")
        void updateSeatIsEmptyTest(){
            log.info("updateSeatIsEmpty Test");
            // given
            String seatId = "bad1_1";
            Boolean isEmpty = false;

            SeatEntity originalSeat = seatRepository.findBySeatId(seatId);

            entityManager.clear();

            log.info("\n\tgiven" +
                    "\n\t  ┗ seatId: " + seatId +
                    "\n\t  ┗ isEmpty: " + isEmpty +
                    "\n\t  ┗ originalSeat" +
                    "\n\t    ┗ id: " + originalSeat.getId() +
                    "\n\t    ┗ name: " + originalSeat.getName() +
                    "\n\t    ┗ time: " + originalSeat.getTime() +
                    "\n\t    ┗ isEmpty: " + originalSeat.getIsEmpty() +
                    "\n\t    ┗ facilityEntity" +
                    "\n\t      ┗ id: " + originalSeat.getFacilityEntity().getId() +"\n");
            // when
            seatRepository.updateSeatIsEmpty(seatId, isEmpty);

            entityManager.flush();

            entityManager.clear();

            SeatEntity result = seatRepository.findBySeatId(seatId);

            log.info("""

                    \twhen
                    \t  ┗ seatRepository.updateSeatIsEmpty(seatId, isEmpty)
                    \t  ┗ SeatEntity result = seatRepository.findBySeatId(seatId)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalSeat.getId());
            assertThat(result.getName()).isEqualTo(originalSeat.getName());
            assertThat(result.getTime()).isEqualTo(originalSeat.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(isEmpty);
            assertThat(result.getFacilityEntity().getId()).isEqualTo(originalSeat.getFacilityEntity().getId());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getId()).isEqualTo(originalSeat.getId())
                    \t  ┗ assertThat(result.getName()).isEqualTo(originalSeat.getName())
                    \t  ┗ assertThat(result.getTime()).isEqualTo(originalSeat.getTime())
                    \t  ┗ assertThat(result.getIsEmpty()).isEqualTo(isEmpty)
                    \t  ┗ assertThat(result.getFacilityEntity().getId()).isEqualTo(originalSeat.getFacilityEntity().getId())
                    """);
        }
    }

    @Nested
    @DisplayName("Delete")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Delete{
        @Test
        @Order(1)
        @DisplayName("deleteById Test")
        void deleteByIdTest(){
            log.info("deleteById Test");
            // given
            String id = "bad1_1";

            SeatEntity originalSeat = seatRepository.findBySeatId(id);

            entityManager.clear();

            log.info("\n\tgiven" +
                    "\n\t  ┗ id: " + id +
                    "\n\t  ┗ originalSeat" +
                    "\n\t    ┗ id: " + originalSeat.getId() +
                    "\n\t    ┗ name: " + originalSeat.getName() +
                    "\n\t    ┗ time: " + originalSeat.getTime() +
                    "\n\t    ┗ isEmpty: " + originalSeat.getIsEmpty() +
                    "\n\t    ┗ facilityEntity" +
                    "\n\t      ┗ id: " + originalSeat.getFacilityEntity().getId() +"\n");
            // when
            seatRepository.deleteById(id);

            entityManager.flush();

            entityManager.clear();

            SeatEntity result = seatRepository.findBySeatId(id);

            log.info("""

                    \twhen
                    \t  ┗ seatRepository.deleteById(id)
                    \t  ┗ SeatEntity result = seatRepository.findBySeatId(id)
                    """);
            // then
            assertThat(result).isNull();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNull()
                    """);
        }
    }
}
