package com.project.clickit.member;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.login.MemberNotFoundException;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@Slf4j
@DisplayName("MemberService 테스트")
@ExtendWith({MockitoExtension.class})
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("isExist Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class isExist{
        @Test
        @Order(1)
        @DisplayName("isExist Test")
        void isExistTest(){
            log.info("isExist Test");
            // given
            String id = "test2";

            given(memberRepository.existsById(id)).willReturn(true);

            log.info("""

                            \tgiven
                            \t  ┣ id: {}
                            \t  ┗ given(memberRepository.existsById(id)).willReturn(true)
                            """,
                    id);
            // when
            Boolean result = memberService.isExist(id);

            log.info("""

                    \twhen
                    \t  ┗ memberService.isExist(id)
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
        @DisplayName("isExist Test(존재하지 않는 아이디)")
        void isExistTestDuplicated(){
            log.info("isExist Test(존재하지 않는 아이디)");
            // given
            String id = "1234";

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""
                            
                            \tgiven
                            \t  ┣ id: {}
                            \t  ┗ when(memberRepository.existsById(id)).thenReturn(False)
                            """,
                    id);
            // when
            Boolean result = memberService.isExist(id);

            log.info("""

                    \twhen
                    \t  ┗ memberService.isExist(id)
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
    @DisplayName("Create Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateTest{
        @Test
        @Order(1)
        @DisplayName("CreateMember Test")
        void createMemberTest(){
            log.info("CreateMember Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("test_case_id")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test_case_email@test.com")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(false);

            when(memberRepository.save(any(MemberEntity.class))).thenReturn(new MemberEntity());

            log.info("""
                            \tgiven
                            \t  ┣ MemberDTO
                            \t  ┃  ┣ id: {}
                            \t  ┃  ┣ password: {}
                            \t  ┃  ┣ name: {}
                            \t  ┃  ┣ email: {}
                            \t  ┃  ┣ phone: {}
                            \t  ┃  ┣ studentNum: {}
                            \t  ┃  ┣ type: {}
                            \t  ┃  ┗ dormitoryDTO
                            \t  ┃     ┣ id: {}
                            \t  ┃     ┗ name: {}
                            \t  ┣ given(memberRepository.existsById(memberDTO.getId())).willReturn(false)
                            \t  ┗ when(memberRepository.save(any(MemberEntity.class))).thenReturn(new MemberEntity())
                            """,
                    memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(),
                    memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(),
                    memberDTO.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            memberService.create(memberDTO);

            log.info("""

                    \twhen
                    \t  ┗ memberService.create(memberDTO)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().save(any(MemberEntity.class)),
                    () -> assertThatCode(() -> memberService.create(memberDTO)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ then(memberRepository).should().save(any(MemberEntity.class))
                    \t  ┗ assertThatCode(() -> memberService.create(memberDTO)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("CreateMember Test(아이디 중복)")
        void createMemberTestWithDuplicatedId() {
            log.info("CreateMember Test(아이디 중복)");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("test2")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(true);

            then(memberRepository).should(never()).save(any(MemberEntity.class));

            log.info("""
                            
                            \tgiven
                            \t  ┣ MemberDTO
                            \t  ┃  ┣ id: {}
                            \t  ┃  ┣ password: {}
                            \t  ┃  ┣ name: {}
                            \t  ┃  ┣ email: {}
                            \t  ┃  ┣ phone: {}
                            \t  ┃  ┣ studentNum: {}
                            \t  ┃  ┣ type: {}
                            \t  ┃  ┗ dormitoryDTO
                            \t  ┃     ┣ id: {}
                            \t  ┃     ┗ name: {}
                            \t  ┣ given(memberRepository.existsById(memberDTO.getId())).willReturn(true)
                            \t  ┗ then(memberRepository).should(never()).save(any(MemberEntity.class))
                            """,
                    memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(),
                    memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(),
                    memberDTO.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            Throwable result = catchThrowable(() -> memberService.create(memberDTO));

            log.info("""

                    \twhen
                    \t  ┗ memberService.create(memberDTO)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class),
                    () -> then(memberRepository).should(never()).save(any(MemberEntity.class))
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(DuplicatedIdException.class)
                    \t  ┗ then(memberRepository).should(never()).save(any(MemberEntity.class)
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("CreateList Test")
        void createListTest() {
            log.info("CreateList Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO1 = MemberDTO.builder()
                    .id("service_test_1")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            MemberDTO memberDTO2 = MemberDTO.builder()
                    .id("service_test_2")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            List<MemberDTO> memberDTOList = List.of(memberDTO1, memberDTO2);

            given(memberRepository.existsById(memberDTO1.getId())).willReturn(false);
            given(memberRepository.existsById(memberDTO2.getId())).willReturn(false);

            when(memberRepository.saveAll(anyList())).thenReturn(List.of(new MemberEntity(), new MemberEntity()));

            log.info("""
                    
                    \tgiven
                    \t  ┣ List<MemberDTO>
                    \t  ┃  ┣ MemberDTO1
                    \t  ┃  ┃  ┣ id: {}
                    \t  ┃  ┃  ┣ password: {}
                    \t  ┃  ┃  ┣ name: {}
                    \t  ┃  ┃  ┣ email: {}
                    \t  ┃  ┃  ┣ phone: {}
                    \t  ┃  ┃  ┣ studentNum: {}
                    \t  ┃  ┃  ┣ type: {}
                    \t  ┃  ┃  ┗ dormitoryDTO
                    \t  ┃  ┃     ┣ id: {}
                    \t  ┃  ┃     ┗ name: {}
                    \t  ┃  ┗ MemberDTO2
                    \t  ┃     ┣ id: {}
                    \t  ┃     ┣ password: {}
                    \t  ┃     ┣ name: {}
                    \t  ┃     ┣ email: {}
                    \t  ┃     ┣ phone: {}
                    \t  ┃     ┣ studentNum: {}
                    \t  ┃     ┣ type: {}
                    \t  ┃     ┗ dormitoryDTO
                    \t  ┃        ┣ id: {}
                    \t  ┃        ┗ name: {}
                    \t  ┣ given(memberRepository.existsById(memberDTO1.getId())).willReturn(false)
                    \t  ┣ given(memberRepository.existsById(memberDTO2.getId())).willReturn(false)
                    \t  ┗ when(memberRepository.saveAll(anyList())).thenReturn(List.of(new MemberEntity(), new MemberEntity()))
                    """,
                    memberDTO1.getId(), memberDTO1.getPassword(), memberDTO1.getName(),
                    memberDTO1.getEmail(), memberDTO1.getPhone(), memberDTO1.getStudentNum(),
                    memberDTO1.getType(), dormitoryDTO.getId(), dormitoryDTO.getName(),
                    memberDTO2.getId(), memberDTO2.getPassword(), memberDTO2.getName(),
                    memberDTO2.getEmail(), memberDTO2.getPhone(), memberDTO2.getStudentNum(),
                    memberDTO2.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            memberService.createList(memberDTOList);

            log.info("""

                    \twhen
                    \t  ┗ memberService.createList(memberDTOList)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().saveAll(anyList()),
                    () -> verify(memberRepository, times(1)).saveAll(anyList()),
                    () -> assertThatCode(() -> memberService.createList(memberDTOList)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ then(memberRepository).should().saveAll(anyList())
                    \t  ┣ verify(memberRepository, times(1)).saveAll(anyList())
                    \t  ┗ assertThatCode(() -> memberService.createList(memberDTOList)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("CreateList Test(아이디 중복)")
        void createListTestDuplicated(){
            log.info("CreateList Test(아이디 중복)");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO1 = MemberDTO.builder()
                    .id("test2")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            MemberDTO memberDTO2 = MemberDTO.builder()
                    .id("service_test_2")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            List<MemberDTO> memberDTOList = List.of(memberDTO1, memberDTO2);

            given(memberRepository.existsById(memberDTO1.getId())).willReturn(true);
            given(memberRepository.existsById(memberDTO2.getId())).willReturn(false);

            when(memberRepository.saveAll(anyList())).thenReturn(List.of(new MemberEntity(), new MemberEntity()));

            log.info("""

                    \tgiven
                    \t  ┣ List<MemberDTO>
                    \t  ┃  ┣ MemberDTO1
                    \t  ┃  ┃  ┣ id: {}
                    \t  ┃  ┃  ┣ password: {}
                    \t  ┃  ┃  ┣ name: {}
                    \t  ┃  ┃  ┣ email: {}
                    \t  ┃  ┃  ┣ phone: {}
                    \t  ┃  ┃  ┣ studentNum: {}
                    \t  ┃  ┃  ┣ type: {}
                    \t  ┃  ┃  ┗ dormitoryDTO
                    \t  ┃  ┃     ┣ id: {}
                    \t  ┃  ┃     ┗ name: {}
                    \t  ┃  ┗ MemberDTO2
                    \t  ┃     ┣ id: {}
                    \t  ┃     ┣ password: {}
                    \t  ┃     ┣ name: {}
                    \t  ┃     ┣ email: {}
                    \t  ┃     ┣ phone: {}
                    \t  ┃     ┣ studentNum: {}
                    \t  ┃     ┣ type: {}
                    \t  ┃     ┗ dormitoryDTO
                    \t  ┃        ┣ id: {}
                    \t  ┃        ┗ name: {}
                    \t  ┣ given(memberRepository.existsById(memberDTO1.getId())).willReturn(true)
                    \t  ┣ given(memberRepository.existsById(memberDTO2.getId())).willReturn(false)
                    \t  ┗ when(memberRepository.saveAll(anyList())).thenReturn(List.of(new MemberEntity(), new MemberEntity()))
                    """,
                    memberDTO1.getId(), memberDTO1.getPassword(), memberDTO1.getName(),
                    memberDTO1.getEmail(), memberDTO1.getPhone(), memberDTO1.getStudentNum(),
                    memberDTO1.getType(), dormitoryDTO.getId(), dormitoryDTO.getName(),
                    memberDTO2.getId(), memberDTO2.getPassword(), memberDTO2.getName(),
                    memberDTO2.getEmail(), memberDTO2.getPhone(), memberDTO2.getStudentNum(),
                    memberDTO2.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            Throwable result = catchThrowable(() -> memberService.createList(memberDTOList));

            log.info("""

                    \twhen
                    \t  ┗ memberService.createList(memberDTOList)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class),
                    () -> verify(memberRepository, never()).saveAll(anyList()),
                    () -> then(memberRepository).should(never()).saveAll(anyList())
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(DuplicatedIdException.class)
                    \t  ┣ verify(memberRepository, never()).saveAll(anyList())
                    \t  ┗ then(memberRepository).should(never()).saveAll(anyList())
                    """);
        }
    }

    @Nested
    @DisplayName("Read Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadTest{
        @Test
        @Order(1)
        @DisplayName("getAll Test")
        void getAllTest() {
            log.info("getAll Test");
            // given
            Page<MemberEntity> memberEntityPage = Page.empty();

            given(memberRepository.findAll(Pageable.ofSize(10))).willReturn(memberEntityPage);

            log.info("""

                    \tgiven
                    \t  ┣ memberEntityPage: Page.empty()
                    \t  ┗ when(memberRepository.findAll(Pageable.ofSize(10))).thenReturn(memberEntityPage)
                    """);
            // when
            Page<MemberDTO> result = memberService.getAll(Pageable.ofSize(10));

            log.info("""

                    \twhen
                    \t  ┗ memberService.getAll(Pageable.ofSize(10))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> assertThatIterable(result).allMatch(Objects::nonNull)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(Page.class)
                    \t  ┗ assertThatCode(() -> assertThatIterable(result).allMatch(memberDTO -> memberDTO instanceof MemberDTO)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("findByMemberId Test")
        void findByMemberIdTest() {
            log.info("findByMemberId Test");
            // given
            String id = "test_member_id";

            given(memberRepository.existsById(id)).willReturn(true);

            when(memberRepository.findById(id)).thenReturn(new MemberEntity());

            log.info("""

                    \tgiven
                    \t  ┣ id: test_member_id
                    \t  ┣ given(memberRepository.existsById(id)).willReturn(true)
                    \t  ┗ when(memberRepository.findById(id)).thenReturn(new MemberEntity())
                    """);
            // when
            MemberDTO result = memberService.findByMemberId(id);

            log.info("""

                    \twhen
                    \t  ┗ memberService.findByMemberId(id)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(MemberDTO.class),
                    () -> assertThatCode(() -> memberService.findByMemberId(id)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(MemberDTO.class)
                    \t  ┗ assertThatCode(() -> memberService.findByMemberId(id)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("findByMemberId Test(존재하지 않는 아이디)")
        void findByMemberIdTestNotFound(){
            log.info("findByMemberId Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""

                    \tgiven
                    \t  ┣ id: never_used_id
                    \t  ┗ when(memberRepository.existsById(id)).thenReturn(false)
                    """);
            // when
            Throwable result = catchThrowable(() -> memberService.findByMemberId(id));

            log.info("""

                    \twhen
                    \t  ┗ memberService.findByMemberId(id)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(RuntimeException.class),
                    () -> then(memberRepository).should(never()).findById(id)
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(RuntimeException.class)
                    \t  ┗ then(memberRepository).should(never()).findById(id)
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("findByMemberName Test")
        void findByMemberNameTest(){
            log.info("findByMemberName Test");
            // given
            String name = "test_member_name";

            Page<MemberEntity> memberEntityPage = Page.empty();

            given(memberRepository.findByMemberName(name, Pageable.ofSize(10))).willReturn(memberEntityPage);

            log.info("""

                    \tgiven
                    \t  ┣ name: test_member_name
                    \t  ┗ when(memberRepository.findByMemberName(name, Pageable.ofSize(10))).thenReturn(memberEntityPage)
                    """);
            // when
            Page<MemberDTO> result = memberService.findByMemberName(name, Pageable.ofSize(10));

            log.info("""

                    \twhen
                    \t  ┗ memberService.findByMemberName(name, Pageable.ofSize(10))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> memberService.findByMemberName(name, Pageable.ofSize(10))).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(Page.class)
                    \t  ┗ assertThatCode(() -> memberService.findByMemberName(name, Pageable.ofSize(10))).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(5)
        @DisplayName("findPasswordByMemberId Test")
        void findPasswordByMemberIdTest(){
            log.info("findPasswordByMemberId Test");
            // given
            String id = "test_member_id";
            String password = "test_member_password";

            given(memberRepository.existsById(id)).willReturn(true);

            given(memberRepository.findPasswordByMemberId(id)).willReturn(password);

            log.info("""

                    \tgiven
                    \t  ┣ id: {}
                    \t  ┣ password: {}
                    \t  ┣ given(memberRepository.existsById(id)).willReturn(true)
                    \t  ┗ given(memberRepository.findPasswordByMemberId(id)).willReturn(password)
                    """, id, password);
            // when
            String result = memberService.findPasswordByMemberId(id);

            log.info("""

                    \twhen
                    \t  ┗ memberService.findPasswordByMemberId(id)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(String.class),
                    () -> assertThat(result).isEqualTo(password)
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(String.class)
                    \t  ┗ assertThat(result).isEqualTo(password)
                    """);
        }

        @Test
        @Order(6)
        @DisplayName("findPasswordByMemberId Test(존재하지 않는 아이디)")
        void findPasswordByMemberIdTestNotFound(){
            log.info("findPasswordByMemberId Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""

                    \tgiven
                    \t  ┣ id: never_used_id
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(false)
                    """);
            // when
            Throwable result = catchThrowable(() -> memberService.findPasswordByMemberId(id));

            log.info("""

                    \twhen
                    \t  ┗ memberService.findPasswordByMemberId(id)
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(MemberNotFoundException.class),
                    () -> then(memberRepository).should(never()).findPasswordByMemberId(id)
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(MemberNotFoundException.class)
                    \t  ┗ then(memberRepository).should(never()).findPasswordByMemberId(id)
                    """);
        }

        @Test
        @Order(7)
        @DisplayName("findByDormitoryId Test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId Test");
            // given
            String dormitoryId = "test_dormitory_id";

            Page<MemberEntity> memberEntityPage = Page.empty();

            given(memberRepository.findByDormitoryId(dormitoryId, Pageable.ofSize(10))).willReturn(memberEntityPage);

            log.info("""

                    \tgiven
                    \t  ┣ dormitoryId: {}
                    \t  ┗ given(memberRepository.findByDormitoryId(dormitoryId, Pageable.ofSize(10))).willReturn(memberEntityPage)
                    """, dormitoryId);
            // when
            Page<MemberDTO> result = memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10));

            log.info("""

                    \twhen
                    \t  ┗ memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10))).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isInstanceOf(Page.class)
                    \t  ┗ assertThatCode(() -> memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(8)
        @DisplayName("findByDormitoryId Test(존재하지 않는 아이디)")
        void findByDormitoryIdTestNotFound(){
            // 존재하지 않는 기숙사 아이디로 조회할 일은 없을 것 같지만 혹시 모르니 테스트 진행
            log.info("findByDormitoryId Test(존재하지 않는 아이디)");
            // given
            String dormitoryId = "never_used_id";

            log.info("""

                    \tgiven
                    \t  ┣ dormitoryId: {}
                    """, dormitoryId);
            // when
            Throwable result = catchThrowable(() -> memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10)));

            log.info("""

                    \twhen
                    \t  ┗ memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10))
                    """);
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(RuntimeException.class)
//                    () -> verify(memberRepository, never()).findByDormitoryId(dormitoryId, Pageable.ofSize(10))
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isInstanceOf(RuntimeException.class)
                    \t  ┗ verify(memberRepository, never()).findByDormitoryId(dormitoryId, Pageable.ofSize(10))
                    """);
        }
    }

    @Nested
    @DisplayName("Update Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateTest{
        @Test
        @Order(1)
        @DisplayName("update Test")
        void updateTest() {
            log.info("Update Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("test_case_id")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ MemberDTO
                    \t  ┃  ┣ id: {}
                    \t  ┃  ┣ password: {}
                    \t  ┃  ┣ name: {}
                    \t  ┃  ┣ email: {}
                    \t  ┃  ┣ phone: {}
                    \t  ┃  ┣ studentNum: {}
                    \t  ┃  ┣ type: {}
                    \t  ┃  ┗ dormitoryDTO
                    \t  ┃     ┣ id: {}
                    \t  ┃     ┗ name: {}
                    \t  ┗ given(memberRepository.existsById(memberDTO.getId())).willReturn(true)
                    """,
                    memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(),
                    memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(),
                    memberDTO.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            memberService.update(memberDTO);

            log.info("""

                    \twhen
                    \t  ┗ memberService.update(memberDTO)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().save(any(MemberEntity.class)),
                    () -> verify(memberRepository, times(1)).save(any(MemberEntity.class)),
                    () -> assertThatCode(() -> memberService.update(memberDTO)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ then(memberRepository).should().save(any(MemberEntity.class))
                    \t  ┣ verify(memberRepository, times(1)).save(any(MemberEntity.class))
                    \t  ┗ assertThatCode(() -> memberService.update(memberDTO)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("update Test(존재하지 않는 아이디)")
        void updateMemberTestWithNotExistedId() {
            log.info("update Test(존재하지 않는 아이디)");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("never_used_id")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ MemberDTO
                    \t  ┃  ┣ id: {}
                    \t  ┃  ┣ password: {}
                    \t  ┃  ┣ name: {}
                    \t  ┃  ┣ email: {}
                    \t  ┃  ┣ phone: {}
                    \t  ┃  ┣ studentNum: {}
                    \t  ┃  ┣ type: {}
                    \t  ┃  ┗ dormitoryDTO
                    \t  ┃     ┣ id: {}
                    \t  ┃     ┗ name: {}
                    \t  ┗ given(memberRepository.existsById(memberDTO.getId())).willReturn(false)
                    """,
                    memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(),
                    memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(),
                    memberDTO.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            Throwable exception = catchThrowable(() -> memberService.update(memberDTO));

            log.info("""

                    \twhen
                    \t  ┗ Throwable exception = catchThrowable(() -> memberService.update(memberDTO))
                    """);
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(MemberNotFoundException.class),
                    () -> then(memberRepository).should(never()).save(any(MemberEntity.class))
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(exception).isInstanceOf(MemberNotFoundException.class)
                    \t  ┗ then(memberRepository).should(never()).save(any(MemberEntity.class)
                    """);
        }

        @Test
        @Order(3)
        @DisplayName("updateMemberForStaff Test")
        void updateMemberForStaffTest() {
            log.info("updateMemberForStaff Test");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("test_member_id")
                    .password("test_member_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(true);

            log.info("""
                           
                            \tgiven
                            \t  ┣ MemberDTO
                            \t  ┃  ┣ id: {}
                            \t  ┃  ┣ password: {}
                            \t  ┃  ┣ name: {}
                            \t  ┃  ┣ email: {}
                            \t  ┃  ┣ phone: {}
                            \t  ┃  ┣ studentNum: {}
                            \t  ┃  ┣ type: {}
                            \t  ┃  ┗ dormitoryDTO
                            \t  ┃     ┣ id: {}
                            \t  ┃     ┗ name: {}
                            \t  ┗ given(memberRepository.existsById(memberDTO.getId())).willReturn(true)
                           \s""",
                    memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(),
                    memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(),
                    memberDTO.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            memberService.updateMemberForStaff(memberDTO);

            log.info("""

                    \twhen
                    \t  ┗ memberService.updateMemberForStaff(memberDTO)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()),
                    () -> verify(memberRepository, times(1)).updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()),
                    () -> assertThatCode(() -> memberService.updateMemberForStaff(memberDTO)).doesNotThrowAnyException()
            );

            log.info("""

                    \tthen
                    \t  ┣ then(memberRepository).should().updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())
                    \t  ┣ verify(memberRepository, times(1)).updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())
                    \t  ┗ assertThatCode(() -> memberService.updateMemberForStaff(memberDTO)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(4)
        @DisplayName("updateMemberForStaff Test(존재하지 않는 아이디)")
        void updateMemberForStaffTestWithNotExistedId() {
            log.info("updateMemberForStaff Test(존재하지 않는 아이디)");
            // given
            DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                    .id("test_case_id")
                    .name("test_case_name")
                    .build();

            MemberDTO memberDTO = MemberDTO.builder()
                    .id("never_used_id")
                    .password("test_member_password")
                    .name("test_case")
                    .email("test")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("CLICKIT_STUDENT")
                    .dormitoryDTO(dormitoryDTO)
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(false);

            log.info("""
                            
                            \tgiven
                            \t  ┣ MemberDTO
                            \t  ┃  ┣ id: {}
                            \t  ┃  ┣ password: {}
                            \t  ┃  ┣ name: {}
                            \t  ┃  ┣ email: {}
                            \t  ┃  ┣ phone: {}
                            \t  ┃  ┣ studentNum: {}
                            \t  ┃  ┣ type: {}
                            \t  ┃  ┗ dormitoryDTO
                            \t  ┃     ┣ id: {}
                            \t  ┃     ┗ name: {}
                            \t  ┗ given(memberRepository.existsById(memberDTO.getId())).willReturn(false)
                            """,
                    memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(),
                    memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(),
                    memberDTO.getType(), dormitoryDTO.getId(), dormitoryDTO.getName());
            // when
            Throwable exception = catchThrowable(() -> memberService.updateMemberForStaff(memberDTO));

            log.info("""

                    \twhen
                    \t  ┗ Throwable exception = catchThrowable(() -> memberService.updateMemberForStaff(memberDTO))
                    """);
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(MemberNotFoundException.class),
                    () -> then(memberRepository).should(never()).updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())
            );

            log.info("""

                    \tthen
                    \t  ┣ assertThat(exception).isInstanceOf(MemberNotFoundException.class)
                    \t  ┗ then(memberRepository).should(never()).updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())
                    """);
        }

        @Test
        @Order(5)
        @DisplayName("updatePassword Test")
        void updatePasswordTest(){
            log.info("updatePassword Test");
            // given
            String id = "test_member_id";
            String password = "update_password";

            given(memberRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id: {}
                    \t  ┣ password: {}
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(true)
                    """, id, password);
            // when
            memberService.updatePassword(id, password);

            log.info("""
                    
                    \twhen
                    \t  ┗ memberService.updatePassword(id, password)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().updatePassword(id, password),
                    () -> verify(memberRepository, times(1)).updatePassword(id, password),
                    () -> assertThatCode(() -> memberService.updatePassword(id, password)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ then(memberRepository).should().updatePassword(id, password)
                    \t  ┣ verify(memberRepository, times(1)).updatePassword(id, password)
                    \t  ┗ assertThatCode(() -> memberService.updatePassword(id, password)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(6)
        @DisplayName("updatePassword Test(존재하지 않는 아이디)")
        void updatePasswordTestWithNotExistedId(){
            log.info("updatePassword Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";
            String password = "update_password";

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id: {}
                    \t  ┣ password: {}
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(false)
                    """, id, password);
            // when
            Throwable exception = catchThrowable(() -> memberService.updatePassword(id, password));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable exception = catchThrowable(() -> memberService.updatePassword(id, password))
                    """);
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(MemberNotFoundException.class),
                    () -> then(memberRepository).should(never()).updatePassword(id, password)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(exception).isInstanceOf(MemberNotFoundException.class)
                    \t  ┗ then(memberRepository).should(never()).updatePassword(id, password)
                    """);
        }

        @Test
        @Order(7)
        @DisplayName("updateRefreshToken Test")
        void updateRefreshTokenTest(){
            log.info("updateRefreshToken Test");
            // given
            String id = "test_member_id";
            String refreshToken = "test_refresh_token";

            given(memberRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id: {}
                    \t  ┣ refreshToken: {}
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(true)
                    """, id, refreshToken);
            // when
            memberService.updateRefreshToken(id, refreshToken);

            log.info("""
                    
                    \twhen
                    \t  ┗ memberService.updateRefreshToken(id, refreshToken)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().updateRefreshToken(id, refreshToken),
                    () -> verify(memberRepository, times(1)).updateRefreshToken(id, refreshToken),
                    () -> assertThatCode(() -> memberService.updateRefreshToken(id, refreshToken)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ then(memberRepository).should().updateRefreshToken(id, refreshToken)
                    \t  ┣ verify(memberRepository, times(1)).updateRefreshToken(id, refreshToken)
                    \t  ┗ assertThatCode(() -> memberService.updateRefreshToken(id, refreshToken)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(8)
        @DisplayName("updateRefreshToken Test(존재하지 않는 아이디)")
        void updateRefreshTokenTestWithNotExistedId(){
            log.info("updateRefreshToken Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";
            String refreshToken = "test_refresh_token";

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id: {}
                    \t  ┣ refreshToken: {}
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(false)
                    """, id, refreshToken);
            // when
            Throwable exception = catchThrowable(() -> memberService.updateRefreshToken(id, refreshToken));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable exception = catchThrowable(() -> memberService.updateRefreshToken(id, refreshToken))
                    """);
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(MemberNotFoundException.class),
                    () -> then(memberRepository).should(never()).updateRefreshToken(id, refreshToken)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(exception).isInstanceOf(MemberNotFoundException.class)
                    \t  ┗ then(memberRepository).should(never()).updateRefreshToken(id, refreshToken)
                    """);
        }
    }

    @Nested
    @DisplayName("Delete Test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteTest{
        @Test
        @Order(1)
        @DisplayName("deleteById Test")
        void deleteByIdTest() {
            log.info("deleteById Test");
            // given
            String id = "test_member_id";

            given(memberRepository.existsById(id)).willReturn(true);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id: {}
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(true)
                    """, id);
            // when
            memberService.deleteById(id);

            log.info("""
                    
                    \twhen
                    \t  ┗ memberService.deleteById(id)
                    """);
            // then
            assertAll(
                    () -> then(memberRepository).should().deleteById(id),
                    () -> verify(memberRepository, times(1)).deleteById(id),
                    () -> assertThatCode(() -> memberService.deleteById(id)).doesNotThrowAnyException()
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ then(memberRepository).should().deleteById(id)
                    \t  ┣ verify(memberRepository, times(1)).deleteById(id)
                    \t  ┗ assertThatCode(() -> memberService.deleteById(id)).doesNotThrowAnyException()
                    """);
        }

        @Test
        @Order(2)
        @DisplayName("deleteById Test(존재하지 않는 아이디)")
        void deleteMemberTestWithNotExistedId() {
            log.info("deleteById Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(id)).willReturn(false);

            log.info("""
                    
                    \tgiven
                    \t  ┣ id: {}
                    \t  ┗ given(memberRepository.existsById(id)).willReturn(false)
                    """, id);
            // when
            Throwable exception = catchThrowable(() -> memberService.deleteById(id));

            log.info("""
                    
                    \twhen
                    \t  ┗ Throwable exception = catchThrowable(() -> memberService.deleteById(id))
                    """);
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(MemberNotFoundException.class),
                    () -> then(memberRepository).should(never()).deleteById(id)
            );

            log.info("""
                    
                    \tthen
                    \t  ┣ assertThat(exception).isInstanceOf(MemberNotFoundException.class)
                    \t  ┗ then(memberRepository).should(never()).deleteById(id)
                    """);
        }
    }
}
