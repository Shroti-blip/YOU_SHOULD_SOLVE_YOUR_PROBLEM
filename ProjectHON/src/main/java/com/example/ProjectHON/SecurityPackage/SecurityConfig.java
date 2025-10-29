package com.example.ProjectHON.SecurityPackage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // Allow all routes (no Spring login form)
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // disable CSRF for simplicity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/**", // allow all URLs (you can tighten later)
//                                "/usermaster/**"
//                        ).permitAll()
//                        .anyRequest().permitAll()
//                )
//                .formLogin(form -> form.disable()) // disable default Spring login form
//                .httpBasic(basic -> basic.disable()); // disable basic auth too
//
//        return http.build();
//    }

    //  Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
