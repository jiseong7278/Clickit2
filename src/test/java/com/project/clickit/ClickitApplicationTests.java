package com.project.clickit;

import com.project.clickit.jwt.JwtService;
import com.project.clickit.member.domain.entity.MemberEntity;
import com.project.clickit.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class ClickitApplicationTests {

	@Autowired
	MemberService memberService;

	@Autowired
	JwtService jwtService;

	Date date;

	@Test
	void contextLoads() {
	}

	@Test
	void createToken(){
		String accessToken = jwtService.createToken(1, "test", "test", true);
		String refreshToken = jwtService.createToken(1, "test", "test", false);

		System.out.println("AccessToken: "+accessToken);
		System.out.println("RefreshToken: "+refreshToken);
	}

	@Test
	void tokenValidation(){

		// 기본 토큰
		String accessToken = jwtService.createToken(1, "test", "test", true);
		String refreshToken = jwtService.createToken(1, "test", "test", false);

		// 올바른 토큰
		String bearerAccessToken = "Bearer "+accessToken; // "bearer
		String bearerRefreshToken = "Bearer "+refreshToken; // "bearer

		// 존재하지 않는 토큰
		String nullToken = null;
		String emptyToken = "";

		// 기간 만료된 토큰
		String expiredAccessToken = jwtService.createTokenExpired(1, "test", "test", true);
		String expiredRefreshToken = jwtService.createTokenExpired(1, "test", "test", false);

		// 잘못된 Issuer 토큰
		String wrongIssuerAccessToken = jwtService.createTokenWrongIssuer(1, "test", "test", true);
		String wrongIssuerRefreshToken = jwtService.createTokenWrongIssuer(1, "test", "test", false);

		// 잘못된 Subject 토큰
		String wrongSubjectAccessToken = jwtService.createTokenWrongSubject(1, "test", "test", true);
		String wrongSubjectRefreshToken = jwtService.createTokenWrongSubject(1, "test", "test", false);

		// Access 토큰 검증
		// 순서: 올바른, 기본(Bearer 없음), 존재하지 않는, 기간 만료된, 잘못된 Issuer, 잘못된 Subject
		System.out.println("Access 토큰 검증");
		System.out.println("올바른 토큰 검증: "+jwtService.validateToken(bearerAccessToken, true));
		System.out.println("기본 토큰 검증: "+jwtService.validateToken(accessToken, true));
		System.out.println("존재하지 않는 토큰 검증: "+jwtService.validateToken(nullToken, true));
		System.out.println("존재하지 않는 토큰 검증2: "+jwtService.validateToken(emptyToken, true));
		System.out.println("기간 만료된 토큰 검증: "+jwtService.validateToken(expiredAccessToken, true));
		System.out.println("잘못된 Issuer 토큰 검증: "+jwtService.validateToken(wrongIssuerAccessToken, true));
		System.out.println("잘못된 Subject 토큰 검증: "+jwtService.validateToken(wrongSubjectAccessToken, true));

		// Refresh 토큰 검증
		// 순서: 올바른, 기본(Bearer 없음), 존재하지 않는, 기간 만료된, 잘못된 Issuer, 잘못된 Subject
		System.out.println("Refresh 토큰 검증");
		System.out.println("올바른 토큰 검증: "+jwtService.validateToken(bearerRefreshToken, false));
		System.out.println("기본 토큰 검증: "+jwtService.validateToken(refreshToken, false));
		System.out.println("존재하지 않는 토큰 검증: "+jwtService.validateToken(nullToken, false));
		System.out.println("존재하지 않는 토큰 검증2: "+jwtService.validateToken(emptyToken, false));
		System.out.println("기간 만료된 토큰 검증: "+jwtService.validateToken(expiredRefreshToken, false));
		System.out.println("잘못된 Issuer 토큰 검증: "+jwtService.validateToken(wrongIssuerRefreshToken, false));

	}

	@Test
	void getTokenInfo(){
		String AccessToken = jwtService.createToken(1, "test", "test", true);
		AccessToken = "Bearer "+AccessToken; // "bearer"
		System.out.println("Created Token: "+AccessToken);
		System.out.println("Decoded Token: "+jwtService.decodeToken(AccessToken, true).getBody().get("memberNum"));

		String RefreshToken = jwtService.createToken(3, "test", "test", false);
		RefreshToken = "Bearer "+RefreshToken; // "bearer"
		System.out.println("Created Token: "+RefreshToken);
		System.out.println("Decoded Token: "+jwtService.decodeToken(RefreshToken, false).getBody().get("memberNum"));
//		System.out.println(jwtService.getHeader(token));
//		System.out.println();
//		System.out.println(jwtService.parseToken(token));
	}

	@Test
	void tokenValidationTest(){
		String token = jwtService.createToken(1, "test", "test", true);
		token = "Bearer "+token; // "bearer
		System.out.println("Created Token: "+token);
//		System.out.println("Decoded Token: "+jwtService.decodeToken(token, true));

		String token2 = jwtService.createTokenExpired(3, "expired", "expired", true);
		token2 = "Bearer "+token2; // "bearer
		System.out.println("Created Token: "+token2);
//		System.out.println("Decoded Token: "+jwtService.decodeToken(token2, true));

		System.out.println("Is Token Expired: "+jwtService.isTokenExpired(token, true));
		System.out.println("Is Token2 Expired : "+jwtService.isTokenExpired(token2, true));
	}

	@Test
	void loginTest(){
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setId("test2");
		memberEntity.setPassword("test2UserPassword");
//		System.out.println(memberService.login(memberEntity).getHeaders().get("Authorization"));
//		System.out.println(memberService.login(memberEntity).getRefreshToken());
//		memberEntity.setId("error_id");
//		System.out.println(memberService.login(memberEntity).getPassword());
	}

	@Test
	void findPasswordByMemberIdTest(){
		System.out.println(memberService.findPasswordByMemberId("test2"));
		System.out.println(memberService.findPasswordByMemberId("error_id"));
	}
}
