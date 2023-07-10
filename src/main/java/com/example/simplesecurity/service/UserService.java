package com.example.simplesecurity.service;

import com.example.simplesecurity.entity.Role;
import com.example.simplesecurity.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!username.equals("user")) {
            throw new UsernameNotFoundException("User not found with username " + username);
        }

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User userSample = new User();
        userSample.setEmail("user");
        userSample.setPassword(passwordEncoder.encode("password"));

        Role roleSample = new Role();
        roleSample.setAuthority("USER");

        userSample.getAuthorities().add(roleSample);

        return userSample;
    }
}
