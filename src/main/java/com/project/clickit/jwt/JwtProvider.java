package com.project.clickit.jwt;

import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.jwt.*;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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
     * @param memberName String
     * @param roles List<String>
     * @return String
     */
    public String createAccessToken(String memberName, List<String> roles){

        Claims claims = Jwts.claims().setIssuer(issuer);
        claims.setSubject(TOKEN_SUBJECT_ACCESS);
        claims.put("roles", roles);
        claims.put("memberName", memberName);

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
     * @param memberName String
     * @param roles List<String>
     * @return String
     */
    public String createRefreshToken(String memberName, List<String> roles){

        Claims claims = Jwts.claims().setIssuer(issuer);
        claims.setSubject(TOKEN_SUBJECT_REFRESH);
        claims.put("roles", roles);
        claims.put("memberName", memberName);

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
        UserDetails userDetails = userDetailsService.loadUserByUsername(getMemberName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * <b>Get member name from token</b>
     * @param token String
     * @return String
     */
    public String getMemberName(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("memberName", String.class);
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

    /**
     * <b>Get roles from token</b>
     * @param token String
     * @return List<String>
     */
    public List<String> getRoles(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("roles", List.class);
    }

    /**
     * <b>Validate token</b><br>throw exceptions when token is invalid<br>invalid token: expired, invalid issuer, invalid signature, invalid token, unsupported token
     * @param token String
     * @return boolean
     */
    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if(!claims.getBody().getIssuer().equals(issuer)) throw new InvalidIssuerException(ErrorCode.INVALID_ISSUER);
            return true;
        }catch (SignatureException e){
            throw new InvalidSignatureException(ErrorCode.INVALID_SIGNATURE_TOKEN);
        }catch(ExpiredJwtException e){
            throw new ExpiredTokenException(ErrorCode.EXPIRED_TOKEN);
        }catch(UnsupportedJwtException e){
            throw new UnsupportedTokenException(ErrorCode.UNSUPPORTED_TOKEN);
        }catch(IllegalArgumentException e){
            throw new IllegalTokenException(ErrorCode.ILLEGAL_TOKEN);
        }catch(Exception e){
            throw new UnexpectedTokenException(ErrorCode.UNEXPECTED_TOKEN_ERROR);
        }
    }

    /**
     * <b>Resolve token from request header</b>
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

}
