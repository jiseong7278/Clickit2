package com.project.clickit.configs;

import com.project.clickit.exceptions.security.CustomAccessDeniedHandler;
import com.project.clickit.filters.JwtAuthenticationFilter;
import com.project.clickit.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Value("${roles.dev}")
    private String TYPE_DEV;

    @Value("${roles.staff}")
    private String TYPE_STAFF;

    @Value("${roles.student}")
    private String TYPE_STUDENT;

    private final JwtProvider jwtProvider;

    private final String[] dev_only = {""};

    private final String[] staff_allowed =
            {"member/create", "member/update", "member/delete",
            "dormitory/create", "dormitory/update", "dormitory/delete"};

    @Autowired
    public SecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(CsrfConfigurer::disable);
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/swagger-ui.html#/**").permitAll()
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/member/**").authenticated()
                                .requestMatchers("/dormitory/getAll").hasAnyRole(TYPE_DEV, TYPE_STAFF)
                                .anyRequest().authenticated())
                .sessionManagement((sessionManager) -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors((cors)-> cors.configurationSource(
                        request -> {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000/", "http://localhost:8080/"));
                            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
                            corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
                            corsConfiguration.addExposedHeader("Authorization");
                            return corsConfiguration;
                        }));

        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(handler -> handler.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

}