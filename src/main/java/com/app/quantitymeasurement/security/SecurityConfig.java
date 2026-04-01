package com.app.quantitymeasurement.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuthSuccessHandler oAuthSuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter,
                          OAuthSuccessHandler oAuthSuccessHandler){
        this.jwtFilter=jwtFilter;
        this.oAuthSuccessHandler=oAuthSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            //  Disable CSRF
            .csrf(csrf -> csrf.disable())

            // IMPORTANT: allow H2 console frames
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            )

            // Return 401 instead of redirect
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                
                //operation available for guest user
                .requestMatchers("/api/v1/quantities/**").permitAll()

                // OAuth
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                // H2 Console
                .requestMatchers("/h2-console/**").permitAll()

                // Swagger
                .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()

                // Secure all others
                .anyRequest().authenticated()
            )

            // OAuth login success handler
            .oauth2Login(oauth -> oauth
                .successHandler(oAuthSuccessHandler)
            )

            // JWT filter before Spring auth
            .addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}