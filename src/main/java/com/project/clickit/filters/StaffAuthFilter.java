package com.project.clickit.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class StaffAuthFilter extends GenericFilterBean {

    private String TYPE_STAFF;

    public StaffAuthFilter(String type) {
        this.TYPE_STAFF = type;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("[Filter]: StaffAuthFilter");

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

//        log.info(String.valueOf(typeCheck.isStaff(authorities.stream().findFirst().get().getAuthority())));

        if(authorities.stream().findFirst().isEmpty()){
            log.info("[Filter]: StaffAuthFilter - Auth Not Found");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpResponse.getWriter().write("접근 권한이 없습니다.");
        }else{
            if(!authorities.stream().findFirst().get().getAuthority().equals(TYPE_STAFF)){
                log.info(TYPE_STAFF + " " + authorities.stream().findFirst().get().getAuthority());
                log.info(String.valueOf(authorities.stream().findFirst().get().getAuthority().equals(TYPE_STAFF)));
                log.info("auth: "+authorities.stream().findFirst().get().getAuthority());
                log.info("[AuthorizationFilter]: StaffAuthFilter: Not a staff member");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json");
                httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
                httpResponse.getWriter().write("접근 권한이 없습니다.");
                httpResponse.getWriter().close();
            }
        }

        chain.doFilter(request, response);
    }
}
