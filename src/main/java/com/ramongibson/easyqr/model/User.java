package com.ramongibson.easyqr.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Size(max = 32, message = "First Name is too long")
    @NotEmpty(message = "First name is required")
    private String firstName;

    @Size(max = 32, message = "last Name is too long")
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Username is required")
    @Size(min = 5, max = 12, message = "Username must be at least 5-12 characters long")
    @Size
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 11, message = "Password must be at least 8-11 characters long")
    private String password;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Transient
    private String confirmPassword;
    private List<QRCode> qrCodes = new ArrayList<>();
}
