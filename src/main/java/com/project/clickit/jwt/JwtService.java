package com.project.clickit.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.access-secret-key}")
    private String accessSecretKey;

    @Value("${jwt.refresh-secret-key}")
    private String refreshSecretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.subject}")
    private String subject;

    /**
     * 토큰 생성
     * @param memberNum
     * @param name
     * @param type (DEV, STAFF, MEMBER)
     * @param isAccess (true: access, false: refresh)
     * @return
     */
    public String createToken(Integer memberNum, String name, String type, Boolean isAccess){
        Date now = new Date();
        Date expiration;
        String key;
        if(isAccess) {
            expiration = new Date(now.getTime() + Duration.ofHours(2).toMillis());
            key = accessSecretKey;
        }else{
            expiration = new Date(now.getTime() + Duration.ofDays(30).toMillis());
            key = refreshSecretKey;
        }
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer) // 토큰 발급자
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiration) // 토큰 만료 시간
                .setSubject(subject) // 토큰 제목
                .claim("memberNum", memberNum) // 사용자 정보 (번호)
                .claim("name", name) // 사용자 정보 (이름)
                .claim("type", type) // 사용자 정보 (타입)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(key.getBytes())) // 암호화
                .compact();
    }

    // 토큰 만료 테스트용 삭제 요망
    public String createTokenExpired(Integer memberNum, String name, String type, Boolean isAccess){
        Date now = new Date();
        Date expiration;
        Date yesterday = new Date(now.getTime() - (1000 * 60 * 60 * 24));
        String key;
        if(isAccess) {
            expiration = new Date(yesterday.getTime() - Duration.ofHours(2).toMillis());
            key = accessSecretKey;
        }else{
            expiration = new Date(yesterday.getTime() - Duration.ofHours(4).toMillis());
            key = refreshSecretKey;
        }
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer) // 토큰 발급자
                .setIssuedAt(yesterday) // 토큰 발급 시간
                .setExpiration(expiration) // 토큰 만료 시간
                .setSubject(subject) // 토큰 제목
                .claim("memberNum", memberNum) // 사용자 정보 (번호)
                .claim("name", name) // 사용자 정보 (이름)
                .claim("type", type) // 사용자 정보 (타입)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(key.getBytes())) // 암호화
                .compact();
    }

    /**
     * 토큰 검증. 발급자, 제목, 만료 여부 확인 <br>토큰이 유효하면 true<br>유효하지 않으면 false
     * @param token
     * @param isAccess
     * @return boolean
     */
    public boolean validateToken(String token, boolean isAccess){ // 토큰 검증
        //토큰이 없거나, Bearer로 시작하지 않으면 false
        if(token == null || token.equals("")) return false;
        if(!token.startsWith("Bearer ")) return false;

        token = bearerToken(token);
        Jws<Claims> claims = decodeToken(token, isAccess);

        //발급자와 제목이 일치하는지 확인
        if(!claims.getBody().getIssuer().equals(issuer) || !claims.getBody().getSubject().equals(subject)) return false;

        //토큰 만료 여부 확인
        try{
            return isTokenExpired(token, isAccess);
        }catch(JwtException e){
            throw new JwtException("토큰 검증 실패");
        }
    }

    private String bearerToken(String token){
        return token.substring("Bearer ".length());
    }

    /**
     * <b>토큰 만료 여부 확인</b><br>토큰이 만료되었으면 true<br>만료되지 않았으면 false
     * @param token
     * @param isAccess
     * @return boolean
     */
    public boolean isTokenExpired(String token, boolean isAccess){ // 토큰 만료 여부 확인
        String key;

        if(isAccess) key = accessSecretKey;
        else key = refreshSecretKey;

        try{
            if(token.startsWith("Bearer "))  token = bearerToken(token);
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(key.getBytes()))
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        }catch(ExpiredJwtException e){
            return true;
        }catch(Exception e){
            throw new JwtException("토큰 검증 실패");
        }
    }

    public Jws<Claims> decodeToken(String token, Boolean isAccess){
        token = bearerToken(token);
        String key;
        if(isAccess) {
            key = accessSecretKey;
        }else{
            key = refreshSecretKey;
        }
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(key.getBytes()))
                .parseClaimsJws(token);
    }
}
