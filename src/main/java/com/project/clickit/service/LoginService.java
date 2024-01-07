package com.project.clickit.service;

import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.login.ConcurrentlySignUpException;
import com.project.clickit.exceptions.login.DuplicatedIdException;
import com.project.clickit.exceptions.login.InvalidIdException;
import com.project.clickit.exceptions.login.InvalidPasswordException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    private LoginDTO loginDTO;

    @Autowired
    public LoginService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public Boolean duplicateCheck(String id) {
        return memberRepository.existsById(id);
    }

    @Transactional
    public TokenDTO signUp(MemberDTO memberDTO){
        ReentrantLock lock = lockMap.computeIfAbsent(memberDTO.getId(), key -> new ReentrantLock());

        if(!lock.tryLock()){
            throw new ConcurrentlySignUpException();
        }

        try{
            if (duplicateCheck(memberDTO.getId())) {
                throw new DuplicatedIdException();
            }else{
                MemberEntity memberEntity = MemberEntity.builder()
                        .id(memberDTO.getId())
                        .password(memberDTO.getPassword())
                        .name(memberDTO.getName())
                        .email(memberDTO.getEmail())
                        .phone(memberDTO.getPhone())
                        .studentNum(memberDTO.getStudentNum())
                        .type("STUDENT")
                        .build();

                String accessToken = jwtProvider.createAccessToken(memberEntity.getId(), Collections.singletonList("STUDENT"));
                String refreshToken = jwtProvider.createRefreshToken(memberEntity.getId(), Collections.singletonList("STUDENT"));

                memberEntity.setRefreshToken(refreshToken);

                memberRepository.save(memberEntity);

                return TokenDTO.builder()
                        .id(memberEntity.getId())
                        .password(memberEntity.getPassword())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }finally {
            lock.unlock();
        }
    }

    @Transactional
    public TokenDTO signIn(LoginDTO loginDTO){
        this.loginDTO = loginDTO;
        Boolean isExist = duplicateCheck(loginDTO.getId());
        if(isExist){
            MemberEntity memberEntity = memberRepository.findById(loginDTO.getId());

            if(memberEntity.getPassword().equals(loginDTO.getPassword())){
                String accessToken = jwtProvider.createAccessToken(memberEntity.getId(), Collections.singletonList("STUDENT"));
                String refreshToken = jwtProvider.createRefreshToken(memberEntity.getId(), Collections.singletonList("STUDENT"));

                memberEntity.setRefreshToken(refreshToken);

                memberRepository.save(memberEntity);

                return TokenDTO.builder()
                        .id(memberEntity.getId())
                        .password(memberEntity.getPassword())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }else{
                throw new InvalidPasswordException();
            }
        }else{
            throw new InvalidIdException();
        }
    }
}