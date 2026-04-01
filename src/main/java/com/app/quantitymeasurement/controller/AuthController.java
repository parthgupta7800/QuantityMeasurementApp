package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.app.quantitymeasurement.DTO.*;

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

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto){

        String email=dto.getEmail().toLowerCase();

        if(repository.findByEmail(email).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user=new User();
        user.setName(dto.getName());
        user.setEmail(email);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setProvider("LOCAL");

        repository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO){

        User existing=repository.findByEmail(loginDTO.getEmail().toLowerCase())
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!"LOCAL".equals(existing.getProvider())){
            throw new RuntimeException("Please login using Google");
        }

        if(!encoder.matches(loginDTO.getPassword(),existing.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(existing.getEmail());
    }
}
