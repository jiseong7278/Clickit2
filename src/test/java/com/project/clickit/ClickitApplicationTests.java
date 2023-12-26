package com.project.clickit;

import com.project.clickit.jwt.JwtProvider;
import com.project.clickit.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ClickitApplicationTests {

	@Mock
	MemberService memberService;

	@Mock
	JwtProvider jwtProvider;

	Date date;

	HttpServletRequest request;
	HttpServletResponse response;
	FilterChain filterChain;

	String accessToken;
	String refreshToken;

	@BeforeEach
	void init(){
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		filterChain = Mockito.mock(FilterChain.class);
	}

	@BeforeEach
	void createToken(){
//		accessToken = jwtProvider.createToken(1, "test", "test", true);
//		refreshToken = jwtProvider.createToken(1, "test", "test", false);
	}

	@Test
	@DisplayName("토큰 생성 테스트")
	void tokenCreateTest(){
		// given
//		String accessToken = jwtProvider.createToken(1, "test", "test");
//		String refreshToken = jwtProvider.createToken(1, "test", "test");

		// then
		assertNotNull(accessToken);
		assertNotNull(refreshToken);
	}

	@Test
	@DisplayName("토큰 검증 테스트")
	void tokenValidationTest(){

		// given
		String bearerAccessToken = "Bearer "+accessToken; // "bearer
		String bearerRefreshToken = "Bearer "+refreshToken; // "bearer

		// then
		assertTrue(jwtProvider.validateToken(bearerAccessToken));
		assertTrue(jwtProvider.validateToken(bearerRefreshToken));
	}


}
