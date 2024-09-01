package com.SoT.JIN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 인증 및 권한 설정
        http.authorizeHttpRequests((authorize) -> {
            authorize
                    .requestMatchers("/", "/home", "/about", "/public/**").permitAll()  // 특정 경로 접근 허용
                    .anyRequest().authenticated();  // 그 외의 모든 요청은 인증 필요
        });

        // 로그인 설정
        http.formLogin((formLogin) -> {
            formLogin
                    .loginPage("/login")  // 사용자 정의 로그인 페이지 경로
                    .defaultSuccessUrl("/test", true)  // 로그인 성공 시 이동할 페이지
                    .failureUrl("/login?error=true")  // 로그인 실패 시 이동할 페이지
                    .permitAll();  // 로그인 페이지는 인증 없이 접근 가능
        });

        // 로그아웃 설정
        http.logout((logout) -> {
            logout
                    .logoutUrl("/logout")  // 로그아웃 처리 URL
                    .logoutSuccessUrl("/home")  // 로그아웃 성공 후 이동할 페이지
                    .invalidateHttpSession(true)  // 세션 무효화
                    .deleteCookies("JSESSIONID")  // 쿠키 삭제
                    .permitAll();  // 로그아웃은 인증 없이 접근 가능
        });

        return http.build();
    }
}
