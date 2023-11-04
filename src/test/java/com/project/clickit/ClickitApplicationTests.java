package com.project.clickit;

import com.project.clickit.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class ClickitApplicationTests {

	@Autowired
	JwtService jwtService;

	Date date;

	@Test
	void contextLoads() {
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
	void getYesterday(){
		date = new Date();
		System.out.println(date);
		Date yesterday = new Date(date.getTime() - (1000 * 60 * 60 * 24 * 2));
		System.out.println(yesterday);
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
}
