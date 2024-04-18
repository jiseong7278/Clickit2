package com.project.clickit.member;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
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

    @BeforeEach
    void setUp(){

    }

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

            when(memberRepository.existsById(id)).thenReturn(false);

            log.info("\n\tgiven" +
                    "\n\t  ┣ id: " + id +
                    "\n\t  ┗ when(memberRepository.existsById(id)).thenReturn(false)\n");
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

        @Test
        @Order(2)
        @DisplayName("isExist Test(존재하지 않는 아이디)")
        void isExistTestDuplicated(){
            log.info("isExist Test(존재하지 않는 아이디)");
            // given
            String id = "1234";

            when(memberRepository.existsById(id)).thenReturn(true);

            log.info("\n\tgiven" +
                    "\n\t  ┣ id: " + id +
                    "\n\t  ┗ when(memberRepository.existsById(id)).thenReturn(true)\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┣ MemberDTO" +
                    "\n\t  ┃  ┣ id: " + memberDTO.getId() +
                    "\n\t  ┃  ┣ password: " + memberDTO.getPassword() +
                    "\n\t  ┃  ┣ name: " + memberDTO.getName() +
                    "\n\t  ┃  ┣ email: " + memberDTO.getEmail() +
                    "\n\t  ┃  ┣ phone: " + memberDTO.getPhone() +
                    "\n\t  ┃  ┣ studentNum: " + memberDTO.getStudentNum() +
                    "\n\t  ┃  ┣ type: " + memberDTO.getType() +
                    "\n\t  ┃  ┗ dormitoryDTO" +
                    "\n\t  ┃     ┣ id: " + dormitoryDTO.getId() +
                    "\n\t  ┃     ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┣ given(memberRepository.existsById(memberDTO.getId())).willReturn(false)" +
                    "\n\t  ┗ when(memberRepository.save(any(MemberEntity.class))).thenReturn(new MemberEntity())\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┣ MemberDTO" +
                    "\n\t  ┃  ┣ id: " + memberDTO.getId() +
                    "\n\t  ┃  ┣ password: " + memberDTO.getPassword() +
                    "\n\t  ┃  ┣ name: " + memberDTO.getName() +
                    "\n\t  ┃  ┣ email: " + memberDTO.getEmail() +
                    "\n\t  ┃  ┣ phone: " + memberDTO.getPhone() +
                    "\n\t  ┃  ┣ studentNum: " + memberDTO.getStudentNum() +
                    "\n\t  ┃  ┣ type: " + memberDTO.getType() +
                    "\n\t  ┃  ┗ dormitoryDTO" +
                    "\n\t  ┃     ┣ id: " + dormitoryDTO.getId() +
                    "\n\t  ┃     ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┣ given(memberRepository.existsById(memberDTO.getId())).willReturn(true)" +
                    "\n\t  ┗ then(memberRepository).should(never()).save(any(MemberEntity.class))\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┣ List<MemberDTO>" +
                    "\n\t  ┃  ┣ MemberDTO1" +
                    "\n\t  ┃  ┃  ┣ id: " + memberDTO1.getId() +
                    "\n\t  ┃  ┃  ┣ password: " + memberDTO1.getPassword() +
                    "\n\t  ┃  ┃  ┣ name: " + memberDTO1.getName() +
                    "\n\t  ┃  ┃  ┣ email: " + memberDTO1.getEmail() +
                    "\n\t  ┃  ┃  ┣ phone: " + memberDTO1.getPhone() +
                    "\n\t  ┃  ┃  ┣ studentNum: " + memberDTO1.getStudentNum() +
                    "\n\t  ┃  ┃  ┣ type: " + memberDTO1.getType() +
                    "\n\t  ┃  ┃  ┗ dormitoryDTO" +
                    "\n\t  ┃  ┃     ┣ id: " + dormitoryDTO.getId() +
                    "\n\t  ┃  ┃     ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┃  ┗ MemberDTO2" +
                    "\n\t  ┃     ┣ id: " + memberDTO2.getId() +
                    "\n\t  ┃     ┣ password: " + memberDTO2.getPassword() +
                    "\n\t  ┃     ┣ name: " + memberDTO2.getName() +
                    "\n\t  ┃     ┣ email: " + memberDTO2.getEmail() +
                    "\n\t  ┃     ┣ phone: " + memberDTO2.getPhone() +
                    "\n\t  ┃     ┣ studentNum: " + memberDTO2.getStudentNum() +
                    "\n\t  ┃     ┣ type: " + memberDTO2.getType() +
                    "\n\t  ┃     ┗ dormitoryDTO" +
                    "\n\t  ┃        ┣ id: " + dormitoryDTO.getId() +
                    "\n\t  ┃        ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┣ given(memberRepository.existsById(memberDTO1.getId())).willReturn(false)" +
                    "\n\t  ┣ given(memberRepository.existsById(memberDTO2.getId())).willReturn(false)" +
                    "\n\t  ┗ when(memberRepository.saveAll(anyList())).thenReturn(List.of(new MemberEntity(), new MemberEntity()))\n");
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

            log.info("\n\tgiven" +
                    "\n\t  ┣ List<MemberDTO>" +
                    "\n\t  ┃  ┣ MemberDTO1" +
                    "\n\t  ┃  ┃  ┣ id: " + memberDTO1.getId() +
                    "\n\t  ┃  ┃  ┣ password: " + memberDTO1.getPassword() +
                    "\n\t  ┃  ┃  ┣ name: " + memberDTO1.getName() +
                    "\n\t  ┃  ┃  ┣ email: " + memberDTO1.getEmail() +
                    "\n\t  ┃  ┃  ┣ phone: " + memberDTO1.getPhone() +
                    "\n\t  ┃  ┃  ┣ studentNum: " + memberDTO1.getStudentNum() +
                    "\n\t  ┃  ┃  ┣ type: " + memberDTO1.getType() +
                    "\n\t  ┃  ┃  ┗ dormitoryDTO" +
                    "\n\t  ┃  ┃     ┣ id: " + dormitoryDTO.getId() +
                    "\n\t  ┃  ┃     ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┃  ┗ MemberDTO2" +
                    "\n\t  ┃     ┣ id: " + memberDTO2.getId() +
                    "\n\t  ┃     ┣ password: " + memberDTO2.getPassword() +
                    "\n\t  ┃     ┣ name: " + memberDTO2.getName() +
                    "\n\t  ┃     ┣ email: " + memberDTO2.getEmail() +
                    "\n\t  ┃     ┣ phone: " + memberDTO2.getPhone() +
                    "\n\t  ┃     ┣ studentNum: " + memberDTO2.getStudentNum() +
                    "\n\t  ┃     ┣ type: " + memberDTO2.getType() +
                    "\n\t  ┃     ┗ dormitoryDTO" +
                    "\n\t  ┃        ┣ id: " + dormitoryDTO.getId() +
                    "\n\t  ┃        ┗ name: " + dormitoryDTO.getName() +
                    "\n\t  ┣ given(memberRepository.existsById(memberDTO1.getId())).willReturn(true)" +
                    "\n\t  ┣ given(memberRepository.existsById(memberDTO2.getId())).willReturn(false)" +
                    "\n\t  ┗ when(memberRepository.saveAll(anyList())).thenReturn(List.of(new MemberEntity(), new MemberEntity()))\n");
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
    @DisplayName("회원 조회 테스트")
    class ReadTest{
        @Test
        @DisplayName("회원 조회 테스트")
        void readMemberTest() {
            log.info("회원 조회 테스트");
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
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Page.class);

            log.info("""

                    \tthen
                    \t  ┣ assertThat(result).isNotNull()
                    \t  ┣ assertThat(result).isNotEmpty()
                    \t  ┣ assertThat(result).isInstanceOf(Page.class)
                    \t  ┗ assertThatIterable(result).allMatch(memberDTO -> memberDTO instanceof MemberDTO)
                    """);
        }

        @Test
        @DisplayName("회원 조회 테스트: 존재하지 않는 아이디")
        void readMemberTestWithNotExistedId() {
            log.info("회원 조회 테스트: 존재하지 않는 아이디");
            // given
            log.info("given - 존재하지 않는 아이디 생성, 회원 존재 여부 확인(return false)");
            String id = "never_used_id";

            given(memberRepository.existsById(id)).willReturn(false);

            // when
            log.info("when - memberService.findByMemberId(id) throws MemberNotFoundException");
            Throwable exception = catchThrowable(() -> memberService.findByMemberId(id));

            // then
            log.info("then - 회원 조회 결과 확인 및 예외 발생 확인");
            log.info("예외 정보: ", exception);
            assertThat(exception).isInstanceOf(RuntimeException.class);
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("회원 전체 조회 테스트")
        void readAllMemberTest() {
            log.info("회원 전체 조회 테스트");
            // given
            log.info("given");

            // when
            log.info("when - memberService.getAll()");
            Page<MemberDTO> result = memberService.getAll(Pageable.ofSize(10));

            // then
            log.info("then - 회원 전체 조회 결과 확인");
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("회원 수정 테스트")
    class UpdateTest{
        @Test
        @DisplayName("회원 수정 테스트")
        void updateMemberTest() {
            log.info("회원 수정 테스트");
            // given

            // when

            // then

        }

        @Test
        @DisplayName("회원 수정 테스트: 존재하지 않는 아이디")
        void updateMemberTestWithNotExistedId() {
            log.info("회원 수정 테스트: 존재하지 않는 아이디");
            // given

            // when

            // then

        }
    }

    @Nested
    @DisplayName("회원 삭제 테스트")
    class DeleteTest{
        @Test
        @DisplayName("회원 삭제 테스트")
        void deleteMemberTest() {
            log.info("회원 삭제 테스트");
            // given
            log.info("given - 존재하는 아이디 생성, 회원 존재 여부 확인(return true)");
            String id = "test_member_id";

            given(memberRepository.existsById(id)).willReturn(true);

            // when
            log.info("when - memberService.deleteById(id)");
            memberService.deleteById(id);

            // then
            log.info("then - 회원 삭제 확인");
            then(memberRepository).should().deleteById(id);
            assertThatCode(() -> memberService.deleteById(id)).doesNotThrowAnyException();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("회원 삭제 테스트: 존재하지 않는 아이디")
        void deleteMemberTestWithNotExistedId() {
            log.info("회원 삭제 테스트: 존재하지 않는 아이디");
            // given
            log.info("given - 존재하지 않는 아이디 생성, 회원 존재 여부 확인(return false)");
            String id = "never_used_id";

            given(memberRepository.existsById(id)).willReturn(false);

            // when
            log.info("when - memberService.deleteById(id) throws MemberNotFoundException");
            Throwable exception = catchThrowable(() -> memberService.deleteById(id));

            // then
            log.info("then - 회원 삭제 결과 확인 및 예외 발생 확인");
            log.info("예외 정보: ", exception);
            then(memberRepository).should(never()).deleteById(id);
            assertThat(exception).isInstanceOf(RuntimeException.class);
            log.info("테스트 종료");
        }
    }
}
