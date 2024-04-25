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

            log.info("isExist Test given - ✔");
            // when
            Boolean result = noticeRepository.existsByNum(num);

            log.info("isExist Test when - ✔");
            // then
            assertThat(result).isTrue();

            log.info("isExist Test then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("isExist Test - Not Exist")
        public void isExistTestNotExist(){
            log.info("isExist Test - Not Exist");
            // given
            Integer num = 100;

            log.info("isExist Test - Not Exist | given - ✔");
            // when
            Boolean result = noticeRepository.existsByNum(num);

            log.info("isExist Test - Not Exist | when - ✔");
            // then
            assertThat(result).isFalse();

            log.info("isExist Test - Not Exist | then - ✔");
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

            log.info("Create Notice Test given - ✔");
            // when
            NoticeEntity result = noticeRepository.save(noticeEntity);

            log.info("Create Notice Test when - ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(noticeEntity.getTitle());
            assertThat(result.getContent()).isEqualTo(noticeEntity.getContent());
            assertThat(result.getMemberEntity().getId()).isEqualTo(noticeEntity.getMemberEntity().getId());

            log.info("Create Notice Test then - ✔");
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

            log.info("findAll Test given - ✔");
            // when
            Page<NoticeEntity> result = noticeRepository.findAll(Pageable.unpaged());

            log.info("findAll Test when - ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findAll Test then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findByNum Test")
        void findByNumTest(){
            log.info("findByNum Test");
            // given
            Integer num = 1;

            log.info("findByNum Test given - ✔");
            // when
            NoticeEntity result = noticeRepository.findByNum(num);

            log.info("findByNum Test when - ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findByNum Test then - ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByWriterId Test")
        void findByWriterIdTest(){
            log.info("findByWriterId Test");
            // given
            String memberId = "test_member_id";

            log.info("findByWriterId Test given - ✔");
            // when
            Page<NoticeEntity> result = noticeRepository.findByWriterId(memberId, Pageable.unpaged());

            log.info("findByWriterId Test when - ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findByWriterId Test then - ✔");
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

            log.info("Notice Update Test given - ✔");
            // when
            NoticeEntity result = noticeRepository.save(updateNotice);

            log.info("Notice Update Test when - ✔");
            // then
            assertThat(result).isNotNull();
            assertThat(result.getNum()).isEqualTo(originalNotice.getNum());
            assertThat(result.getTitle()).isEqualTo(originalNotice.getTitle());
            assertThat(result.getContent()).isNotEqualTo(originalNotice.getContent());
            assertThat(result.getMemberEntity().getId()).isEqualTo(originalNotice.getMemberEntity().getId());

            log.info("Notice Update Test then - ✔");
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

            log.info("Delete Test given - ✔");
            // when
            noticeRepository.deleteById(num);

            log.info("Delete Test when - ✔");
            // then
            assertThat(noticeRepository.existsByNum(num)).isFalse();

            log.info("Delete Test then - ✔");
        }
    }
}
