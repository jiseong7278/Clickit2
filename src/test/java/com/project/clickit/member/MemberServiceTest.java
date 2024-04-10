package com.project.clickit.member;

import com.project.clickit.dto.MemberDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;
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

    private MemberDTO memberDTO;

    private MemberEntity memberEntity;

    @BeforeEach
    void setUp(){

    }

    @Nested
    @DisplayName("회원 생성 테스트")
    class CreateTest{
        @Test
        @DisplayName("회원 생성 테스트")
        void createMemberTest(){
            log.info("회원 생성 테스트 - 성공");
            log.info("given - 회원 생성, 아이디 중복 체크(return false), 회원 저장(return 회원)");
            // given
            memberDTO = MemberDTO.builder()
                    .id("test_case_id")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test_case_email@test.com")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("STUDENT")
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(false);
//            when(memberRepository.save(any(MemberEntity.class))).then(AdditionalAnswers.returnsFirstArg());
            will(AdditionalAnswers.returnsFirstArg()).given(memberRepository).save(any(MemberEntity.class));

            // when
            log.info("when - 회원 생성(memberService.create(memberDTO))");
            MemberDTO result = memberService.create(memberDTO);

            // then
            log.info("then - 회원 생성 결과 확인");
            assertThat(result.getId()).isEqualTo(memberDTO.getId());
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("회원 생성 테스트: 아이디 중복")
        void createMemberTestWithDuplicatedId() {
            log.info("회원 생성 테스트: 아이디 중복");

            // given
            log.info("given - 회원 생성, 아이디 중복 체크(return true)");
            memberDTO = MemberDTO.builder()
                    .id("test_member_id")
                    .password("test_case_password")
                    .name("test_case")
                    .email("test_case_email@test.com")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("STUDENT")
                    .build();

            given(memberRepository.existsById(memberDTO.getId())).willReturn(true);

            // when
            log.info("when - memberService.create(memberDTO) throws DuplicatedIdException");
            Throwable exception = catchThrowable(() -> memberService.create(memberDTO));

            // then
            log.info("then - 회원 생성 결과 확인 및 예외 발생 확인");
            log.info("예외 정보: ", exception);
            assertThat(exception).isInstanceOf(DuplicatedIdException.class);
            log.info("테스트 종료");
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
            log.info("given - 존재하는 아이디 생성, 회원 존재 여부 확인(return true)");
            String id = "test_member_id";
            memberDTO = MemberDTO.builder()
                    .id(id)
                    .build();

            given(memberRepository.existsById(id)).willReturn(true);
            when(memberRepository.findByMemberId(id)).thenReturn(memberDTO.toEntity());

            // when
            log.info("when - memberService.findByMemberId(id)");
            MemberDTO result = memberService.findByMemberId(id);

            // then
            log.info("then - 회원 조회 결과 확인");
            assertThat(result.getId()).isEqualTo(id);
            log.info("테스트 종료");
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
            log.info("given - 존재하는 아이디 생성, 업데이트 할 memberDTO 생성, 회원 존재 여부 확인(return true)");
            String id = "test_member_id";
            memberDTO = MemberDTO.builder()
                    .id(id)
                    .password("test_case_password")
                    .name("test_case")
                    .email("ttt")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("STUDENT")
                    .build();

            given(memberRepository.existsById(id)).willReturn(true);

            // when
            log.info("when - memberService.findByMemberId(id)");
            memberService.update(memberDTO);

            // then
            log.info("then - 회원 수정 확인");
            then(memberRepository).should().save(memberDTO.toEntity());

            assertThatCode(() -> memberService.update(memberDTO)).doesNotThrowAnyException();
            log.info("테스트 종료");
        }

        @Test
        @DisplayName("회원 수정 테스트: 존재하지 않는 아이디")
        void updateMemberTestWithNotExistedId() {
            log.info("회원 수정 테스트: 존재하지 않는 아이디");
            // given
            log.info("given - 존재하지 않는 아이디 생성, 업데이트 할 memberDTO 생성, 회원 존재 여부 확인(return false)");
            String id = "never_used_id";
            memberDTO = MemberDTO.builder()
                    .id(id)
                    .password("test_case_password")
                    .name("test_case")
                    .email("ttt")
                    .phone("010-1234-5678")
                    .studentNum("20151111")
                    .type("STUDENT")
                    .build();

            given(memberRepository.existsById(id)).willReturn(false);

            // when
            log.info("when - memberService.updateMember(memberDTO) throws MemberNotFoundException");
            Throwable exception = catchThrowable(() -> memberService.update(memberDTO));

            // then
            log.info("then - 회원 수정 결과 확인 및 예외 발생 확인");
            log.info("예외 정보: ", exception);
            assertThat(exception).isInstanceOf(RuntimeException.class);
            log.info("테스트 종료");
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
