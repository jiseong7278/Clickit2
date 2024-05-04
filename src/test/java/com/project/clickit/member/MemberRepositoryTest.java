//package com.project.clickit.member;
//
//import com.project.clickit.entity.DormitoryEntity;
//import com.project.clickit.entity.MemberEntity;
//import com.project.clickit.repository.MemberRepository;
//import jakarta.persistence.EntityManager;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Slf4j
//@DataJpaTest
//@DisplayName("Member Repository Test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class MemberRepositoryTest {
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Nested
//    @DisplayName("existsById")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class ExistsById{
//        @Test
//        @Order(1)
//        @DisplayName("existsById test")
//        void existsByIdTest(){
//            log.info("existsById test");
//            // given
//            String id = "test_member_id";
//
//            log.info("existsById test given: ✔");
//            // when
//            Boolean result = memberRepository.existsById(id);
//
//            log.info("existsById test when: ✔");
//            // then
//            assertThat(result).isTrue();
//
//            log.info("existsById test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("existsById test False")
//        void existsByIdTestFalse(){
//            log.info("existsById test False");
//            // given
//            String id = "test_member_id_false";
//
//            log.info("existsById test False given: ✔");
//            // when
//            Boolean result = memberRepository.existsById(id);
//
//            log.info("existsById test False when: ✔");
//            // then
//            assertThat(result).isFalse();
//
//            log.info("existsById test False then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Create")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Create{
//        @Test
//        @Order(1)
//        @DisplayName("save test")
//        void saveTest(){
//            log.info("save test");
//            // given
//            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
//                    .id("dor_1")
//                    .build();
//
//            MemberEntity memberEntity = MemberEntity.builder()
//                    .id("test")
//                    .password("test")
//                    .name("test")
//                    .email("test")
//                    .phone("test")
//                    .studentNum("test")
//                    .type("CLICKIT_STUDENT")
//                    .dormitoryEntity(dormitoryEntity)
//                    .build();
//
//            log.info("save test given: ✔");
//            // when
//            MemberEntity result = memberRepository.save(memberEntity);
//
//            log.info("save test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(memberEntity.getId());
//            assertThat(result.getPassword()).isEqualTo(memberEntity.getPassword());
//
//            log.info("save test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("saveAll test")
//        void saveAllTest(){
//            log.info("saveAll test");
//            // given
//            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
//                    .id("dor_2")
//                    .build();
//
//            List<MemberEntity> memberEntityList = List.of(
//                    MemberEntity.builder()
//                            .id("test1")
//                            .password("test1")
//                            .name("test1")
//                            .email("test1")
//                            .phone("test1")
//                            .studentNum("test1")
//                            .type("CLICKIT_STUDENT")
//                            .dormitoryEntity(dormitoryEntity)
//                            .build(),
//                    MemberEntity.builder()
//                            .id("test2")
//                            .password("test2")
//                            .name("test2")
//                            .email("test2")
//                            .phone("test2")
//                            .studentNum("test2")
//                            .type("CLICKIT_STUDENT")
//                            .dormitoryEntity(dormitoryEntity)
//                            .build()
//            );
//
//
//            log.info("saveAll test given: ✔");
//            // when
//            List<MemberEntity> result = memberRepository.saveAll(memberEntityList);
//
//            log.info("saveAll test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.size()).isEqualTo(memberEntityList.size());
//            assertThat(result.get(0).getId()).isEqualTo(memberEntityList.get(0).getId());
//            assertThat(result.get(0).getPassword()).isEqualTo(memberEntityList.get(0).getPassword());
//            assertThat(result.get(1).getId()).isEqualTo(memberEntityList.get(1).getId());
//            assertThat(result.get(1).getPassword()).isEqualTo(memberEntityList.get(1).getPassword());
//
//            log.info("saveAll test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Read")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Read{
//        @Test
//        @Order(1)
//        @DisplayName("findAll test")
//        void findAllTest(){
//            log.info("findAll test");
//            // given
//
//            log.info("findAll test given: ✔");
//            // when
//            Page<MemberEntity> result = memberRepository.findAll(Pageable.unpaged());
//
//            log.info("findAll test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//
//            log.info("findAll test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("findById Test")
//        void findByIdTest(){
//            log.info("findById Test");
//            // given
//            String id = "test_member_id";
//
//            log.info("findById Test given: ✔");
//            // when
//            MemberEntity result = memberRepository.findById(id);
//
//            log.info("findById Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(id);
//
//            log.info("findById Test then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("findByMemberName Test")
//        void findByMemberNameTest(){
//            log.info("findByMemberName Test");
//            // given
//            String name = "test";
//
//            log.info("findByMemberName Test given: ✔");
//            // when
//            Page<MemberEntity> result = memberRepository.findByMemberName(name, "CLICKIT_STUDENT",Pageable.unpaged());
//
//            log.info("findByMemberName Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//
//            log.info("findByMemberName Test then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("findByDormitoryId Test")
//        void findByDormitoryIdTest(){
//            log.info("findByDormitoryId Test");
//            // given
//            String dormitoryId = "dor_1";
//
//            log.info("findByDormitoryId Test given: ✔");
//            // when
//            Page<MemberEntity> result = memberRepository.findByDormitoryId(dormitoryId, Pageable.unpaged());
//
//            log.info("findByDormitoryId Test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//
//            log.info("findByDormitoryId Test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Update")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Update{
//        @Test
//        @Order(1)
//        @DisplayName("update test")
//        void updateTest() {
//            log.info("update test");
//            // given
//            MemberEntity originalMemberEntity = memberRepository.findById("test2");
//
//            entityManager.clear();
//
//            MemberEntity memberEntity = MemberEntity.builder()
//                    .id(originalMemberEntity.getId())
//                    .password(originalMemberEntity.getPassword())
//                    .name("update")
//                    .email("test_update")
//                    .phone("test_update")
//                    .studentNum("20181216")
//                    .type("CLICKIT_STUDENT")
//                    .dormitoryEntity(originalMemberEntity.getDormitoryEntity())
//                    .build();
//
//            log.info("update test given: ✔");
//            // when
//            MemberEntity result = memberRepository.save(memberEntity);
//
//            log.info("update test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
//            assertThat(result.getPassword()).isEqualTo(originalMemberEntity.getPassword());
//            assertThat(result.getName()).isNotEqualTo(originalMemberEntity.getName());
//            assertThat(result.getEmail()).isNotEqualTo(originalMemberEntity.getEmail());
//            assertThat(result.getPhone()).isNotEqualTo(originalMemberEntity.getPhone());
//
//            log.info("update test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("updateMemberForStaff test")
//        void updateMemberForStaffTest(){
//            log.info("updateMemberForStaff test");
//            // given
//            MemberEntity memberEntity = memberRepository.findById("test2");
//
//            entityManager.clear();
//
//            MemberEntity originalMemberEntity = MemberEntity.builder()
//                    .id(memberEntity.getId())
//                    .password(memberEntity.getPassword())
//                    .name(memberEntity.getName())
//                    .email(memberEntity.getEmail())
//                    .phone(memberEntity.getPhone())
//                    .studentNum(memberEntity.getStudentNum())
//                    .type(memberEntity.getType())
//                    .dormitoryEntity(memberEntity.getDormitoryEntity())
//                    .build();
//
//            MemberEntity updateMemberEntity = MemberEntity.builder()
//                    .id(originalMemberEntity.getId())
//                    .password(originalMemberEntity.getPassword())
//                    .name("update")
//                    .email("update")
//                    .phone("update")
//                    .studentNum("20181216")
//                    .type("CLICKIT_STUDENT")
//                    .dormitoryEntity(originalMemberEntity.getDormitoryEntity())
//                    .build();
//
//            log.info("updateMemberForStaff test given: ✔");
//            // when
//            memberRepository.updateMemberForStaff(updateMemberEntity.getId(),
//                    updateMemberEntity.getPassword(),
//                    updateMemberEntity.getName(),
//                    updateMemberEntity.getEmail(),
//                    updateMemberEntity.getPhone(),
//                    updateMemberEntity.getStudentNum(),
//                    updateMemberEntity.getType(),
//                    updateMemberEntity.getDormitoryEntity().getId());
//
//            MemberEntity result = memberRepository.findById(updateMemberEntity.getId());
//
//            log.info("updateMemberForStaff test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
//            assertThat(result.getPassword()).isEqualTo(originalMemberEntity.getPassword());
//            assertThat(result.getName()).isNotEqualTo(originalMemberEntity.getName());
//            assertThat(result.getEmail()).isNotEqualTo(originalMemberEntity.getEmail());
//
//            log.info("updateMemberForStaff test then: ✔");
//        }
//
//        @Test
//        @Order(3)
//        @DisplayName("updatePassword test")
//        void updatePasswordTest(){
//            log.info("updatePassword test");
//            // given
//            MemberEntity originalMemberEntity = memberRepository.findById("test2");
//
//            entityManager.clear();
//
//            String updatePassword = "update_password";
//
//            log.info("updatePassword test given: ✔");
//            // when
//            memberRepository.updatePassword(originalMemberEntity.getId(), updatePassword);
//
//            MemberEntity result = memberRepository.findById(originalMemberEntity.getId());
//
//            entityManager.clear();
//
//            log.info("updatePassword test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getPassword()).isEqualTo(updatePassword);
//            assertThat(result.getPassword()).isNotEqualTo(originalMemberEntity.getPassword());
//
//            log.info("updatePassword test then: ✔");
//        }
//
//        @Test
//        @Order(4)
//        @DisplayName("updateRefreshToken test")
//        void updateRefreshTokenTest(){
//            log.info("updateRefreshToken test");
//            // given
//            MemberEntity originalMemberEntity = memberRepository.findById("test2");
//
//            entityManager.clear();
//
//            String updateRefreshToken = "update_refresh_token";
//
//            log.info("updateRefreshToken test given: ✔");
//            // when
//            memberRepository.updateRefreshToken(originalMemberEntity.getId(), updateRefreshToken);
//
//            MemberEntity result = memberRepository.findById(originalMemberEntity.getId());
//
//            entityManager.clear();
//
//            log.info("updateRefreshToken test when: ✔");
//            // then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
//            assertThat(result.getRefreshToken()).isEqualTo(updateRefreshToken);
//            assertThat(result.getRefreshToken()).isNotEqualTo(originalMemberEntity.getRefreshToken());
//
//            log.info("updateRefreshToken test then: ✔");
//        }
//    }
//
//    @Nested
//    @DisplayName("Delete")
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class Delete{
//        @Test
//        @Order(1)
//        @DisplayName("deleteById test")
//        void deleteByIdTest(){
//            log.info("deleteById test");
//            // given
//            String id = "test2";
//
//            log.info("deleteById test given: ✔");
//            // when
//            memberRepository.deleteById(id);
//
//            MemberEntity result = memberRepository.findById(id);
//
//            entityManager.clear();
//
//            log.info("deleteById test when: ✔");
//            // then
//            assertThat(result).isNull();
//
//            log.info("deleteById test then: ✔");
//        }
//
//        @Test
//        @Order(2)
//        @DisplayName("delete test")
//        void deleteTest(){
//            log.info("delete test");
//            // given
//            String id = "test2";
//
//            log.info("delete test given: ✔");
//            // when
//            memberRepository.delete(memberRepository.findById(id));
//
//            MemberEntity result = memberRepository.findById(id);
//
//            entityManager.clear();
//
//            log.info("delete test when: ✔");
//            // then
//            assertThat(result).isNull();
//
//            log.info("delete test then: ✔");
//        }
//    }
//}
