package com.project.clickit.member.service;

import com.project.clickit.member.domain.entity.MemberEntity;
import com.project.clickit.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity getAll() {
        try {
            List<MemberEntity> memberEntityList = memberRepository.getAll();
            return ResponseEntity.ok(memberEntityList);
        } catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity create(MemberEntity memberEntity) {
        try {
            memberRepository.save(memberEntity);
            return ResponseEntity.ok(memberEntity);
        } catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity createList(List<MemberEntity> memberEntityList) {
        try {
            memberRepository.saveAll(memberEntityList);
            return ResponseEntity.ok(memberEntityList);
        } catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }

    private ResponseEntity errorResponse(String message) {
        return new ResponseEntity(message, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST);
    }
}
