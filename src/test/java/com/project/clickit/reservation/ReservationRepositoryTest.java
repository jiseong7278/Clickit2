//package com.project.clickit.reservation;
//
//import com.project.clickit.entity.MemberEntity;
//import com.project.clickit.entity.ReservationEntity;
//import com.project.clickit.entity.SeatEntity;
//import com.project.clickit.repository.ReservationRepository;
//import jakarta.persistence.EntityManager;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatIterable;
//
//@Slf4j
//@DataJpaTest
//@DisplayName("ReservationRepository Test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ReservationRepositoryTest {
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    private SeatEntity seatEntity;
//
//    private MemberEntity memberEntity;
//
//    @BeforeEach
//    void setUp(){
//        seatEntity = SeatEntity.builder()
//                .id("ping1_1")
//                .build();
//
//        memberEntity = MemberEntity.builder()
//                .id("test_member_id")
//                .build();
//    }
//
//    @Nested
//    @DisplayName("Create")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Create{
//        @Test
//        @Order(1)
//        @DisplayName("Create Test")
//        void createTest(){
//            log.info("Create Test");
//            // given
//            ReservationEntity reservationEntity = ReservationEntity.builder()
//                    .seatEntity(seatEntity)
//                    .memberEntity(memberEntity)
//                    .timestamp(LocalDateTime.now())
//                    .status("예약")
//                    .build();
//
//            log.info("create test given: ✔");
//            // when
//            ReservationEntity result = reservationRepository.save(reservationEntity);
//
//            log.info("create test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getSeatEntity().getId()).isEqualTo(seatEntity.getId());
//            assertThat(result.getMemberEntity().getId()).isEqualTo(memberEntity.getId());
//            assertThat(result.getStatus()).isEqualTo("예약");
//
//            log.info("create test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Read")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Read{
//        @Test
//        @Order(1)
//        @DisplayName("findAll Test")
//        void findAllTest(){
//            log.info("findAll Test");
//            // given
//
//            log.info("findAll Test given: ✔");
//            // when
//            Page<ReservationEntity> result = reservationRepository.findAll(Pageable.unpaged());
//
//            log.info("findAll Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result).isNotEmpty();
//            assertThat(result).isInstanceOf(Page.class);
//            assertThatIterable(result).isInstanceOf(Page.class);
//
//            log.info("findAll Test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("findByMemberId Test")
//        void findByMemberIdTest(){
//            log.info("findByMemberId Test");
//            // given
//
//            log.info("findByMemberId Test given: ✔");
//            // when
//            Page<ReservationEntity> result = reservationRepository.findByMemberEntityId(memberEntity.getId(), Pageable.unpaged());
//
//            log.info("findByMemberId Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result).isNotEmpty();
//            assertThat(result).isInstanceOf(Page.class);
//            assertThatIterable(result).map(ReservationEntity::getMemberEntity).extracting(MemberEntity::getId).contains(memberEntity.getId());
//
//            log.info("findByMemberId Test then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("findBySeatIdAndToday Test")
//        void findBySeatIdAndTodayTest(){
//            log.info("findBySeatIdAndToday Test");
//            // given
//
//            log.info("findBySeatIdAndToday Test given: ✔");
//            // when
//            Page<ReservationEntity> result = reservationRepository.findBySeatEntityIdAndToday(seatEntity.getId(), Pageable.unpaged());
//
//            log.info("findBySeatIdAndToday Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result).isNotEmpty();
//            assertThat(result).isInstanceOf(Page.class);
//            assertThatIterable(result).map(ReservationEntity::getSeatEntity).extracting(SeatEntity::getId).contains(seatEntity.getId());
//
//            log.info("findBySeatIdAndToday Test then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("findByMemberIdAndToday Test")
//        void findByMemberIdAndTodayTest(){
//            log.info("findByMemberIdAndToday Test");
//            // given
//
//            log.info("findByMemberIdAndToday Test given: ✔");
//            // when
//            Page<ReservationEntity> result = reservationRepository.findByMemberEntityIdAndToday(memberEntity.getId(), Pageable.unpaged());
//
//            log.info("findByMemberIdAndToday Test when: ✔");
//            // then
//
//            log.info("만약 예외가 발생한다면 DB에 저장된 데이터의 timestamp가 현재 날짜와 일치하는지 확인");
//
//            assertThat(result).isNotNull();
//            assertThat(result).isNotEmpty();
//            assertThat(result).isInstanceOf(Page.class);
//            assertThatIterable(result).map(ReservationEntity::getMemberEntity).extracting(MemberEntity::getId).contains(memberEntity.getId());
//
//            log.info("findByMemberIdAndToday Test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Update")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Update{
//        @Test
//        @Order(1)
//        @DisplayName("Update Test")
//        void updateTest(){
//            log.info("Update Test");
//            // given
//            ReservationEntity originalReservation = reservationRepository.findByNum(1);
//
//            entityManager.clear();
//
//            ReservationEntity updateReservation = ReservationEntity.builder()
//                    .num(originalReservation.getNum())
//                    .seatEntity(originalReservation.getSeatEntity())
//                    .memberEntity(originalReservation.getMemberEntity())
//                    .timestamp(LocalDateTime.now())
//                    .status("취소")
//                    .build();
//
//            log.info("update test given: ✔");
//            // when
//            ReservationEntity result = reservationRepository.save(updateReservation);
//
//            log.info("update test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getNum()).isEqualTo(originalReservation.getNum());
//            assertThat(result.getSeatEntity().getId()).isEqualTo(originalReservation.getSeatEntity().getId());
//            assertThat(result.getMemberEntity().getId()).isEqualTo(originalReservation.getMemberEntity().getId());
//            assertThat(result.getTimestamp()).isNotEqualTo(originalReservation.getTimestamp());
//            assertThat(result.getStatus()).isNotEqualTo(originalReservation.getStatus());
//
//            log.info("update test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("updateReservationStatus Test")
//        void updateReservationStatusTest(){
//            log.info("updateReservationStatus Test");
//            // given
//            ReservationEntity originalReservation = reservationRepository.findByNum(1);
//
//            entityManager.clear();
//
//            String status = "취소";
//
//            log.info("updateReservationStatus Test given: ✔");
//            // when
//            reservationRepository.updateReservationStatus(originalReservation.getNum(), status);
//
//            ReservationEntity result = reservationRepository.findByNum(originalReservation.getNum());
//
//            log.info("updateReservationStatus Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getNum()).isEqualTo(originalReservation.getNum());
//            assertThat(result.getSeatEntity().getId()).isEqualTo(originalReservation.getSeatEntity().getId());
//            assertThat(result.getMemberEntity().getId()).isEqualTo(originalReservation.getMemberEntity().getId());
//            assertThat(result.getTimestamp()).isEqualTo(originalReservation.getTimestamp());
//            assertThat(result.getStatus()).isEqualTo(status);
//            assertThat(result.getStatus()).isNotEqualTo(originalReservation.getStatus());
//
//            log.info("updateReservationStatus Test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Delete")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Delete{
//        @Test
//        @Order(1)
//        @DisplayName("Delete Test")
//        void deleteTest(){
//            log.info("Delete Test");
//            // given
//            Integer num = 1;
//
//            log.info("delete test given: ✔");
//            // when
//            reservationRepository.deleteByNum(num);
//
//            Boolean result = reservationRepository.existsByNum(num);
//
//            log.info("delete test when: ✔");
//            // then
//            assertThat(result).isNull();
//
//            log.info("delete test then: ✔");
//        }
//    }
//}
