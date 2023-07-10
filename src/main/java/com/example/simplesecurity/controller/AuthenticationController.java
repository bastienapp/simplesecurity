package com.example.simplesecurity.controller;

import com.example.simplesecurity.entity.Role;
import com.example.simplesecurity.entity.User;
import com.example.simplesecurity.repository.RoleRepository;
import com.example.simplesecurity.repository.UserRepository;
import com.example.simplesecurity.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthenticationController(
            UserRepository userRepositoryInjected,
            RoleRepository roleRepositoryInjected,
            AuthenticationManager authManagerInjected,
            TokenService tokenServiceInjected
    ) {
        this.userRepository = userRepositoryInjected;
        this.roleRepository = roleRepositoryInjected;
        this.authManager = authManagerInjected;
        this.tokenService = tokenServiceInjected;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {

        Role userRole = this.roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No user role found"));

        user.setAuthorities(Set.of(userRole));

        PasswordEncoder passwordEncoder
                = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
            Authentication authentication = this.authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            return tokenService.generateToken(authentication);
    }
}
