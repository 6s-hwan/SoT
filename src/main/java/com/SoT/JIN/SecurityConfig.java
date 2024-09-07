package com.SoT.JIN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> {
            csrf.disable();
        });
        http.authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers("/**").permitAll();
        });

        http.formLogin((formLogin) -> {
            formLogin.loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .failureHandler(customAuthenticationFailureHandler); // 실패 핸들러 적용
        });

        http.logout((logout) -> {
            logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/home")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID");
        });

        return http.build();
    }
}
