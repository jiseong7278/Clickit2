package com.project.clickit.jwt;

import com.project.clickit.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        log.info("token: " + token);

        try{
            String path = httpRequest.getServletPath();
            logger.info("path: " + path);
            if(!PatternMatchUtils.simpleMatch(whiteList, path)){
                if(token != null && jwtProvider.validateToken(token)){
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            log.info("doFilter Exception: " + e.getMessage());
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpResponse.getWriter().write(e.getMessage());
        }
    }
}