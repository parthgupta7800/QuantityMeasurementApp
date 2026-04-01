package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    public OAuthSuccessHandler(UserRepository repository,
                               JwtUtil jwtUtil){
        this.repository=repository;
        this.jwtUtil=jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException,ServletException {

        OAuth2User oAuthUser=(OAuth2User)authentication.getPrincipal();

        String email=oAuthUser.getAttribute("email");
        String name=oAuthUser.getAttribute("name");

        // Check if user exists
        User user = repository.findByEmail(email)
                .orElse(null);

        if(user == null){
            user = new User();
            user.setEmail(email.toLowerCase()); 
            user.setName(name);
            user.setProvider("GOOGLE");
            user.setPassword(null); // IMPORTANT
            user = repository.save(user);

            System.out.println("USER SAVED: "+user.getEmail());
        }
        if(user != null && "LOCAL".equals(user.getProvider())){
            throw new RuntimeException("User already registered with email/password");
        }

        // Generate JWT
        String token=jwtUtil.generateToken(user.getEmail());

        String redirectUrl = "http://localhost:5500/index.html?token=" + token;

        response.sendRedirect(redirectUrl);
    }
}