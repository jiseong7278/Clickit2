package com.project.clickit.configs;

import com.project.clickit.filters.StaffAuthFilter;
import com.project.clickit.jwt.JwtAuthenticationFilter;
import com.project.clickit.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

    @Value("${roles.staff}")
    private String TYPE_STAFF;

    private final JwtProvider jwtProvider;

    @Autowired
    public SecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/swagger-ui.html#/**").permitAll()
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/member/**").authenticated()
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
//                .securityMatcher("/member/**").addFilter(new AuthorizationFilter(jwtProvider)) // 예시
//                .securityMatcher("/login/**").addFilterAt(new AuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // 예시

//                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

//                .addFilterAt(new AuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
//        http.securityMatcher("/login/**").addFilter(new AuthorizationFilter(jwtProvider)); // 예시
//        http.securityMatcher("/login/**", "/member/**").addFilter(new AuthorizationFilter(jwtProvider)); // 예시

        log.info("SecurityConfig: filterChain");
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        http.securityMatcher("/dormitory/getAll").addFilterAfter(new StaffAuthFilter(TYPE_STAFF), JwtAuthenticationFilter.class);

        return http.build();
    }
}