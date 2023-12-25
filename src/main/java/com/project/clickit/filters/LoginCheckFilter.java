package com.project.clickit.filters;

import com.project.clickit.jwt.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;


@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] whiteList = {"/" , "/member/login" , "/member/logout" , "/css/*" , "/test"};

//    private final JwtProvider jwtProvider = new JwtProvider();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

//        try{
//            log.info("LoginCheckFilter start: ", requestURI);
//            if(isLoginCheckPath(requestURI)){
//                log.info("인증 체크 로직 실행: ", requestURI);
//                String accessToken = httpRequest.getHeader("Authorization");
//                if(!jwtProvider.validateToken(accessToken, true)){
//                    log.info("토큰이 유효하지 않습니다. accessToken: ", accessToken);
//                    httpResponse.sendError(400, "올바르지 않은 토큰"); // 여기서 redirect 하지 말고 에러 메시지를 보내야 front-end에서 redirect 할 수 있음
//                    return;
//                }
//            }
//            chain.doFilter(request, response);
//        }catch (Exception e) {
//            throw e;
//        }finally {
//            log.info("LoginCheckFilter end: ", requestURI);
//        }
    }

    @Override
    public void destroy() {

    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
