package com.example.simplesecurity;

import com.example.simplesecurity.entity.Role;
import com.example.simplesecurity.entity.User;
import com.example.simplesecurity.repository.RoleRepository;
import com.example.simplesecurity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SimplesecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplesecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository) {

        return (args) -> {
            if (roleRepository.findByAuthority("ADMIN").isPresent()) {
                // don't create users and roles if presents
                return;
            }
            PasswordEncoder passwordEncoder
                    = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            Role adminRole = new Role("ADMIN");
            adminRole = roleRepository.save(adminRole);

            Role userRole = new Role("USER");
            userRole = roleRepository.save(userRole);

            User user = new User(
                    "user@email.com",
                    passwordEncoder.encode("password"),
                    Set.of(userRole)
            );
            userRepository.save(user);

            User admin = new User(
                    "admin@email.com",
                    passwordEncoder.encode("password"),
                    Set.of(userRole, adminRole)
            );
            userRepository.save(admin);
        };
    }
}
