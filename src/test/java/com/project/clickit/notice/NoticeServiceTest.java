package com.project.clickit.notice;

import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.NoticeDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.entity.NoticeEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.notice.NoticeNotFoundException;
import com.project.clickit.repository.NoticeRepository;
import com.project.clickit.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@Slf4j
@DisplayName("Service - Notice")
@ExtendWith({MockitoExtension.class})
public class NoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Nested
    @DisplayName("isExist")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IsExist{
        @Test
        @Order(1)
        @DisplayName("isExist Test(존재)")
        void isExistTestTrue(){
            log.info("isExist Test(존재)");
            // given
            NoticeDTO noticeDTO = NoticeDTO.builder().num(1).build();
            given(noticeRepository.existsByNum(anyInt())).willReturn(true);

            log.info("isExist Test(존재) given - ✔");
            //when
            Boolean result = noticeService.isExist(noticeDTO.getNum());

            log.info("isExist Test(존재) when - ✔");
            //then
            assertThat(result).isTrue();

            log.info("isExist Test(존재) then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("isExist Test(미존재)")
        void isExistTestFalse(){
            log.info("isExist Test(미존재)");
            // given
            NoticeDTO noticeDTO = NoticeDTO.builder().num(1).build();
            given(noticeRepository.existsByNum(anyInt())).willReturn(false);

            log.info("isExist Test(미존재) | given - ✔");
            //when
            Boolean result = noticeService.isExist(noticeDTO.getNum());

            log.info("isExist Test(미존재) | when - ✔");
            //then
            assertThat(result).isFalse();

            log.info("isExist Test(미존재) | then - ✔");
        }
    }

    @Nested
    @DisplayName("create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Create{
        @Test
        @Order(1)
        @DisplayName("createNotice Test")
        void createNoticeTest(){
            log.info("createNotice Test");
            // given
            MemberDTO memberDTO = MemberDTO.builder().id("test3").build();

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .num(10)
                    .title("title")
                    .content("content")
                    .memberDTO(memberDTO)
                    .build();

            given(noticeRepository.existsByNum(anyInt())).willReturn(false);

            log.info("createNotice Test given - ✔");
            // when
            noticeService.createNotice(noticeDTO);

            log.info("createNotice Test when - ✔");
            // then
            assertThatCode(()->noticeService.createNotice(noticeDTO)).doesNotThrowAnyException();

            log.info("createNotice Test then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("createNotice Test - DuplicatedIdException")
        void createNoticeTestDuplicatedIdException(){
            log.info("createNotice Test - DuplicatedIdException");
            // given
            MemberDTO memberDTO = MemberDTO.builder().id("test3").build();

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .num(1)
                    .title("title")
                    .content("content")
                    .memberDTO(memberDTO)
                    .build();

            given(noticeRepository.existsByNum(anyInt())).willReturn(true);

            log.info("createNotice Test - DuplicatedIdException | given - ✔");
            // when
            Throwable result = catchThrowable(()->noticeService.createNotice(noticeDTO));

            log.info("createNotice Test - DuplicatedIdException | when - ✔");
            // then
            assertThat(result).isInstanceOf(DuplicatedIdException.class);

            log.info("createNotice Test - DuplicatedIdException | then - ✔");
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest(){
            log.info("getAll Test");
            // given
            Page<NoticeEntity> noticeEntityPage = Page.empty();

            given(noticeRepository.findAll(any(Pageable.class))).willReturn(noticeEntityPage);

            log.info("getAll Test given - ✔");
            // when
            Page<NoticeDTO> result = noticeService.getAll(Pageable.unpaged());

            log.info("getAll Test when - ✔");
            // then
            assertThat(result).isNotNull();

            log.info("getAll Test then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findByNoticeNum Test")
        void findByNoticeNumTest(){
            log.info("findByNoticeNum Test");
            // given
            Integer num = 1;

            NoticeEntity noticeEntity = NoticeEntity.builder()
                    .num(num)
                    .memberEntity(MemberEntity.builder().id("test3").build())
                    .build();

            given(noticeRepository.findByNum(anyInt())).willReturn(noticeEntity);
            given(noticeService.isExist(anyInt())).willReturn(true);

            log.info("findByNoticeNum Test given - ✔");
            // when
            NoticeDTO result = noticeService.findByNoticeNum(num);

            log.info("findByNoticeNum Test when - ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findByNoticeNum Test then - ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByNoticeNum - NoticeNotFoundException")
        void findByNoticeNumTestNotFound(){
            log.info("NoticeService - findByNoticeNum_NotFound");
            // given
            Integer num = 11;

            given(noticeService.isExist(anyInt())).willReturn(false);

            log.info("NoticeService - findByNoticeNum_NotFound | given - ✔");
            // when
            Throwable result = catchThrowable(()->noticeService.findByNoticeNum(num));

            log.info("NoticeService - findByNoticeNum_NotFound | when - ✔");
            // then
            assertThat(result).isInstanceOf(NoticeNotFoundException.class);

            log.info("NoticeService - findByNoticeNum_NotFound | then - ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findNoticeByWriterId Test")
        void findNoticeByWriterIdTest(){
            log.info("findNoticeByWriterId Test");
            // given
            String memberId = "test3";

            Page<NoticeEntity> noticeEntityPage = Page.empty();
            given(noticeRepository.findByWriterId(anyString(), any(Pageable.class))).willReturn(noticeEntityPage);

            log.info("findNoticeByWriterId Test given - ✔");
            // when
            Page<NoticeDTO> result = noticeService.findNoticeByWriterId(memberId, Pageable.unpaged());

            log.info("findNoticeByWriterId Test when - ✔");
            // then
            assertThat(result).isNotNull();

            log.info("findNoticeByWriterId Test then - ✔");
        }
    }

    @Nested
    @DisplayName("update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Update{
        @Test
        @Order(1)
        @DisplayName("updateNotice Test")
        void updateNotice(){
            log.info("updateNotice Test");
            // given
            MemberDTO memberDTO = MemberDTO.builder().id("test3").build();

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .num(1)
                    .title("title")
                    .content("content")
                    .memberDTO(memberDTO)
                    .build();

            given(noticeService.isExist(anyInt())).willReturn(true);

            log.info("updateNotice Test given - ✔");
            // when
            noticeService.updateNotice(noticeDTO);

            log.info("updateNotice Test when - ✔");
            // then
            assertThatCode(()->noticeService.updateNotice(noticeDTO)).doesNotThrowAnyException();

            log.info("updateNotice Test then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("updateNotice Test - NoticeNotFoundException")
        void updateNotice_NotFound(){
            log.info("NoticeService Test - NoticeNotFoundException");

            // given
            NoticeDTO noticeDTO = NoticeDTO.builder().num(11).build();

            given(noticeService.isExist(anyInt())).willReturn(false);

            log.info("NoticeService Test - NoticeNotFoundException | given - ✔");
            // when
            Throwable result = catchThrowable(()->noticeService.updateNotice(noticeDTO));

            log.info("NoticeService Test - NoticeNotFoundException | when - ✔");
            // then
            assertThat(result).isInstanceOf(NoticeNotFoundException.class);

            log.info("NoticeService Test - NoticeNotFoundException | then - ✔");
        }
    }

    @Nested
    @DisplayName("delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Delete{
        @Test
        @Order(1)
        @DisplayName("deleteNotice Test")
        void deleteNotice(){
            log.info("deleteNotice Test");
            // given
            Integer num = 1;

            given(noticeService.isExist(anyInt())).willReturn(true);

            log.info("deleteNotice Test given - ✔");
            // when
            noticeService.deleteNotice(num);

            log.info("deleteNotice Test when - ✔");
            // then
            assertThatCode(()->noticeService.deleteNotice(num)).doesNotThrowAnyException();

            log.info("deleteNotice Test then - ✔");
        }

        @Test
        @Order(2)
        @DisplayName("deleteNotice Test - NoticeNotFoundException")
        void deleteNotice_NotFound(){
            log.info("NoticeService Test - deleteNotice_NotFound");
            // given
            Integer num = 11;

            given(noticeService.isExist(anyInt())).willReturn(false);

            log.info("NoticeService Test - deleteNotice_NotFound | given - ✔");
            // when
            Throwable result = catchThrowable(()->noticeService.deleteNotice(num));

            log.info("NoticeService Test - deleteNotice_NotFound | when - ✔");
            // then
            assertThat(result).isInstanceOf(NoticeNotFoundException.class);

            log.info("NoticeService Test - deleteNotice_NotFound | then - ✔");
        }
    }

}
