package com.example.chatClips.config;


import static org.springframework.web.servlet.function.RequestPredicates.headers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console());
    }


    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/", "/login", "/loginProc", "/loginForm", "/joinForm", "/user/signup", "/user/login").permitAll() //모든 사용자에게 허용
            .requestMatchers("/admin").hasRole("ADMIN") //"ADMIN" 권한을 가진 사람만 허용
            .requestMatchers("/user/**", "/feedback/**", "/chatroom/**", "/api/**").hasAnyRole("USER") //"ADMIN"과 "USER"만 허용
            .anyRequest().authenticated()
        );



        http.formLogin((auth)->auth.loginPage("/login")
            .loginProcessingUrl("/loginProc")
            .permitAll()
        );
        http.csrf((auth) -> auth.disable());

        http.sessionManagement((auth) -> auth
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false));

        http.sessionManagement((auth) -> auth
            .sessionFixation().changeSessionId());

        return http.build();
    }
}
