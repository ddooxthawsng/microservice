package com.thangdm.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.Objects;

public class CustomUserDetailsService {

    PasswordEncoder passwordEncoder;

//        custom basic user from db
//    private final UserRepository userRepository; // JpaRepository<UserEntity, Long>
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword()) // đã mã hóa bằng BCrypt
//                .roles(user.getRole())        // ví dụ: "ADMIN"
//                .build();
//    }

    @Bean // user im mem
    public UserDetailsService users() {
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean // user from config yml
    public UserDetailsService userDetailsService(BasicUserProperties props) {
        if (Objects.isNull(props.getUsers()))
            return null;
        List<UserDetails> users = props.getUsers().stream()
                .map(u -> org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRoles().split(","))
                        .build())
                .toList();
        return new InMemoryUserDetailsManager(users);
    }
}
