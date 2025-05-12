package com.example.firstApi.controllers;

import com.example.firstApi.dtos.SignInRequest;
import com.example.firstApi.dtos.SignInResponse;
import com.example.firstApi.dtos.SignUpRequest;
import com.example.firstApi.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequest signUpRequest){
        return authService.registerUser(signUpRequest);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){
        return authService.loginUser(signInRequest);
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "✅ Protected User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "✅ Protected Admin Content.";
    }
}
