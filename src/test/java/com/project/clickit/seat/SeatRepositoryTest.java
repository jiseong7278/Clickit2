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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

            log.info("existsById Test given: ✔");
            // when
            Boolean result = seatRepository.existsById(id);

            log.info("existsById Test when: ✔");
            // then
            assertThat(result).isTrue();

            log.info("existsById Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("existsById Test - False")
        void existsByIdTestFalse(){
            log.info("existsById Test - False");
            // given
            String id = "bad1_2";

            log.info("existsById Test - False | given: ✔");
            // when
            Boolean result = seatRepository.existsById(id);

            log.info("existsById Test - False | when: ✔");
            // then
            assertThat(result).isFalse();

            log.info("existsById Test - False | then: ✔");
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

            log.info("Create Test given: ✔");
            // when
            SeatEntity result = seatRepository.save(seatEntity);

            log.info("Create Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(seatEntity.getId());
            assertThat(result.getName()).isEqualTo(seatEntity.getName());
            assertThat(result.getTime()).isEqualTo(seatEntity.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(seatEntity.getIsEmpty());
            assertThat(result.getFacilityEntity().getId()).isEqualTo(seatEntity.getFacilityEntity().getId());

            log.info("Create Test then: ✔");
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

            log.info("CreateList Test given: ✔");
            // when
            List<SeatEntity> result = seatRepository.saveAll(seatEntityList);

            log.info("CreateList Test when: ✔");
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

            log.info("CreateList Test then: ✔");
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

            log.info("getAll Test given: ✔");
            // when
            Page<SeatEntity> result = seatRepository.findAll(Pageable.unpaged());

            log.info("getAll Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();

            log.info("getAll Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest(){
            log.info("findById Test");
            // given
            String id = "bad1_1";

            log.info("findById Test given: ✔");
            // when
            SeatEntity result = seatRepository.findBySeatId(id);

            log.info("findById Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(id);

            log.info("findById Test then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByFacilityId Test")
        void findByFacilityIdTest(){
            log.info("findByFacilityId Test");
            // given
            String facilityId = "dor_1_badminton";

            log.info("findByFacilityId Test given: ✔");
            // when
            Page<SeatEntity> result = seatRepository.findByFacilityId(facilityId, Pageable.unpaged());

            log.info("findByFacilityId Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();

            log.info("findByFacilityId Test then: ✔");
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

            log.info("updateSeat Test given: ✔");
            // when
            SeatEntity result = seatRepository.save(updateSeat);

            entityManager.flush();

            entityManager.clear();

            log.info("updateSeat Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(updateSeat.getId());
            assertThat(result.getName()).isEqualTo(updateSeat.getName());
            assertThat(result.getTime()).isEqualTo(updateSeat.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(updateSeat.getIsEmpty());
            assertThat(result.getFacilityEntity().getId()).isEqualTo(updateSeat.getFacilityEntity().getId());
            assertThat(result.getTime()).isNotEqualTo(originalSeat.getTime());
            assertThat(result.getIsEmpty()).isNotEqualTo(originalSeat.getIsEmpty());

            log.info("updateSeat Test then: ✔");
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

            log.info("updateSeatFacility Test given: ✔");
            // when
            seatRepository.updateSeatFacility(seatId, facilityId);

            entityManager.flush();

            entityManager.clear();

            SeatEntity result = seatRepository.findBySeatId(seatId);

            log.info("updateSeatFacility Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalSeat.getId());
            assertThat(result.getName()).isEqualTo(originalSeat.getName());
            assertThat(result.getTime()).isEqualTo(originalSeat.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(originalSeat.getIsEmpty());
            assertThat(result.getFacilityEntity().getId()).isEqualTo(facilityId);

            log.info("updateSeatFacility Test then: ✔");
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

            log.info("updateSeatIsEmpty Test given: ✔");
            // when
            seatRepository.updateSeatIsEmpty(seatId, isEmpty);

            entityManager.flush();

            entityManager.clear();

            SeatEntity result = seatRepository.findBySeatId(seatId);

            log.info("updateSeatIsEmpty Test when: ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalSeat.getId());
            assertThat(result.getName()).isEqualTo(originalSeat.getName());
            assertThat(result.getTime()).isEqualTo(originalSeat.getTime());
            assertThat(result.getIsEmpty()).isEqualTo(isEmpty);
            assertThat(result.getFacilityEntity().getId()).isEqualTo(originalSeat.getFacilityEntity().getId());

            log.info("updateSeatIsEmpty Test then: ✔");
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

            log.info("deleteById Test given: ✔");
            // when
            seatRepository.deleteById(id);

            SeatEntity result = seatRepository.findBySeatId(id);

            log.info("deleteById Test when: ✔");
            // then
            assertThat(result).isNull();

            log.info("deleteById Test then: ✔");
        }
    }
}
