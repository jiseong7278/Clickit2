package com.project.clickit.service;

import com.project.clickit.dto.MemberDTO;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.login.MemberNotFoundException;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    final MemberRepository memberRepository;
    final JwtProvider jwtProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    /**
     * <b>해당 id의 회원이 존재하는지 확인</b><br>(존재한다면 true, 존재하지 않는다면 false)
     * @param id String
     * @return Boolean
     */
    public Boolean isExist(String id) {
        return memberRepository.existsById(id);
    }

    // ========== Create ========== //
    /**
     * <b>회원 생성</b>
     * @param memberDTO MemberDTO
     * @return MemberDTO
     */
    @Transactional
    public MemberDTO create(MemberDTO memberDTO) {
        if (isExist(memberDTO.getId())) {
            throw new DuplicatedIdException();
        }
        return memberRepository.save(memberDTO.toEntity()).toDTO();
    }

    /**
     * <b>List로 회원 생성</b>
     * @param MemberDTOList List&lt;MemberDTO&gt;
     */
    @Transactional
    public void createList(List<MemberDTO> MemberDTOList) {
        for(MemberDTO memberDTO : MemberDTOList){
            if (isExist(memberDTO.getId())) {
                throw new DuplicatedIdException();
            }
        }
        memberRepository.saveAll(toEntityList(MemberDTOList));
    }


    // ========== Read ========== //
    /**
     * <b>모든 회원 조회</b>
     * @param pageable Pageable
     * @return Page&lt;MemberDTO&gt;
     */
    @Transactional
    public Page<MemberDTO> getAll(Pageable pageable) {
        return toDTOPage(memberRepository.findAll(pageable));
    }

    /**
     * <b>id로 회원 조회</b>
     * @param id String
     * @return MemberDTO
     */
    @Transactional
    public MemberDTO findByMemberId(String id) {
        if (!isExist(id)) {
            throw new MemberNotFoundException();
        }
        return memberRepository.findById(id).toDTO();
    }

    /**
     * <b>이름으로 회원 조회</b>
     * @param name String
     * @param pageable Pageable
     * @return Page&lt;MemberDTO&gt;
     */
    @Transactional
    public Page<MemberDTO> findByMemberName(String name, Pageable pageable) {
        return toDTOPage(memberRepository.findByMemberName(name, pageable));
    }

    /**
     * <b>비밀번호 찾기</b>
     * @param id String
     * @return String password
     */
    @Transactional
    public String findPasswordByMemberId(String id) {
        if (!isExist(id))
            throw new MemberNotFoundException();
        return memberRepository.findPasswordByMemberId(id);
    }

    /**
     * <b>기숙사 아이디로 회원 조회</b>
     * @param dormitoryId String
     * @param pageable Pageable
     * @return Page&lt;MemberDTO&gt;
     */
    @Transactional
    public Page<MemberDTO> findByDormitoryId(String dormitoryId, Pageable pageable) {
        return toDTOPage(memberRepository.findByDormitoryId(dormitoryId, pageable));
    }

    // ========== Update ========== //
    /**
     * <b>학생이 본인 정보 업데이트</b>
     * @param memberDTO MemberDTO
     */
    @Transactional
    public void update(MemberDTO memberDTO){
        if (!isExist(memberDTO.getId()))
            throw new MemberNotFoundException();
        memberRepository.save(memberDTO.toEntity());
    }

    /**
     * <b>직원이 회원 정보 업데이트</b>
     * @param memberDTO MemberDTO
     */
    @Transactional
    public void updateMemberForStaff(MemberDTO memberDTO){
        if (!isExist(memberDTO.getId()))
            throw new MemberNotFoundException();
        memberRepository.updateMemberForStaff(memberDTO.getId(),
                memberDTO.getPassword(),
                memberDTO.getName(),
                memberDTO.getEmail(),
                memberDTO.getPhone(),
                memberDTO.getStudentNum(),
                memberDTO.getType(),
                memberDTO.getDormitoryDTO().getId());
    }

    /**
     * <b>비밀번호 업데이트</b>
     * @param id String
     * @param password String
     */
    @Transactional
    public void updatePassword(String id, String password) {
        if (!isExist(id))
            throw new MemberNotFoundException();
        memberRepository.updatePassword(id, password);
    }

    /**
     * <b>Refresh Token 업데이트</b>
     * @param id String
     * @param refreshToken String
     */
    @Transactional
    public void updateRefreshToken(String id, String refreshToken) {
        if (!isExist(id))
            throw new MemberNotFoundException();
        memberRepository.updateRefreshToken(id, refreshToken);
    }

    // ========== Delete ========== //
    /**
     * <b>회원 삭제</b>
     * @param id String
     */
    @Transactional
    public void deleteById(String id) {
        if (!isExist(id))
            throw new MemberNotFoundException();
        memberRepository.deleteById(id);
    }

    /**
     * <b>List&lt;MemberEntity&gt;를 List&lt;MemberDTO&gt;로 변환</b>
     * @param memberDTOList List&lt;MemberDTO&gt;
     * @return List&lt;MemberEntity&gt;
     */
    private List<MemberEntity> toEntityList(List<MemberDTO> memberDTOList) {
        List<MemberEntity> memberEntityList = new ArrayList<>();
        for (MemberDTO memberDTO : memberDTOList) {
            memberEntityList.add(memberDTO.toEntity());
        }
        return memberEntityList;
    }

    /**
     * <b>Page&lt;MemberEntity&gt;를 Page&lt;MemberDTO&gt;로 변환</b>
     * @param memberEntityPage Page&lt;MemberEntity&gt;
     * @return Page&lt;MemberDTO&gt;
     */
    private Page<MemberDTO> toDTOPage(Page<MemberEntity> memberEntityPage) {
        return memberEntityPage.map(MemberEntity::toDTO);
    }
}
