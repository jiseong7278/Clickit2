package com.project.clickit.service;

import com.project.clickit.dto.MemberDTO;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.login.MemberNotFoundException;
import com.project.clickit.repository.MemberRepository;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public MemberDTO create(MemberDTO memberDTO) {
        if(memberRepository.existsById(memberDTO.getId())){
            throw new DuplicatedIdException();
        }
        return memberRepository.save(memberDTO.toEntity()).toDTO();
    }

    @Transactional
    public void createList(List<MemberDTO> MemberDTOList) {
        memberRepository.saveAll(toEntityList(MemberDTOList));
    }

    @Transactional
    public MemberDTO findByMemberId(String id) {
        if(!memberRepository.existsById(id)){
            throw new MemberNotFoundException();
        }
        return memberRepository.findByMemberId(id).toDTO();
    }

    @Transactional
    public List<MemberDTO> getAll() {
        return toDTOList(memberRepository.findAll());
    }

    @Transactional
    public void updateMember(MemberDTO memberDTO) {
        if(!memberRepository.existsById(memberDTO.getId())){
            throw new MemberNotFoundException();
        }
        memberRepository.updateMember(memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName(), memberDTO.getEmail(), memberDTO.getPhone(), memberDTO.getStudentNum(), memberDTO.getType());
    }

    @Transactional
    public void deleteById(String id) {
        if(!memberRepository.existsById(id)){
            throw new MemberNotFoundException();
        }
        memberRepository.deleteById(id);
    }

    public List<MemberDTO> toDTOList(List<MemberEntity> memberEntityList) {
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(memberEntity.toDTO());
        }
        return memberDTOList;
    }

    public List<MemberEntity> toEntityList(List<MemberDTO> memberDTOList) {
        List<MemberEntity> memberEntityList = new ArrayList<>();
        for (MemberDTO memberDTO : memberDTOList) {
            memberEntityList.add(memberDTO.toEntity());
        }
        return memberEntityList;
    }
}
