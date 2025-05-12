package com.example.firstApi.services;

import com.example.firstApi.dtos.SignInRequest;
import com.example.firstApi.dtos.SignInResponse;
import com.example.firstApi.dtos.SignUpRequest;
import com.example.firstApi.entities.ERole;
import com.example.firstApi.entities.User;
import com.example.firstApi.repositories.UserRepository;
import com.example.firstApi.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

   public String registerUser(SignUpRequest signUpRequest){
       if(userRepository.existsByEmail(signUpRequest.getEmail())){
           return "User already exists";
       }
       User user = new User();
       user.setEmail(signUpRequest.getEmail());
       user.setUsername(signUpRequest.getUsername());
       user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
       user.setRole(ERole.ROLE_USER);

       userRepository.save(user);
       return "User registered successfully";
   }

   public ResponseEntity<?> loginUser(SignInRequest signInRequest){
       Authentication authentication;
       try {
           authentication = authenticationManager
                   .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
       } catch (AuthenticationException exception) {
           Map<String, Object> map = new HashMap<>();
           map.put("message", "Bad credentials");
           map.put("status", false);
           return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
       }

       SecurityContextHolder.getContext().setAuthentication(authentication);

       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       String jwt = jwtUtils.generateTokenFromUsername(signInRequest.getEmail());

       String role = userDetails.getAuthorities().iterator().next().getAuthority();

       SignInResponse loginResponse = new SignInResponse(jwt, userDetails.getUsername(), role);

       return ResponseEntity.ok(loginResponse);




   };
}
