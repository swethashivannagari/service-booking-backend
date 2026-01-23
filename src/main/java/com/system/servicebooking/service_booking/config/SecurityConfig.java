package com.system.servicebooking.service_booking.config;


import com.system.servicebooking.service_booking.util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    public SecurityConfig(JwtAuthenticationFilter jwtFilter){
        this.jwtFilter=jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**","/services","/services/**")
                        .permitAll()
                        //.anyRequest().permitAll());
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
                return http.build();

    }
}

