package com.project.clickit.service;

import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
import com.project.clickit.dto.TokenDTO;
import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.login.ConcurrentlySignUpException;
import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.InvalidIdException;
import com.project.clickit.exceptions.login.InvalidPasswordException;
import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Value("${roles.student}")
    private String TYPE_STUDENT;

    @Autowired
    public LoginService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    /**
     * <b>해당 id의 회원이 존재하는지 확인</b><br>(존재한다면 true, 존재하지 않는다면 false)
     * @param id String
     * @return Boolean
     */
    @Transactional
    public Boolean isExist(String id) {
        return memberRepository.existsById(id);
    }

    /**
     * <b>회원가입</b>
     * @param memberDTO MemberDTO
     * @param role String
     * @return TokenDTO
     */
    @Transactional
    public TokenDTO signUp(MemberDTO memberDTO, String role){ // 매개변수에 Type 추가 and accessToken, refreshToken에서 Collections.singletonList에 Type(매개변수)로 변경
        ReentrantLock lock = lockMap.computeIfAbsent(memberDTO.getId(), key -> new ReentrantLock());

        if(!lock.tryLock()){
            throw new ConcurrentlySignUpException();
        }

        try{
            if (isExist(memberDTO.getId())) {
                throw new DuplicatedIdException();
            }else{
                MemberEntity memberEntity = memberDTO.toEntity();
                memberEntity.setType(role);

                String accessToken = jwtProvider.createAccessToken(memberEntity.getId(), Collections.singletonList(role));
                String refreshToken = jwtProvider.createRefreshToken(memberEntity.getId(), Collections.singletonList(role));

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

    /**
     * <b>로그인</b>
     * @param loginDTO LoginDTO
     * @return TokenDTO
     */
    @Transactional
    public TokenDTO signIn(LoginDTO loginDTO){
        if(isExist(loginDTO.getId())){
            MemberEntity memberEntity = memberRepository.findById(loginDTO.getId());

            if(memberEntity.getPassword().equals(loginDTO.getPassword())){
                String accessToken = jwtProvider.createAccessToken(memberEntity.getId(), Collections.singletonList(memberEntity.getType()));
                String refreshToken = jwtProvider.createRefreshToken(memberEntity.getId(), Collections.singletonList(memberEntity.getType()));

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

    /**
     * <b>로그아웃</b>
     * @param token String
     */
    public void logout(String token){
        String resolvedToken = jwtProvider.resolveToken(token);
        if (jwtProvider.validateToken(resolvedToken)){
            SecurityContextHolder.clearContext();
        }
    }
}