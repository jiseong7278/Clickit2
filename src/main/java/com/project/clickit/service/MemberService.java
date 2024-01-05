package com.project.clickit.service;

import com.project.clickit.repository.MemberRepository;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<MemberEntity> getAll() {
        return memberRepository.getAll();
    }

    @Transactional
    public MemberEntity create(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity);
    }

    @Transactional
    public void createList(List<MemberEntity> memberEntityList) {
        memberRepository.saveAll(memberEntityList);
    }

}
