package com.project.clickit.member;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.NoPermissionException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
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

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("isExist Test given: ✔");
            // when
            Boolean result = memberService.isExist(id);

            log.info("isExist Test when: ✔");
            // then
            assertThat(result).isTrue();

            log.info("isExist Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("isExist Test(존재하지 않는 아이디)")
        void isExistTestDuplicated(){
            log.info("isExist Test(존재하지 않는 아이디)");
            // given
            String id = "1234";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("isExist Test(존재하지 않는 아이디) | given: ✔");
            // when
            Boolean result = memberService.isExist(id);

            log.info("isExist Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertThat(result).isFalse();

            log.info("isExist Test(존재하지 않는 아이디) | then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(false);

            given(memberRepository.save(any(MemberEntity.class))).willReturn(BDDMockito.mock(MemberEntity.class));

            log.info("createMemberTest given: ✔");
            // when
            memberService.create(memberDTO);

            log.info("createMemberTest when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().save(any(MemberEntity.class)),
                    () -> assertThatCode(() -> memberService.create(memberDTO)).doesNotThrowAnyException()
            );

            log.info("createMemberTest then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("CreateMember Test(아이디 중복) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> memberService.create(memberDTO));

            log.info("CreateMember Test(아이디 중복) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class),
                    () -> then(memberRepository).shouldHaveNoMoreInteractions()
            );

            log.info("CreateMember Test(아이디 중복) | then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(false);

            given(memberRepository.saveAll(anyList())).willReturn(List.of(new MemberEntity(), new MemberEntity()));

            log.info("CreateList Test given: ✔");
            // when
            memberService.createList(memberDTOList);

            log.info("CreateList Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().saveAll(anyList()),
                    () -> assertThatCode(() -> memberService.createList(memberDTOList)).doesNotThrowAnyException()
            );

            log.info("CreateList Test then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("CreateList Test(아이디 중복) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> memberService.createList(memberDTOList));

            log.info("CreateList Test(아이디 중복) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(DuplicatedIdException.class),
                    () -> then(memberRepository).shouldHaveNoMoreInteractions()
            );

            log.info("CreateList Test(아이디 중복) | then: ✔");
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

            given(memberRepository.findAll(any(Pageable.class))).willReturn(memberEntityPage);

            log.info("getAll Test given: ✔");
            // when
            Page<MemberDTO> result = memberService.getAll(Pageable.ofSize(10));

            log.info("getAll Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> assertThatIterable(result).allMatch(Objects::nonNull)).doesNotThrowAnyException()
            );

            log.info("getAll Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("findByMemberId Test")
        void findByMemberIdTest() {
            log.info("findByMemberId Test");
            // given
            String id = "test_member_id";

            given(memberRepository.existsById(anyString())).willReturn(true);

            given(memberRepository.findById(anyString())).willReturn(new MemberEntity());

            log.info("findByMemberId Test given: ✔");
            // when
            MemberDTO result = memberService.findByMemberId(id);

            log.info("findByMemberId Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(MemberDTO.class),
                    () -> assertThatCode(() -> memberService.findByMemberId(id)).doesNotThrowAnyException()
            );

            log.info("findByMemberId Test then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("findByMemberId Test(존재하지 않는 아이디)")
        void findByMemberIdTestNotFound(){
            log.info("findByMemberId Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("findByMemberId Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> memberService.findByMemberId(id));

            log.info("findByMemberId Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(RuntimeException.class),
                    () -> then(memberRepository).should(never()).findById(id)
            );

            log.info("findByMemberId Test(존재하지 않는 아이디) | then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("findByMemberName Test")
        void findByMemberNameTest(){
            log.info("findByMemberName Test");
            // given
            String name = "test_member_name";

            Page<MemberEntity> memberEntityPage = Page.empty();

            given(memberRepository.findByMemberName(anyString(), any(), any(Pageable.class))).willReturn(memberEntityPage);

            log.info("findByMemberName Test given: ✔");
            // when
            Page<MemberDTO> result = memberService.findByMemberName(name, Pageable.unpaged());

            log.info("findByMemberName Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> memberService.findByMemberName(name, Pageable.ofSize(10))).doesNotThrowAnyException()
            );

            log.info("findByMemberName Test then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("findByDormitoryId Test")
        void findByDormitoryIdTest(){
            log.info("findByDormitoryId Test");
            // given
            String dormitoryId = "test_dormitory_id";

            Page<MemberEntity> memberEntityPage = Page.empty();

            given(memberRepository.findByDormitoryId(anyString(), any(Pageable.class))).willReturn(memberEntityPage);

            log.info("findByDormitoryId Test given: ✔");
            // when
            Page<MemberDTO> result = memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10));

            log.info("findByDormitoryId Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10))).doesNotThrowAnyException()
            );

            log.info("findByDormitoryId Test then: ✔");
        }

        @Test
        @Order(6)
        @DisplayName("findByDormitoryId Test(존재하지 않는 아이디)")
        void findByDormitoryIdTestNotFound(){
            // 존재하지 않는 기숙사 아이디로 조회할 일은 없을 것 같지만 혹시 모르니 테스트 진행
            log.info("findByDormitoryId Test(존재하지 않는 아이디)");
            // given
            String dormitoryId = "never_used_id";

            log.info("findByDormitoryId Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable result = catchThrowable(() -> memberService.findByDormitoryId(dormitoryId, Pageable.ofSize(10)));

            log.info("findByDormitoryId Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isInstanceOf(RuntimeException.class)
            );

            log.info("findByDormitoryId Test(존재하지 않는 아이디) | then: ✔");
        }

        @Test
        @Order(7)
        @DisplayName("getAllByType Test")
        void getAllByTypeTest(){
            log.info("getAllByType Test");
            // given
            Page<MemberEntity> memberEntityPage = Page.empty();

            given(memberRepository.findAllStudent(any(), any(Pageable.class))).willReturn(memberEntityPage);

            log.info("getAllByType Test given: ✔");
            // when
            Page<MemberDTO> result = memberService.getAllStudent(Pageable.ofSize(10));

            log.info("getAllByType Test when: ✔");
            // then
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).isInstanceOf(Page.class),
                    () -> assertThatCode(() -> assertThatIterable(result).allMatch(Objects::nonNull)).doesNotThrowAnyException()
            );

            log.info("getAllByType Test then: ✔");
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
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

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

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("update Test given: ✔");
            // when
            memberService.update(memberDTO);

            log.info("update Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().save(any(MemberEntity.class)),
                    () -> assertThatCode(() -> memberService.update(memberDTO)).doesNotThrowAnyException()
            );

            log.info("update Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("update Test(존재하지 않는 아이디)")
        void updateMemberTestWithNotExistedId() {
            log.info("update Test(존재하지 않는 아이디)");
            // given
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

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

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("update Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable exception = catchThrowable(() -> memberService.update(memberDTO));

            log.info("update Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(ObjectNotFoundException.class),
                    () -> then(memberRepository).shouldHaveNoMoreInteractions()
            );

            log.info("update Test(존재하지 않는 아이디) | then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("updateMemberForStaff Test given: ✔");
            // when
            memberService.updateMemberForStaff(memberDTO);

            log.info("updateMemberForStaff Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()),
                    () -> assertThatCode(() -> memberService.updateMemberForStaff(memberDTO)).doesNotThrowAnyException()
            );

            log.info("updateMemberForStaff Test then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("updateMemberForStaff Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable exception = catchThrowable(() -> memberService.updateMemberForStaff(memberDTO));

            log.info("updateMemberForStaff Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(ObjectNotFoundException.class),
                    () -> then(memberRepository).should(never()).updateMemberForStaff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())
            );

            log.info("updateMemberForStaff Test(존재하지 않는 아이디) | then: ✔");
        }

        @Test
        @Order(5)
        @DisplayName("updatePassword Test")
        void updatePasswordTest(){
            log.info("updatePassword Test");
            // given
            // SecurityContextHolder 사용하도록 변경

            String id = "test_member_id";
            String password = "update_password";

            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(id, "password"));
            SecurityContextHolder.setContext(securityContext);

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("updatePassword Test given: ✔");
            // when
            memberService.updatePassword(password);

            log.info("updatePassword Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().updatePassword(anyString(), anyString()),
                    () -> assertThatCode(() -> memberService.updatePassword(password)).doesNotThrowAnyException()
            );

            log.info("updatePassword Test then: ✔");
        }

        @Test
        @Order(6)
        @DisplayName("updatePassword Test(존재하지 않는 아이디)")
        void updatePasswordTestWithNotExistedId(){
            log.info("updatePassword Test(존재하지 않는 아이디)");
            // given
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("id", "password"));
            SecurityContextHolder.setContext(securityContext);

            String id = "never_used_id";
            String password = "update_password";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("updatePassword Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable exception = catchThrowable(() -> memberService.updatePassword(password));

            log.info("updatePassword Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(ObjectNotFoundException.class),
                    () -> then(memberRepository).should(never()).updatePassword(id, password)
            );

            log.info("updatePassword Test(존재하지 않는 아이디) | then: ✔");
        }

        @Test
        @Order(7)
        @DisplayName("updateRefreshToken Test")
        void updateRefreshTokenTest(){
            log.info("updateRefreshToken Test");
            // given
            String id = "test_member_id";
            String refreshToken = "test_refresh_token";

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("updateRefreshToken Test given: ✔");
            // when
            memberService.updateRefreshToken(id, refreshToken);

            log.info("updateRefreshToken Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().updateRefreshToken(id, refreshToken),
                    () -> assertThatCode(() -> memberService.updateRefreshToken(id, refreshToken)).doesNotThrowAnyException()
            );

            log.info("updateRefreshToken Test then: ✔");
        }

        @Test
        @Order(8)
        @DisplayName("updateRefreshToken Test(존재하지 않는 아이디)")
        void updateRefreshTokenTestWithNotExistedId(){
            log.info("updateRefreshToken Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";
            String refreshToken = "test_refresh_token";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("updateRefreshToken Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable exception = catchThrowable(() -> memberService.updateRefreshToken(id, refreshToken));

            log.info("updateRefreshToken Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(ObjectNotFoundException.class),
                    () -> then(memberRepository).should(never()).updateRefreshToken(id, refreshToken)
            );

            log.info("updateRefreshToken Test(존재하지 않는 아이디) | then: ✔");
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

            given(memberRepository.existsById(anyString())).willReturn(true);

            log.info("deleteById Test given: ✔");
            // when
            memberService.deleteById(id);

            log.info("deleteById Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().deleteById(id),
                    () -> assertThatCode(() -> memberService.deleteById(id)).doesNotThrowAnyException()
            );

            log.info("deleteById Test then: ✔");
        }

        @Test
        @Order(2)
        @DisplayName("deleteById Test(존재하지 않는 아이디)")
        void deleteMemberTestWithNotExistedId() {
            log.info("deleteById Test(존재하지 않는 아이디)");
            // given
            String id = "never_used_id";

            given(memberRepository.existsById(anyString())).willReturn(false);

            log.info("deleteById Test(존재하지 않는 아이디) | given: ✔");
            // when
            Throwable exception = catchThrowable(() -> memberService.deleteById(id));

            log.info("deleteById Test(존재하지 않는 아이디) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(ObjectNotFoundException.class),
                    () -> then(memberRepository).shouldHaveNoMoreInteractions()
            );

            log.info("deleteById Test(존재하지 않는 아이디) | then: ✔");
        }

        @Test
        @Order(3)
        @DisplayName("deleteById Test(학생이 아닌 경우)")
        void deleteByIdTestNotStudent(){
            log.info("deleteById Test(학생이 아닌 경우)");
            // given
            String id = "test_member_id";

            MemberEntity memberEntity = MemberEntity.builder()
                    .id(id)
                    .type("CLICKIT_STAFF")
                    .build();

            given(memberRepository.existsById(anyString())).willReturn(true);

            given(memberRepository.findById(anyString())).willReturn(memberEntity);

            log.info("deleteById Test(학생이 아닌 경우) | given: ✔");
            // when
            Throwable exception = catchThrowable(() -> memberService.deleteById(id));

            log.info("deleteById Test(학생이 아닌 경우) | when: ✔");
            // then
            assertAll(
                    () -> assertThat(exception).isInstanceOf(NoPermissionException.class),
                    () -> then(memberRepository).should(never()).deleteById(id)
            );

            log.info("deleteById Test(학생이 아닌 경우) | then: ✔");
        }

        @Test
        @Order(4)
        @DisplayName("deleteAll Test")
        void deleteAllTest(){
            log.info("deleteAll Test");
            // given

            willDoNothing().given(memberRepository).deleteAllStudent(any());

            log.info("deleteAll Test given: ✔");
            // when
            memberService.deleteAll();

            log.info("deleteAll Test when: ✔");
            // then
            assertAll(
                    () -> then(memberRepository).should().deleteAllStudent(any()),
                    () -> assertThatCode(() -> memberService.deleteAll()).doesNotThrowAnyException()
            );

            log.info("deleteAll Test then: ✔");
        }
    }
}
