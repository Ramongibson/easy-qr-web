package com.ramongibson.easyqr.service;

import com.ramongibson.easyqr.model.User;
import com.ramongibson.easyqr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("User registered: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error registering user: {}", user.getUsername(), e);
            throw e;
        }
    }

    public User loginUser(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                log.error("User not found: {}", username);
                throw new UsernameNotFoundException("User not found");
            }

            // Check if the provided password matches the hashed password in the database
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("User logged in: {}", username);
                return user;
            } else {
                log.error("Invalid password for user: {}", username);
                throw new BadCredentialsException("Invalid password");
            }
        } catch (Exception e) {
            log.error("Error during login for user: {}", username, e);
            throw e;
        }
    }

    public boolean isUsernameTaken(String username) {
        try {
            boolean isTaken = userRepository.existsByUsername(username);
            log.debug("Checking if username is taken: {} - Result: {}", username, isTaken);
            return isTaken;
        } catch (Exception e) {
            log.error("Error checking if username is taken: {}", username, e);
            throw e;
        }
    }

    public boolean isEmailExists(String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            log.debug("Checking if email exists: {} - Result: {}", email, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking if email exists: {}", email, e);
            throw e;
        }
    }

}
