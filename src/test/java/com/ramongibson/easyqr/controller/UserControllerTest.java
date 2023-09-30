package com.ramongibson.easyqr.controller;

import com.ramongibson.easyqr.model.User;
import com.ramongibson.easyqr.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPremiumSignupForm() {
        String viewName = userController.premiumSignupForm(model);
        assertEquals("premium-signup", viewName);
    }

    @Test
    void testPremiumSignupSubmit() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setConfirmPassword("password");

        Errors errors = new BeanPropertyBindingResult(user, "user");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(false);
        when(userService.isEmailExists(user.getEmail())).thenReturn(false);

        String viewName = userController.premiumSignupSubmit(user, errors, model);

        assertEquals("redirect:/premium-login", viewName);
        verify(userService, times(1)).registerUser(user);
        verifyNoMoreInteractions(model);
    }

    @Test
    void testPremiumSignupSubmitWithMatchingPasswords() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setConfirmPassword("password");

        Errors errors = new BeanPropertyBindingResult(user, "user");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(false);
        when(userService.isEmailExists(user.getEmail())).thenReturn(false);

        String viewName = userController.premiumSignupSubmit(user, errors, model);

        assertEquals("redirect:/premium-login", viewName);
        verify(userService, times(1)).registerUser(user);
        verifyNoMoreInteractions(model);
    }


    @Test
    void testPremiumSignupSubmitWithPasswordMismatch() {
        User user = new User();
        user.setPassword("password");
        user.setConfirmPassword("wrongpassword");

        Errors errors = new BeanPropertyBindingResult(user, "user");
        errors.rejectValue("confirmPassword", "error.user", "Passwords do not match");

        String viewName = userController.premiumSignupSubmit(user, errors, model);

        assertEquals("premium-signup", viewName);
    }

    @Test
    void testPremiumSignupSubmitWithUsernameTaken() {
        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password");
        user.setConfirmPassword("password");

        Errors errors = new BeanPropertyBindingResult(user, "user");

        when(userService.isUsernameTaken(user.getUsername())).thenReturn(true);

        String viewName = userController.premiumSignupSubmit(user, errors, model);

        assertEquals("premium-signup", viewName);
        verify(userService, never()).registerUser(user);
    }

    @Test
    void testPremiumSignupSubmitWithEmailExists() {
        User user = new User();
        user.setPassword("testpassword");
        user.setConfirmPassword("testpassword");
        user.setEmail("existing@google.com");

        Errors errors = new BeanPropertyBindingResult(user, "user");

        when(userService.isEmailExists(user.getEmail())).thenReturn(true);

        String viewName = userController.premiumSignupSubmit(user, errors, model);

        assertEquals("premium-signup", viewName);
        verify(userService, never()).registerUser(user);
    }

    @Test
    void testLoginUser() {
        String username = "testuser";
        String password = "testpassword";

        String viewName = userController.loginUser(username, password);

        assertEquals("qr-code", viewName);
        verify(userService, times(1)).loginUser(username, password);
    }

    @Test
    void testPremiumLoginForm() {
        String viewName = userController.premiumLoginForm();
        
        assertEquals("premium-login", viewName);

        verifyNoInteractions(model);
    }
}
