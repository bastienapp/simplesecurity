package com.example.simplesecurity.controller;

import com.example.simplesecurity.entity.Role;
import com.example.simplesecurity.entity.User;
import com.example.simplesecurity.repository.RoleRepository;
import com.example.simplesecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(
            UserRepository userRepositoryInjected,
            RoleRepository roleRepositoryInjected
    ) {
        this.userRepository = userRepositoryInjected;
        this.roleRepository = roleRepositoryInjected;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {

        Role userRole = this.roleRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No user role found"));

        user.setAuthorities(Set.of(userRole));

        PasswordEncoder passwordEncoder
                = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }
}
