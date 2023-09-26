package com.ramongibson.easyqr.controller;

import com.ramongibson.easyqr.model.User;
import com.ramongibson.easyqr.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String premiumSignupSubmit(@ModelAttribute User user, BindingResult bindingResult) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "premium-signup";
        }

        if (userService.isUsernameTaken(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
            return "premium-signup";
        }

        if (userService.isEmailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email is already registered");
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
    public String loginUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        try {
            User authenticatedUser = userService.loginUser(username, password);
            return "qrCode";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password");
            return "premium-login";
        }
    }
}
