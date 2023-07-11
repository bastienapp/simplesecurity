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

            Role adminRole = new Role();
            adminRole.setAuthority("ADMIN");
            adminRole = roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setAuthority("USER");
            roleRepository.save(userRole);

            User user = new User();
            user.setEmail("user@email.com");
            user.setPassword(passwordEncoder.encode("password"));
            user.setAuthorities(Set.of(userRole, adminRole));
            userRepository.save(user);

            User admin = new User();
            admin.setEmail("admin@email.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setAuthorities(Set.of(adminRole));
            userRepository.save(admin);
        };
    }
}
