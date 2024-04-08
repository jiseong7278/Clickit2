package com.project.clickit.filters;

import com.project.clickit.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    private final String[] whiteList = {"/swagger-ui.html#/**", "/login/**"};

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        try{
            String path = httpRequest.getServletPath();
            logger.info("path: " + path);
            if(!PatternMatchUtils.simpleMatch(whiteList, path)){
                if(jwtProvider.validateToken(token)){
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            log.error("doFilter Exception: " + e.getMessage());
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpResponse.getWriter().write("인증 과정에서 오류가 발생하였습니다.\n다시 로그인해주세요.\n증상이 반복될 경우 관리자에게 문의해주세요.");
            httpResponse.getWriter().close();
            log.error("JwtAuthenticationFilter Exception: " + e.getMessage());
        }
    }
}