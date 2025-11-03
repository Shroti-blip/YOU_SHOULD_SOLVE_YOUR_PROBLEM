package com.example.ProjectHON.SecurityPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomSuccessHandler customSuccessHandler;


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        System.out.println("==========DaoAuthentication ==================");

        return daoAuthenticationProvider;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

//        httpSecurity.csrf(csrf->csrf.disable()); //Cross-Site Request Forgery.
        // Configuration
        // urls ko configure kiya hai ki kon se public rahenge or kon se private rahenge.
        // Configuration
        // urls ko configure kiya hai ki kon se public rahenge or kon se private rahenge.
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/register","/css/", "/js/","Images/","/check-email").permitAll()
                .requestMatchers("/user/").hasAnyRole("USER","ADMIN")
                 .requestMatchers("/admin/").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        System.out.println("==========SecurityFilter ==================");

//        httpSecurity.formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/doLogin")
//                        .successHandler((request, response, authentication) -> {
//                            String role = authentication.getAuthorities().iterator().next().getAuthority();
//                            if(role.equals("ADMIN")) response.sendRedirect("/admin/dashboard");
//                            else if(role.equals("FACULTY")) response.sendRedirect("/faculty/dashboard");
//                            else response.sendRedirect("/student/dashboard");
//                        })
//                        .permitAll()
//                );
        //Custom Login Page
        httpSecurity.formLogin(formLogin->{
            // Our login mapping
            formLogin.loginPage("/login");
            // Login form action mapping
//            formLogin.loginProcessingUrl("/authenticate");
                System.out.println("Inside the login");
            // Login karne ke bad kis url par forward karna hai(Valid credential par)
            formLogin.successHandler(customSuccessHandler);
            // Login kiya or nhi hua (Invalid credentials)
            formLogin.failureUrl("/login?error=true");

            // Login form username and password field names
            formLogin.usernameParameter("username");// if email then ham yha email likhte
            formLogin.passwordParameter("password");

        });

//        httpSecurity.csrf(csrf -> csrf
//                .ignoringRequestMatchers("/request-password-update", "/verify-otp-and-update-password")
//        );

        httpSecurity.logout(logout ->{
         logout.permitAll();

        logout.logoutUrl("/logout");
//        logout.
//        logout.logoutSuccessUrl("/?error=true")
        });


        return httpSecurity.build();
    }





    //  Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
