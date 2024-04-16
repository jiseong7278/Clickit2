package com.project.clickit.notice;

import com.project.clickit.entity.MemberEntity;
import com.project.clickit.entity.NoticeEntity;
import com.project.clickit.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@DisplayName("Notice Repository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;

    @Nested
    @DisplayName("existByNum")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ExistByNum {
        @Test
        @Order(1)
        @DisplayName("isExist Test")
        public void isExistTest(){
            log.info("isExist Test");
            // given
            Integer num = 1;

            log.info("\n\tgiven" +
                    "\n\t  ┗ num : " + num + "\n");
            // when
            Boolean result = noticeRepository.existsByNum(num);

            log.info("""

                    \twhen
                    \t  ┗ Boolean result = noticeRepository.existsByNum(num)
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
        @DisplayName("isExist Test - Not Exist")
        public void isExistTestNotExist(){
            log.info("isExist Test - Not Exist");
            // given
            Integer num = 100;

            log.info("\n\tgiven" +
                    "\n\t  ┗ num : " + num + "\n");
            // when
            Boolean result = noticeRepository.existsByNum(num);

            log.info("""

                    \twhen
                    \t  ┗ Boolean result = noticeRepository.existsByNum(num)
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
        @DisplayName("Create Notice Test")
        void createTest(){
            log.info("Create Notice Test");
            // given
            MemberEntity memberEntity = MemberEntity.builder().id("test3").build();

            NoticeEntity noticeEntity = NoticeEntity.builder()
                    .title("test")
                    .content("content")
                    .date(LocalDateTime.now())
                    .memberEntity(memberEntity)
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ NoticeEntity" +
                    "\n\t\t  ┗ title: \"" + noticeEntity.getTitle()+ "\""+
                    "\n\t\t  ┗ content: \"" + noticeEntity.getContent()+ "\""+
                    "\n\t\t  ┗ date: \"" + noticeEntity.getDate()+ "\""+
                    "\n\t\t  ┗ memberEntity" +
                    "\n\t\t\t  ┗ id: " + memberEntity.getId() + "\n");
            // when
            NoticeEntity result = noticeRepository.save(noticeEntity);

            log.info("""

                    \twhen
                    \t  ┗ noticeRepository.save(noticeEntity)
                    """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(noticeEntity.getTitle());
            assertThat(result.getContent()).isEqualTo(noticeEntity.getContent());
            assertThat(result.getMemberEntity().getId()).isEqualTo(noticeEntity.getMemberEntity().getId());

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    \t  ┗ assertThat(result.getTitle()).isEqualTo(noticeEntity.getTitle())
                    \t  ┗ assertThat(result.getContent()).isEqualTo(noticeEntity.getContent())
                    \t  ┗ assertThat(result.getMemberEntity().getId()).isEqualTo(noticeEntity.getMemberEntity().getId())
                    """);
        }
    }

    @Nested
    @DisplayName("Read")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("findAll Test")
        void findAll(){
            log.info("findAll Test");
            // given


            log.info("""

                    \tgiven
                    """);
            // when
            Page<NoticeEntity> result = noticeRepository.findAll(Pageable.unpaged());

            log.info("""

                    \twhen
                    \t  ┗ Page<NoticeEntity> result = noticeRepository.findAll(Pageable.unpaged())
                    """);
            // then
            assertThat(result).isNotNull();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("findByNum Test")
        void findByNumTest(){
            log.info("findByNum Test");
            // given
            Integer num = 1;

            log.info("\n\tgiven" +
                    "\n\t  ┗ num : " + num + "\n");
            // when
            NoticeEntity result = noticeRepository.findByNum(num);

            log.info("""

                    \twhen
                    \t  ┗ NoticeEntity result = noticeRepository.findByNum(num)
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
        @DisplayName("findByWriterId Test")
        void findByWriterIdTest(){
            log.info("findByWriterId Test");
            // given
            String memberId = "test_member_id";

            log.info("\n\tgiven" +
                    "\n\t  ┗ memberId : " + memberId + "\n");
            // when
            Page<NoticeEntity> result = noticeRepository.findByWriterId(memberId, Pageable.unpaged());

            log.info("""

                    \twhen
                    \t  ┗ Page<NoticeEntity> result = noticeRepository.findByWriterId(memberId, Pageable.unpaged())
                    """);
            // then
            assertThat(result).isNotNull();

            log.info("""

                    \tthen
                    \t  ┗ assertThat(result).isNotNull()
                    """);
        }
    }

    @Nested
    @DisplayName("Update")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Update{
        @Test
        @DisplayName("Update")
        void updateTest(){
            log.info("Notice Update Test");
            // given
            NoticeEntity originalNotice = noticeRepository.findByNum(1);

            NoticeEntity updateNotice = NoticeEntity.builder()
                    .num(originalNotice.getNum())
                    .title(originalNotice.getTitle())
                    .content("update content")
                    .date(originalNotice.getDate())
                    .memberEntity(originalNotice.getMemberEntity())
                    .build();

            log.info("\n\tgiven" +
                    "\n\t  ┗ originalNotice" +
                    "\n\t\t  ┗ num: " + originalNotice.getNum()+
                    "\n\t\t  ┗ title: " + originalNotice.getTitle()+
                    "\n\t\t  ┗ content: " + originalNotice.getContent()+
                    "\n\t\t  ┗ date: " + originalNotice.getDate()+
                    "\n\t\t  ┗ memberEntity" +
                    "\n\t\t\t  ┗ id: " + originalNotice.getMemberEntity().getId() +
                    "\n\t  ┗ updateNotice" +
                    "\n\t\t  ┗ num: " + updateNotice.getNum()+
                    "\n\t\t  ┗ title: " + updateNotice.getTitle()+
                    "\n\t\t  ┗ content: " + updateNotice.getContent()+
                    "\n\t\t  ┗ date: " + updateNotice.getDate()+
                    "\n\t\t  ┗ memberEntity" +
                    "\n\t\t\t  ┗ id: " + updateNotice.getMemberEntity().getId() + "\n");
            // when
            NoticeEntity result = noticeRepository.save(updateNotice);

            log.info("""

                \twhen
                \t  ┗ noticeRepository.save(updateNotice)
                """);
            // then
            assertThat(result).isNotNull();
            assertThat(result.getNum()).isEqualTo(originalNotice.getNum());
            assertThat(result.getTitle()).isEqualTo(originalNotice.getTitle());
            assertThat(result.getContent()).isNotEqualTo(originalNotice.getContent());
            assertThat(result.getMemberEntity().getId()).isEqualTo(originalNotice.getMemberEntity().getId());

            log.info("""

                \tthen
                \t  ┗ assertThat(result).isNotNull()
                \t  ┗ assertThat(result.getNum()).isEqualTo(originalNotice.getNum())
                \t  ┗ assertThat(result.getTitle()).isEqualTo(originalNotice.getTitle())
                \t  ┗ assertThat(result.getContent()).isNotEqualTo(updateNotice.getContent())
                \t  ┗ assertThat(result.getMemberEntity().getId()).isEqualTo(originalNotice.getMemberEntity().getId())
                """);
        }
    }

    @Nested
    @DisplayName("Delete")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Delete{
        @Test
        @Order(1)
        @DisplayName("Delete Test")
        void deleteTest(){
            log.info("Delete Test");
            // given
            Integer num = 1;

            log.info("\n\tgiven" +
                    "\n\t  ┗ num : " + num + "\n");
            // when
            noticeRepository.deleteById(num);

            log.info("""

                \twhen
                \t  ┗ noticeRepository.deleteById(num)
                """);
            // then
            assertThat(noticeRepository.existsByNum(num)).isFalse();

            log.info("""

                \tthen
                \t  ┗ assertThat(noticeRepository.existsByNum(num)).isFalse()
                """);
        }
    }
}
