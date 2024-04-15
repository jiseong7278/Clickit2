package com.project.clickit.notice;

import com.project.clickit.entity.MemberEntity;
import com.project.clickit.entity.NoticeEntity;
import com.project.clickit.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Notice Repository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;

    @Nested
    @DisplayName("existByNum")
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

            log.info("\n\twhen" +
                    "\n\t  ┗ Boolean result = noticeRepository.existsByNum(num)\n");
            // then
            assertThat(result).isTrue();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isTrue()\n");
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

            log.info("\n\twhen" +
                    "\n\t  ┗ Boolean result = noticeRepository.existsByNum(num)\n");
            // then
            assertThat(result).isFalse();

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isFalse()\n");
        }
    }

    @Nested
    @DisplayName("Create")
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

            log.info("\n\twhen" +
                    "\n\t  ┗ noticeRepository.save(noticeEntity)\n");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(noticeEntity.getTitle());
            assertThat(result.getContent()).isEqualTo(noticeEntity.getContent());
            assertThat(result.getMemberEntity().getId()).isEqualTo(noticeEntity.getMemberEntity().getId());

            log.info("\n\tthen" +
                    "\n\t  ┗ assertThat(result).isNotNull()" +
                    "\n\t  ┗ assertThat(result.getTitle()).isEqualTo(noticeEntity.getTitle())" +
                    "\n\t  ┗ assertThat(result.getContent()).isEqualTo(noticeEntity.getContent())" +
                    "\n\t  ┗ assertThat(result.getMemberEntity().getId()).isEqualTo(noticeEntity.getMemberEntity().getId())\n");
        }
    }
}
