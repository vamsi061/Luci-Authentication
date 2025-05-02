package com.ai.service;

import com.ai.dto.AuthRequest;
import com.ai.dto.AuthResponse;
import com.ai.dto.RegisterRequest;
import com.ai.exception.AppException;
import com.ai.model.Role;
import com.ai.model.User;
import com.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");

        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword()) // Use encoder if needed
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        // Authentication logic can be added here later
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // You should ideally verify the password manually or via AuthenticationManager

        String token = "Login success";
        return new AuthResponse(token);
    }

    public String generateGravatarUrl(String email) {
        String hash = DigestUtils.md5DigestAsHex(email.trim().toLowerCase().getBytes());
        return "https://www.gravatar.com/avatar/" + hash + "?d=identicon";
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
}
