package com.ramongibson.easyqr.controller;

import com.ramongibson.easyqr.model.User;
import com.ramongibson.easyqr.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String premiumSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "premium-signup";
    }

    @PostMapping("/signup")
    public String premiumSignupSubmit(@Valid @ModelAttribute("user") User user, Errors errors, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "premium-signup";
        }

        if (errors.hasErrors()) {
            for (FieldError error : errors.getFieldErrors()
            ) {
                log.debug("Field error: {}", error);
            }
            model.addAttribute("user", user);
            return "premium-signup";
        }

        if (userService.isUsernameTaken(user.getUsername())) {
            errors.rejectValue("username", "error.user", "Username is already taken");
            return "premium-signup";
        }

        if (userService.isEmailExists(user.getEmail())) {
            errors.rejectValue("email", "error.user", "Email is already registered");
            return "premium-signup";
        }

        userService.registerUser(user);

        return "redirect:/premium-login";
    }

    @GetMapping("/login")
    public String premiumLoginForm() {
        return "premium-login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        userService.loginUser(username, password);
        return "qr-code";
    }

}
