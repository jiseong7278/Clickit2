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
import com.project.clickit.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LoginService {
    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final RedisService redisService;

    private final SmsUtil smsUtil;

    @Value("${roles.student}")
    private String TYPE_STUDENT;

    @Autowired
    public LoginService(MemberRepository memberRepository, JwtProvider jwtProvider, RedisService redisService, SmsUtil smsUtil){
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.redisService = redisService;
        this.smsUtil = smsUtil;
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
                memberEntity.setPassword(passwordEncoder.encode(memberEntity.getPassword()));

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
        if (!isExist(loginDTO.getId())) throw new InvalidIdException();

        MemberEntity memberEntity = memberRepository.findById(loginDTO.getId());

        if (!passwordEncoder.matches(loginDTO.getPassword(), memberEntity.getPassword())) throw new InvalidPasswordException();

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
    }

    /**
     * <b>문자로 인증번호 전송</b>
     * @param id String
     */
    @Transactional
    public void sendVerifyCodeBySMS(String id){
        if(!isExist(id)) throw new InvalidIdException();

        MemberEntity memberEntity = memberRepository.findById(id);
        memberEntity.setPhone(memberEntity.getPhone().replaceAll("-", ""));

        String verifyCode = generateVerifyCode();

        smsUtil.sendOne(memberEntity.getPhone(), verifyCode);

        redisService.setData(memberEntity.getPhone(), verifyCode, Duration.ofMinutes(3));
    }

    /**
     * <b>비밀번호 찾기</b>
     * @param phone String
     * @return String
     */
    public String findPasswordByPhone(String phone){
        return memberRepository.findByPhone(phone);
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

    /**
     * <b>인증번호 생성</b>
     * @return String
     */
    private String generateVerifyCode(){
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        return String.format("%06d", random.nextInt(1000000));
    }

    /**
     * <b>인증번호 확인</b>
     * @param key String 이메일 또는 전화번호
     * @param verifyCode String
     * @return 사용자가 입력한 코드와 Redis에 저장된 코드가 같을 경우 {@code true}, 그 외 {@code false}
     */
    public Boolean verification(String key, String verifyCode){
        String code = redisService.getData(key);
        return code != null && code.equals(verifyCode);
    }
}