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

@RequiredArgsConstructor
@Service
public class MemberService {
    final MemberRepository memberRepository;

    @Autowired
    final JwtProvider jwtProvider;
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

    @Transactional
    public ResponseEntity loginPost(HttpHeaders httpHeaders, MemberEntity memberEntity) {
        String responseBody = null;
        HttpHeaders headers = new HttpHeaders();

        // 아이디 존재 여부 확인
        MemberEntity dbMemberEntity = memberRepository.findByMemberId(memberEntity.getId());
        if(dbMemberEntity == null) {
            return errorResponse("아이디가 존재하지 않습니다.");
        }

        // 변수 선언
        String dbPassword = dbMemberEntity.getPassword();
        String dbRefreshToken = dbMemberEntity.getRefreshToken();
        String createdRefreshToken = null;
        String createdAccessToken = null;

        // 비밀번호 일치 여부 확인
        if(!dbPassword.equals(memberEntity.getPassword())) {
            return errorResponse("비밀번호가 일치하지 않습니다.");
        }

        // refresh token 검증 (내용 및 만료 여부)
        /*if(jwtProvider.validateToken(httpHeaders.get("Authorization").get(0), false)) {
            // 재발급
            createdRefreshToken = jwtProvider.createToken(dbMemberEntity.getMemberNum(), dbMemberEntity.getName(), dbMemberEntity.getType(), false);
            headers.add("Authorization", "Bearer " + createdRefreshToken);

            // db에 refresh token 저장
            try{
                memberRepository.updateRefreshToken(createdRefreshToken, memberEntity.getId());
            }catch (Exception e){
                return errorResponse(e.getMessage());
            }
        }
        createdAccessToken = jwtProvider.createToken(dbMemberEntity.getMemberNum(), dbMemberEntity.getName(), dbMemberEntity.getType(), true);
        headers.add("Authorization", "Bearer " + createdAccessToken);*/

        // 로그인 성공
        // 밑에 코드는 수정
        return ResponseEntity.ok().headers(headers).body("로그인 성공");
    }

    public ResponseEntity loginPost(MemberEntity memberEntity){
        // 아이디와 비밀번호가 null인지 확인
        if(memberEntity.getId() == null || memberEntity.getPassword() == null) return errorResponse("아이디와 비밀번호를 입력해주세요.");

        MemberEntity dbMemberEntity = memberRepository.findByMemberId(memberEntity.getId());

        // 일치하는 아이디가 존재하지 않거나 비밀번호가 일치하지 않으면 에러 메시지 반환
        if(dbMemberEntity == null || !dbMemberEntity.getPassword().equals(memberEntity.getPassword())) return errorResponse("아이디 또는 비밀번호가 일치하지 않습니다.");

        return ResponseEntity.ok().body("로그인 성공");
    }

    @Transactional
    public String getPasswordFromDB(String memberId) {
        return memberRepository.findPasswordByMemberId(memberId);
    }

    private ResponseEntity errorResponse(String message) {
        return new ResponseEntity(message, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST);
    }
}
