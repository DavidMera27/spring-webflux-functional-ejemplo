package com.webflux.security.wrapper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupDTO(@NotBlank(message = "Username is mandatory")
                        String username,

                        @NotBlank(message = "Email is mandatory")
                        @Email(message = "Invalid email")
                        String email,

                        @NotBlank(message = "Password is mandatory")
                        String password) {
}
