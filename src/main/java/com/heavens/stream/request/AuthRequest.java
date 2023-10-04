package com.heavens.stream.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotEmpty(message = "Username must not be null or empty")
    @Size(min = 6, message = "Username should be at least 6 characters")
    @Pattern(regexp = "^(\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b|\\b[A-Za-z0-9.-]+\\b)$",
            message = "Username should be a valid email or alphanumeric username")
    private String username;

    @NotEmpty(message = "Password must not be null or empty")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

}

