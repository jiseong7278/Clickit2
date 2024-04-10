package com.project.clickit.jwt;

import com.project.clickit.exceptions.jwt.*;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtProvider{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_SUBJECT_ACCESS = "access";
    private static final String TOKEN_SUBJECT_REFRESH = "refresh";
    private static final String AUTHORITIES_KEY = "auth";

    private final UserDetailsService userDetailsService;

    public JwtProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * <b>Create access token</b>
     * @param memberId String
     * @param roles List&lt;String&gt;
     * @return String
     */
    public String createAccessToken(String memberId, List<String> roles){

        Claims claims = Jwts.claims().setIssuer(issuer);
        claims.setSubject(TOKEN_SUBJECT_ACCESS);
        claims.put("roles", roles);
        claims.put("memberId", memberId);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofHours(2).toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * <b>Create refresh token</b>
     * @param memberId String
     * @param roles List&lt;String&gt;
     * @return String
     */
    public String createRefreshToken(String memberId, List<String> roles){

        Claims claims = Jwts.claims().setIssuer(issuer);
        claims.setSubject(TOKEN_SUBJECT_REFRESH);
        claims.put("roles", roles);
        claims.put("memberId", memberId);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofHours(24).toMillis());

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * <b>Get Authentication (Username Password Authentication Token including userDetails and users authorities)</b>
     * @param token String
     * @return Authentication
     */
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getMemberId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * <b>Get member name from token</b>
     * @param token String
     * @return String
     */
    public String getMemberId(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("memberId", String.class);
    }

    /**
     * <b>Get issuer from token</b>
     * @param token String
     * @return String
     */
    public String getIssuer(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getIssuer();
    }

    /**
     * <b>Get subject from token</b>
     * @param token String
     * @return String
     */
    public String getSubject(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }


    public List<String> getRoles(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("roles", List.class);
    }

    /**
     * <b>토큰 유효성 검사</b><br>throw exceptions when token is invalid<br>invalid token: expired, invalid issuer, invalid signature, invalid token, unsupported token
     * @param token String
     * @return boolean
     */
    public boolean validateToken(String token){
        if(token == null || token.isEmpty()) throw new TokenNotFoundException();
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if(!claims.getBody().getIssuer().equals(issuer)) throw new InvalidIssuerException();
            return true;
        }catch (SignatureException e){
            throw new InvalidSignatureException();
        }catch(ExpiredJwtException e){
            throw new ExpiredTokenException();
        }catch(UnsupportedJwtException e){
            throw new UnsupportedTokenException();
        }catch(IllegalArgumentException e){
            throw new IllegalTokenException();
        }catch(Exception e){
            throw new UnexpectedTokenException();
        }
    }

    /**
     * <b>헤더로부터 토큰 추출</b>
     * @param req HttpServletRequest
     * @return String
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * <b>토큰 추출</b>
     * @param token String
     * @return String
     */
    public String resolveToken(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

}
