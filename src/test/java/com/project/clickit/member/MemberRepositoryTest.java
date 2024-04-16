package com.project.clickit.member;

import com.project.clickit.entity.DormitoryEntity;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@DisplayName("Member Repository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("existsById")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ExistsById{
        @Test
        @Order(1)
        @DisplayName("existsById test")
        void existsByIdTest(){
            log.info("existsById test");
            // given
            String id = "test_member_id";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id = " + id + "\n");
            // when
            Boolean result = memberRepository.existsById(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.existsById(id);
                    """);
            // then
            assertThat(result).isTrue();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isTrue();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("existsById test False")
        void existsByIdTestFalse(){
            log.info("existsById test False");
            // given
            String id = "test_member_id_false";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id = " + id + "\n");
            // when
            Boolean result = memberRepository.existsById(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.existsById(id);
                    """);
            // then
            assertThat(result).isFalse();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isFalse();
                    """);
        }
    }

    @Nested
    @DisplayName("Create")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Create{
        @Test
        @Order(1)
        @DisplayName("save test")
        void saveTest(){
            log.info("save test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_1")
                    .build();

            MemberEntity memberEntity = MemberEntity.builder()
                    .id("test")
                    .password("test")
                    .name("test")
                    .email("test")
                    .phone("test")
                    .studentNum("test")
                    .type("CLICKIT_STUDENT")
                    .dormitoryEntity(dormitoryEntity)
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ memberEntity" +
                    "\n\t\t  ┗ id = " + memberEntity.getId() +
                    "\n\t\t  ┗ password = " + memberEntity.getPassword() +
                    "\n\t\t  ┗ name = " + memberEntity.getName() +
                    "\n\t\t  ┗ email = " + memberEntity.getEmail() +
                    "\n\t\t  ┗ phone = " + memberEntity.getPhone() +
                    "\n\t\t  ┗ studentNum = " + memberEntity.getStudentNum() +
                    "\n\t\t  ┗ type = " + memberEntity.getType() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id = " + memberEntity.getDormitoryEntity().getId() + "\n");
            // when
            MemberEntity result = memberRepository.save(memberEntity);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.save(memberEntity);
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(memberEntity.getId());
            assertThat(result.getPassword()).isEqualTo(memberEntity.getPassword());

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result.getId()).isEqualTo(memberEntity.getId());
                    \t  ┗ assertThat(result.getPassword()).isEqualTo(memberEntity.getPassword());
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("saveAll test")
        void saveAllTest(){
            log.info("saveAll test");
            // given
            DormitoryEntity dormitoryEntity = DormitoryEntity.builder()
                    .id("dor_2")
                    .build();

            List<MemberEntity> memberEntityList = List.of(
                    MemberEntity.builder()
                            .id("test1")
                            .password("test1")
                            .name("test1")
                            .email("test1")
                            .phone("test1")
                            .studentNum("test1")
                            .type("CLICKIT_STUDENT")
                            .dormitoryEntity(dormitoryEntity)
                            .build(),
                    MemberEntity.builder()
                            .id("test2")
                            .password("test2")
                            .name("test2")
                            .email("test2")
                            .phone("test2")
                            .studentNum("test2")
                            .type("CLICKIT_STUDENT")
                            .dormitoryEntity(dormitoryEntity)
                            .build()
            );

            log.info("\n\tgiven" +
                    "\n\t  ┗ memberEntityList" +
                    "\n\t\t  ┗ memberEntity1" +
                    "\n\t\t\t  ┗ id = " + memberEntityList.get(0).getId() +
                    "\n\t\t\t  ┗ password = " + memberEntityList.get(0).getPassword() +
                    "\n\t\t\t  ┗ name = " + memberEntityList.get(0).getName() +
                    "\n\t\t\t  ┗ email = " + memberEntityList.get(0).getEmail() +
                    "\n\t\t\t  ┗ phone = " + memberEntityList.get(0).getPhone() +
                    "\n\t\t\t  ┗ studentNum = " + memberEntityList.get(0).getStudentNum() +
                    "\n\t\t\t  ┗ type = " + memberEntityList.get(0).getType() +
                    "\n\t\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t\t  ┗ id = " + memberEntityList.get(0).getDormitoryEntity().getId() +
                    "\n\t\t  ┗ memberEntity2" +
                    "\n\t\t\t  ┗ id = " + memberEntityList.get(1).getId() +
                    "\n\t\t\t  ┗ password = " + memberEntityList.get(1).getPassword() +
                    "\n\t\t\t  ┗ name = " + memberEntityList.get(1).getName() +
                    "\n\t\t\t  ┗ email = " + memberEntityList.get(1).getEmail() +
                    "\n\t\t\t  ┗ phone = " + memberEntityList.get(1).getPhone() +
                    "\n\t\t\t  ┗ studentNum = " + memberEntityList.get(1).getStudentNum() +
                    "\n\t\t\t  ┗ type = " + memberEntityList.get(1).getType() +
                    "\n\t\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t\t  ┗ id = " + memberEntityList.get(1).getDormitoryEntity().getId() + "\n");
            // when
            List<MemberEntity> result = memberRepository.saveAll(memberEntityList);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.saveAll(memberEntityList);
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.size()).isEqualTo(memberEntityList.size());
            assertThat(result.get(0).getId()).isEqualTo(memberEntityList.get(0).getId());
            assertThat(result.get(0).getPassword()).isEqualTo(memberEntityList.get(0).getPassword());
            assertThat(result.get(1).getId()).isEqualTo(memberEntityList.get(1).getId());
            assertThat(result.get(1).getPassword()).isEqualTo(memberEntityList.get(1).getPassword());

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result.size()).isEqualTo(memberEntityList.size());
                    \t  ┗ assertThat(result.get(0).getId()).isEqualTo(memberEntityList.get(0).getId());
                    \t  ┗ assertThat(result.get(0).getPassword()).isEqualTo(memberEntityList.get(0).getPassword());
                    \t  ┗ assertThat(result.get(1).getId()).isEqualTo(memberEntityList.get(1).getId());
                    \t  ┗ assertThat(result.get(1).getPassword()).isEqualTo(memberEntityList.get(1).getPassword());
                    """);
        }
    }

    @Nested
    @DisplayName("Read")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("findAll test")
        void findAllTest(){
            log.info("findAll test");
            // given

            log.info("\n\tgiven\n");
            // when
            List<MemberEntity> result = memberRepository.findAll();

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.findAll();
                    """);
            // then
            assertThat(result).isNotNull();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("findById Test")
        void findByIdTest(){
            log.info("findById Test");
            // given
            String id = "test_member_id";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id = " + id + "\n");
            // when
            MemberEntity result = memberRepository.findById(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.findById(id).orElse(null);
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(id);

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result.getId()).isEqualTo(id);
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("findByMemberName Test")
        void findByMemberNameTest(){
            log.info("findByMemberName Test");
            // given

            // when

            // then
        }

        @Test
        @Order(4)
        @DisplayName("findPasswordByMemberId Test")
        void findPasswordByMemberIdTest(){
            log.info("findPasswordByMemberId Test");
            // given

            // when

            // then
        }

        @Test
        @Order(5)
        @DisplayName("findByDormitoryId Test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId Test");
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
        void updateTest() {
            log.info("update test");
            // given
            MemberEntity originalMemberEntity = memberRepository.findById("test2");

            // findById로 가져온 MemberEntity를 영속성 컨텍스트에 저장
            // save 시 isNew로 판단하여 insert가 아닌 update 쿼리가 실행되도록 함
            // update 과정에서 merge가 실행되는데 이 때 영속성 컨텍스트에 있는 originalMemberEntity도 변경됨
            // 결과적으로 then에서 originalMemberEntity의 값이 변경되어 있음 -> isNotEqualTo에서 오류 발생되는 원인
//            String originalName = originalMemberEntity.getName();
//            String originalEmail = originalMemberEntity.getEmail();
//            String originalPhone = originalMemberEntity.getPhone();

            entityManager.clear();

            MemberEntity memberEntity = MemberEntity.builder()
                    .id(originalMemberEntity.getId())
                    .password(originalMemberEntity.getPassword())
                    .name("update")
                    .email("test_update")
                    .phone("test_update")
                    .studentNum("20181216")
                    .type("CLICKIT_STUDENT")
                    .dormitoryEntity(originalMemberEntity.getDormitoryEntity())
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ originalMemberEntity" +
                    "\n\t\t  ┗ id = " + originalMemberEntity.getId() +
                    "\n\t\t  ┗ password = " + originalMemberEntity.getPassword() +
                    "\n\t\t  ┗ name = " + originalMemberEntity.getName() +
                    "\n\t\t  ┗ email = " + originalMemberEntity.getEmail() +
                    "\n\t\t  ┗ phone = " + originalMemberEntity.getPhone() +
                    "\n\t\t  ┗ studentNum = " + originalMemberEntity.getStudentNum() +
                    "\n\t\t  ┗ type = " + originalMemberEntity.getType() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id = " + originalMemberEntity.getDormitoryEntity().getId() +
                    "\n\t  ┗ memberEntity" +
                    "\n\t\t  ┗ id = " + memberEntity.getId() +
                    "\n\t\t  ┗ password = " + memberEntity.getPassword() +
                    "\n\t\t  ┗ name = " + memberEntity.getName() +
                    "\n\t\t  ┗ email = " + memberEntity.getEmail() +
                    "\n\t\t  ┗ phone = " + memberEntity.getPhone() +
                    "\n\t\t  ┗ studentNum = " + memberEntity.getStudentNum() +
                    "\n\t\t  ┗ type = " + memberEntity.getType() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id = " + memberEntity.getDormitoryEntity().getId() + "\n");
            // when
            MemberEntity result = memberRepository.save(memberEntity);

            log.info("""
                    
                    \twhen
                    \t  ┗ result = memberRepository.save(memberEntity);
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
            assertThat(result.getPassword()).isEqualTo(originalMemberEntity.getPassword());
            assertThat(result.getName()).isNotEqualTo(originalMemberEntity.getName());
            assertThat(result.getEmail()).isNotEqualTo(originalMemberEntity.getEmail());
            assertThat(result.getPhone()).isNotEqualTo(originalMemberEntity.getPhone());

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
                    \t  ┗ assertThat(result.getPassword()).isEqualTo(originalMemberEntity.getPassword());
                    \t  ┗ assertThat(result.getName()).isNotEqualTo(originalMemberEntity.getName());
                    \t  ┗ assertThat(result.getEmail()).isNotEqualTo(originalMemberEntity.getEmail());
                    \t  ┗ assertThat(result.getPhone()).isNotEqualTo(originalMemberEntity.getPhone());
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("updateMemberForStaff test")
        void updateMemberForStaffTest(){
            log.info("updateMemberForStaff test");
            // given
            MemberEntity memberEntity = memberRepository.findById("test2");

            entityManager.clear();

            MemberEntity originalMemberEntity = MemberEntity.builder()
                    .id(memberEntity.getId())
                    .password(memberEntity.getPassword())
                    .name(memberEntity.getName())
                    .email(memberEntity.getEmail())
                    .phone(memberEntity.getPhone())
                    .studentNum(memberEntity.getStudentNum())
                    .type(memberEntity.getType())
                    .dormitoryEntity(memberEntity.getDormitoryEntity())
                    .build();

            MemberEntity updateMemberEntity = MemberEntity.builder()
                    .id(originalMemberEntity.getId())
                    .password(originalMemberEntity.getPassword())
                    .name("update")
                    .email("update")
                    .phone("update")
                    .studentNum("20181216")
                    .type("CLICKIT_STUDENT")
                    .dormitoryEntity(originalMemberEntity.getDormitoryEntity())
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ originalMemberEntity" +
                    "\n\t\t  ┗ id = " + originalMemberEntity.getId() +
                    "\n\t\t  ┗ password = " + originalMemberEntity.getPassword() +
                    "\n\t\t  ┗ name = " + originalMemberEntity.getName() +
                    "\n\t\t  ┗ email = " + originalMemberEntity.getEmail() +
                    "\n\t\t  ┗ phone = " + originalMemberEntity.getPhone() +
                    "\n\t\t  ┗ studentNum = " + originalMemberEntity.getStudentNum() +
                    "\n\t\t  ┗ type = " + originalMemberEntity.getType() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id = " + originalMemberEntity.getDormitoryEntity().getId() +
                    "\n\t  ┗ updateMemberEntity" +
                    "\n\t\t  ┗ id = " + updateMemberEntity.getId() +
                    "\n\t\t  ┗ password = " + updateMemberEntity.getPassword() +
                    "\n\t\t  ┗ name = " + updateMemberEntity.getName() +
                    "\n\t\t  ┗ email = " + updateMemberEntity.getEmail() +
                    "\n\t\t  ┗ phone = " + updateMemberEntity.getPhone() +
                    "\n\t\t  ┗ studentNum = " + updateMemberEntity.getStudentNum() +
                    "\n\t\t  ┗ type = " + updateMemberEntity.getType() +
                    "\n\t\t  ┗ dormitoryEntity" +
                    "\n\t\t\t  ┗ id = " + updateMemberEntity.getDormitoryEntity().getId() + "\n");
            // when
            memberRepository.updateMemberForStaff(updateMemberEntity.getId(),
                    updateMemberEntity.getPassword(),
                    updateMemberEntity.getName(),
                    updateMemberEntity.getEmail(),
                    updateMemberEntity.getPhone(),
                    updateMemberEntity.getStudentNum(),
                    updateMemberEntity.getType(),
                    updateMemberEntity.getDormitoryEntity().getId());

            MemberEntity result = memberRepository.findById(updateMemberEntity.getId());

            log.info("""
                    
                    \twhen
                    \t  ┗ memberRepository.updateMemberForStaff(updateMemberEntity.getId(),
                    \t\t  updateMemberEntity.getPassword(),
                    \t\t  updateMemberEntity.getName(),
                    \t\t  updateMemberEntity.getEmail(),
                    \t\t  updateMemberEntity.getPhone(),
                    \t\t  updateMemberEntity.getStudentNum(),
                    \t\t  updateMemberEntity.getType(),
                    \t\t  updateMemberEntity.getDormitoryEntity().getId());
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
            assertThat(result.getPassword()).isEqualTo(originalMemberEntity.getPassword());
            assertThat(result.getName()).isNotEqualTo(originalMemberEntity.getName());
            assertThat(result.getEmail()).isNotEqualTo(originalMemberEntity.getEmail());

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
                    \t  ┗ assertThat(result.getPassword()).isEqualTo(originalMemberEntity.getPassword());
                    \t  ┗ assertThat(result.getName()).isNotEqualTo(originalMemberEntity.getName());
                    \t  ┗ assertThat(result.getEmail()).isNotEqualTo(originalMemberEntity.getEmail());
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("updatePassword test")
        void updatePasswordTest(){
            log.info("updatePassword test");
            // given
            MemberEntity originalMemberEntity = memberRepository.findById("test2");

            entityManager.clear();

            String updatePassword = "update_password";

            log.info("\n\tgiven" +
                    "\n\t  ┗ originalMemberEntity" +
                    "\n\t\t  ┗ id = " + originalMemberEntity.getId() +
                    "\n\t\t  ┗ password = " + originalMemberEntity.getPassword() +
                    "\n\t  ┗ updatePassword = " + updatePassword + "\n");
            // when
            memberRepository.updatePassword(originalMemberEntity.getId(), updatePassword);

            String result = memberRepository.findPasswordByMemberId(originalMemberEntity.getId());

            entityManager.clear();

            log.info("""
                    
                    \twhen
                    \t  ┗ memberRepository.updatePassword(originalMemberEntity.getId(), updatePassword);
                    \t  ┗ result = memberRepository.findPasswordByMemberId(originalMemberEntity.getId());
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(updatePassword);
            assertThat(result).isNotEqualTo(originalMemberEntity.getPassword());

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result).isEqualTo(updatePassword);
                    \t  ┗ assertThat(result).isNotEqualTo(originalMemberEntity.getPassword());
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("updateRefreshToken test")
        void updateRefreshTokenTest(){
            log.info("updateRefreshToken test");
            // given
            MemberEntity originalMemberEntity = memberRepository.findById("test2");

            entityManager.clear();

            String updateRefreshToken = "update_refresh_token";

            log.info("\n\tgiven" +
                    "\n\t  ┗ originalMemberEntity" +
                    "\n\t\t  ┗ id = " + originalMemberEntity.getId() +
                    "\n\t\t  ┗ refreshToken = " + originalMemberEntity.getRefreshToken() +
                    "\n\t  ┗ updateRefreshToken = " + updateRefreshToken + "\n");
            // when
            memberRepository.updateRefreshToken(originalMemberEntity.getId(), updateRefreshToken);

            MemberEntity result = memberRepository.findById(originalMemberEntity.getId());

            entityManager.clear();

            log.info("""
                    
                    \twhen
                    \t  ┗ memberRepository.updateRefreshToken(originalMemberEntity.getId(), updateRefreshToken);
                    \t  ┗ result = memberRepository.findById(originalMemberEntity.getId());
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
            assertThat(result.getRefreshToken()).isEqualTo(updateRefreshToken);
            assertThat(result.getRefreshToken()).isNotEqualTo(originalMemberEntity.getRefreshToken());

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNotNull();
                    \t  ┗ assertThat(result.getId()).isEqualTo(originalMemberEntity.getId());
                    \t  ┗ assertThat(result.getRefreshToken()).isEqualTo(updateRefreshToken);
                    \t  ┗ assertThat(result.getRefreshToken()).isNotEqualTo(originalMemberEntity.getRefreshToken());
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
            String id = "test2";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id = " + id + "\n");
            // when
            memberRepository.deleteById(id);

            MemberEntity result = memberRepository.findById(id);

            entityManager.clear();

            log.info("""
                    
                    \twhen
                    \t  ┗ memberRepository.deleteById(id);
                    \t  ┗ result = memberRepository.findById(id);
                    """);
            // then
            assertThat(result).isNull();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNull();
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("delete test")
        void deleteTest(){
            log.info("delete test");
            // given
            String id = "test2";

            log.info("\n\tgiven" +
                    "\n\t  ┗ id = " + id + "\n");
            // when
            memberRepository.delete(memberRepository.findById(id));

            MemberEntity result = memberRepository.findById(id);

            entityManager.clear();

            log.info("""
                    
                    \twhen
                    \t  ┗ memberRepository.delete(memberRepository.findById(id));
                    \t  ┗ result = memberRepository.findById(id);
                    """);
            // then
            assertThat(result).isNull();

            log.info("""
                    
                    \tthen
                    \t  ┗ assertThat(result).isNull();
                    """);
        }
    }
}
