package com.ai.controller;

import com.ai.dto.UpdateUserRequest;
import com.ai.dto.UserProfileDto;
import com.ai.model.User;
import com.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(request.getPassword());

        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@ModelAttribute UserProfileDto profileDto,
                                                Authentication authentication) throws IOException {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPhone(profileDto.getPhoneNumber());
        user.setDob(profileDto.getDob());

        MultipartFile customImage = profileDto.getProfilePic();

        if (customImage != null && !customImage.isEmpty()) {
            // Custom image uploaded - you can store it in DB or file system
            // For now, save as Base64 (not recommended in production)
            String base64Image = Base64.getEncoder().encodeToString(customImage.getBytes());
            user.setProfileImageUrl("data:image/png;base64," + base64Image);
        } else {
            // No custom image - use Gravatar
            String gravatarUrl = getGravatarUrl(email);
            user.setProfileImageUrl(gravatarUrl);
        }

        userRepository.save(user);

        return ResponseEntity.ok("Profile updated successfully");
    }
    private String getGravatarUrl(String email) {
        String hash = DigestUtils.md5DigestAsHex(email.trim().toLowerCase().getBytes());
        return "https://www.gravatar.com/avatar/" + hash;
    }

}
