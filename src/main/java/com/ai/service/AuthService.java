package com.ai.service;

import com.ai.dto.AuthRequest;
import com.ai.dto.AuthResponse;
import com.ai.dto.RegisterRequest;
import com.ai.exception.AppException;
import com.ai.model.Role;
import com.ai.model.User;
import com.ai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;

@Service
@Validated
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    // Injecting PasswordEncoder directly into AuthService
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST.value());
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }


    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND.value()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Invalid password", HttpStatus.UNAUTHORIZED.value());
        }

        String token = "Login success"; // JWT token generation would go here
        return new AuthResponse(token, "Login successful");
    }

    public String generateGravatarUrl(String email) {
        String hash = DigestUtils.md5DigestAsHex(email.trim().toLowerCase().getBytes());
        return "https://www.gravatar.com/avatar/" + hash + "?d=identicon";
    }
}
