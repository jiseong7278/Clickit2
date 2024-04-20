package com.project.clickit.notice;

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
        @DisplayName("isExist - True(존재)")
        void isExist_True(){
            log.info("NoticeService - isExist");
            // given
            log.info("\n\tgiven\n\t  ┗ noticeDTO.builder().num(1).build()\n\t  ┗ noticeRepository.existsByNum(noticeDTO.getNum())\n");
            NoticeDTO noticeDTO = NoticeDTO.builder().num(1).build();
            given(noticeRepository.existsByNum(noticeDTO.getNum())).willReturn(true);

            //when
            log.info("\n\twhen\n\t  ┗ Boolean result = noticeService.isExist(noticeDTO.getNum())\n");
            Boolean result = noticeService.isExist(noticeDTO.getNum());

            //then
            log.info("\n\tthen\n\t  ┗ assertThat(result).isTrue()");
            assertThat(result).isTrue();
        }

        @Test
        @Order(2)
        @DisplayName("isExist - False(미존재)")
        void isExist_False(){
            log.info("NoticeService - isExist");
            // given
            log.info("\n\tgiven\n\t  ┗ noticeDTO.builder().num(1).build()\n\t  ┗ noticeRepository.existsByNum(noticeDTO.getNum())\n");
            NoticeDTO noticeDTO = NoticeDTO.builder().num(1).build();
            given(noticeRepository.existsByNum(noticeDTO.getNum())).willReturn(false);

            //when
            log.info("\n\twhen\n\t  ┗ Boolean result = noticeService.isExist(noticeDTO.getNum())\n");
            Boolean result = noticeService.isExist(noticeDTO.getNum());

            //then
            log.info("\n\tthen\n\t  ┗ assertThat(result).isFalse()");
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("createNotice")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Create{
        @Test
        @Order(1)
        @DisplayName("createNotice")
        void createNotice(){
            log.info("NoticeService - createNotice");
            // given
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .num(10)
                    .title("title")
                    .content("content")
                    .memberId("test3")
                    .build();

            given(noticeRepository.existsByNum(noticeDTO.getNum())).willReturn(false);

            log.info("\n\tgiven" +
                    "\n\t  ┗ noticeDTO" +
                    "\n\t\t  ┗ num: "+noticeDTO.getNum()+
                    "\n\t\t  ┗ title: \""+noticeDTO.getTitle()+"\"" +
                    "\n\t\t  ┗ content: \""+noticeDTO.getContent()+
                    "\n\t\t  ┗ memberId: \""+noticeDTO.getMemberId()+"\""+
                    "\n\t  ┗ noticeRepository.existsByNum(noticeDTO.getNum()).willReturn(false)\n");

            // when
            noticeService.createNotice(noticeDTO);

            log.info("\n\twhen\n\t  ┗ noticeService.createNotice(noticeDTO)\n");

            // then
            assertThatCode(()->noticeService.createNotice(noticeDTO)).doesNotThrowAnyException();

            log.info("\n\tthen\n\t  ┗ assertThatCode(()->noticeService.createNotice(noticeDTO)).doesNotThrowAnyException()\n");
        }

        @Test
        @Order(2)
        @DisplayName("createNotice - DuplicatedIdException")
        void createNotice_DuplicatedIdException(){
            log.info("NoticeService - createNotice");

            // given
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .num(1)
                    .title("title")
                    .content("content")
                    .memberId("test3")
                    .build();

            given(noticeRepository.existsByNum(noticeDTO.getNum())).willReturn(true);

            log.info("\n\tgiven" +
                    "\n\t  ┗ noticeDTO" +
                    "\n\t\t  ┗ num: "+noticeDTO.getNum()+
                    "\n\t\t  ┗ title: \""+noticeDTO.getTitle()+"\"" +
                    "\n\t\t  ┗ content: \""+noticeDTO.getContent()+
                    "\n\t\t  ┗ memberId: \""+noticeDTO.getMemberId()+"\""+
                    "\n\t  ┗ noticeRepository.existsByNum(noticeDTO.getNum()).willReturn(true)\n");

            // when
            log.info("\n\twhen");

            // then
            assertThatCode(()->noticeService.createNotice(noticeDTO)).isInstanceOf(DuplicatedIdException.class);

            log.info("""

                    \tthen
                    \t  ┗ assertThatCode(()->noticeService.createNotice(noticeDTO)).isInstanceOf(DuplicatedIdException.class)
                    """);
        }
    }

    @Nested
    @DisplayName("getNotice")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Read{
        @Test
        @Order(1)
        @DisplayName("getAll")
        void getAll(){
            log.info("NoticeService - getAll");

            // given
            Page<NoticeEntity> noticeEntityPage = Page.empty();

            given(noticeRepository.findAll(Pageable.unpaged())).willReturn(noticeEntityPage);

            log.info("""

                    \tgiven
                    \t  ┗ Page<NoticeEntity> noticeEntityPage = Page.empty()
                    \t  ┗ noticeRepository.findAll(Pageable.unpaged()).willReturn(noticeEntityPage)
                    """);

            // when
            Page<NoticeDTO> result = noticeService.getAll(Pageable.unpaged());

            log.info("\n\twhen\n\t  ┗ Page<NoticeDTO> result = noticeService.getAll(Pageable.unpaged())\n");

            // then
            assertThat(result).isNotNull();

            log.info("\n\tthen\n\t  ┗ assertThat(result).isNotNull()\n");
        }

        @Test
        @Order(2)
        @DisplayName("findByNoticeNum")
        void findByNoticeNum(){
            log.info("NoticeService - findByNoticeNum");

            // given
            Integer num = 1;

            NoticeEntity noticeEntity = NoticeEntity.builder()
                    .num(num)
                    .memberEntity(MemberEntity.builder().id("test3").build())
                    .build();

            given(noticeRepository.findByNum(num)).willReturn(noticeEntity);
            given(noticeService.isExist(num)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┗ Integer num = 1
                    \t  ┗ NoticeEntity noticeEntity
                    \t\t  ┗ num: {}
                    \t\t  ┗ memberEntity
                    \t\t\t  ┗ id: {}
                    \t  ┗ noticeRepository.findByNum(num).willReturn(noticeEntity)
                    \t  ┗ noticeService.isExist(num).willReturn(true)
                    """, num, noticeEntity.getMemberEntity().getId());
            // when

            NoticeDTO result = noticeService.findByNoticeNum(num);

            log.info("""

                    \twhen
                    \t  ┗ NoticeDTO result = noticeService.findByNoticeNum(num)
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
        @DisplayName("findByNoticeNum - NoticeNotFoundException")
        void findByNoticeNum_NotFound(){
            log.info("NoticeService - findByNoticeNum_NotFound");

            Integer num = 11;

            given(noticeService.isExist(num)).willReturn(false);

            log.info("""

                    \tgiven
                    \t  ┗ Integer num = 11
                    \t  ┗ noticeService.isExist(num).willReturn(false)
                    """);
            // when

            Throwable result = catchThrowable(()->noticeService.findByNoticeNum(num));

            log.info("\n\twhen\n\t  ┗ Throwable result = catchThrowable(()->noticeService.findByNoticeNum(num))\n");
            // then

            assertThat(result).isInstanceOf(NoticeNotFoundException.class);

            log.info("\n\tthen\n\t  ┗ assertThat(result).isInstanceOf(NoticeNotFoundException.class)\n");
        }

        @Test
        @Order(4)
        @DisplayName("findNoticeByWriterId")
        void findNoticeByWriterId(){
            log.info("NoticeService - findNoticeByWriterId");

            // given
            String memberId = "test3";

            Page<NoticeEntity> noticeEntityPage = Page.empty();
            given(noticeRepository.findByWriterId(memberId, Pageable.unpaged())).willReturn(noticeEntityPage);

            log.info("""
                    
                    \tgiven
                    \t  ┗ String memberId = "test3"
                    \t  ┗ Page<NoticeEntity> noticeEntityPage = Page.empty()
                    \t  ┗ noticeRepository.findByWriterId(memberId, Pageable.unpaged()).willReturn(noticeEntityPage)
                    """);
            // when
            Page<NoticeDTO> result = noticeService.findNoticeByWriterId(memberId, Pageable.unpaged());

            log.info("\n\twhen\n\t  ┗ Page<NoticeDTO> result = noticeService.findNoticeByWriterId(memberId, Pageable.unpaged())\n");
            // then

            assertThat(result).isNotNull();

            log.info("\n\tthen\n\t  ┗ assertThat(result).isNotNull()\n");
        }
    }

    @Nested
    @DisplayName("updateNotice")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Update{
        @Test
        @Order(1)
        @DisplayName("updateNotice")
        void updateNotice(){
            log.info("NoticeService - updateNotice");

            // given

            MemberEntity memberEntity = MemberEntity.builder().id("test3").build();

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .num(1)
                    .title("title")
                    .content("content")
                    .memberId(memberEntity.getId())
                    .build();

            given(noticeService.isExist(noticeDTO.getNum())).willReturn(true);

            log.info("\tgiven" +
                    "\n\t  ┗ NoticeDTO noticeDTO" +
                    "\n\t\t  ┗ num: "+noticeDTO.getNum()+
                    "\n\t\t  ┗ title: \""+noticeDTO.getTitle()+
                    "\"\n\t\t  ┗ content: \""+noticeDTO.getContent()+
                    "\"\n\t\t  ┗ memberId: \""+noticeDTO.getMemberId()+
                    "\"\n\t  ┗ noticeService.isExist(noticeDTO.getNum()).willReturn(true)\n");
            // when

            noticeService.updateNotice(noticeDTO);

            log.info("\n\twhen\n\t  ┗ noticeService.updateNotice(noticeDTO)\n");
            // then
            assertThatCode(()->noticeService.updateNotice(noticeDTO)).doesNotThrowAnyException();

            log.info("\n\tthen\n\t  ┗ assertThatCode(()->noticeService.updateNotice(noticeDTO)).doesNotThrowAnyException()\n");
        }

        @Test
        @Order(2)
        @DisplayName("updateNotice - NoticeNotFoundException")
        void updateNotice_NotFound(){
            log.info("NoticeService - updateNotice_NotFound");

            // given
            NoticeDTO noticeDTO = NoticeDTO.builder().num(11).build();

            given(noticeService.isExist(noticeDTO.getNum())).willReturn(false);

            log.info("\n\tgiven" +
                    "\n\t  ┗ NoticeDTO noticeDTO" +
                    "\n\t\t  ┗ num: "+noticeDTO.getNum()+
                    "\n\t  ┗ noticeService.isExist(noticeDTO.getNum()).willReturn(false)\n");
            // when
            Throwable result = catchThrowable(()->noticeService.updateNotice(noticeDTO));

            log.info("\n\twhen\n\t  ┗ Throwable result = catchThrowable(()->noticeService.updateNotice(noticeDTO))\n");
            // then
            assertThat(result).isInstanceOf(NoticeNotFoundException.class);

            log.info("\n\tthen\n\t  ┗ assertThat(result).isInstanceOf(NoticeNotFoundException.class)\n");
        }
    }

    @Nested
    @DisplayName("deleteNotice")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Delete{
        @Test
        @Order(1)
        @DisplayName("deleteNotice")
        void deleteNotice(){
            log.info("NoticeService - deleteNotice");

            // given
            Integer num = 1;

            given(noticeService.isExist(num)).willReturn(true);

            log.info("""

                    \tgiven
                    \t  ┗ Integer num = 1
                    \t  ┗ noticeService.isExist(num).willReturn(true)
                    """);
            // when
            noticeService.deleteNotice(num);

            log.info("\n\twhen\n\t  ┗ noticeService.deleteNotice(num)\n");
            // then
            assertThatCode(()->noticeService.deleteNotice(num)).doesNotThrowAnyException();

            log.info("\n\tthen\n\t  ┗ assertThatCode(()->noticeService.deleteNotice(num)).doesNotThrowAnyException()\n");
        }

        @Test
        @Order(2)
        @DisplayName("deleteNotice - NoticeNotFoundException")
        void deleteNotice_NotFound(){
            log.info("NoticeService - deleteNotice_NotFound");

            // given
            Integer num = 11;

            given(noticeService.isExist(num)).willReturn(false);

            log.info("""

                    \tgiven
                    \t  ┗ Integer num = 11
                    \t  ┗ noticeService.isExist(num).willReturn(false)
                    """);
            // when
            Throwable result = catchThrowable(()->noticeService.deleteNotice(num));

            log.info("\n\twhen\n\t  ┗ Throwable result = catchThrowable(()->noticeService.deleteNotice(num))\n");
            // then
            assertThat(result).isInstanceOf(NoticeNotFoundException.class);

            log.info("\n\tthen\n\t  ┗ assertThat(result).isInstanceOf(NoticeNotFoundException.class)\n");
        }
    }

}
