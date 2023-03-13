package com.hyuuny.authresourceserver.common.security;

import com.hyuuny.authresourceserver.common.security.filters.JwtAuthenticationFilter;
import com.hyuuny.authresourceserver.common.security.provider.JwtProvider;
import com.hyuuny.authresourceserver.domain.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailService;

    private final JwtProvider jwtProvider;

    private final String[] webSecurityIgnoring = {
            "/",
            "/favicon.ico",
            "/auth/login",
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/users/applicants/validate",
            "/users/accounts"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService);
        httpSecurity.csrf().disable()
                .authorizeHttpRequests(
                        auth -> {
                            try {
                                auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS, "*").permitAll()
                                        .requestMatchers(HttpMethod.POST,
                                                "/admin/auth/login",
                                                "/admin/auth/token/refresh"
                                        ).permitAll()
                                        .requestMatchers(this.webSecurityIgnoring).permitAll()
                                        .anyRequest().hasAnyRole("SYS_ADMIN", "SYS_MANAGER");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> {
                    log.error("Unauthorized error: {}", e.getMessage());
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(this.jwtProvider),
                UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
