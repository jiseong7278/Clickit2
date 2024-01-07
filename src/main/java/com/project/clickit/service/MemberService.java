package com.project.clickit.service;

import com.project.clickit.dto.MemberDTO;
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
    public List<MemberDTO> getAll() {
        return toDTOList(memberRepository.findAll());
    }

    @Transactional
    public MemberDTO create(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity).toDTO();
    }

    @Transactional
    public void createList(List<MemberEntity> memberEntityList) {
        memberRepository.saveAll(memberEntityList);
    }

    @Transactional
    public MemberDTO findByMemberId(String id) {
        return memberRepository.findByMemberId(id).toDTO();
    }

    public List<MemberDTO> toDTOList(List<MemberEntity> memberEntityList) {
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(memberEntity.toDTO());
        }
        return memberDTOList;
    }
}
