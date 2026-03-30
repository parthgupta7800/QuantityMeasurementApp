package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository repository,
                          PasswordEncoder encoder,
                          JwtUtil jwtUtil){
        this.repository=repository;
        this.encoder=encoder;
        this.jwtUtil=jwtUtil;
    }

    // REGISTER
    @PostMapping("/register")
    public String register(@RequestBody User user){

        
        user.setProvider("LOCAL");

        repository.save(user);

        return "User registered successfully";
    }

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody User user){

        User existing=repository.findByEmail(user.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        
        return jwtUtil.generateToken(existing.getEmail());
    }
}
