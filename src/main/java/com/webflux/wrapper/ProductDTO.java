package com.webflux.wrapper;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductDTO(@NotBlank(message = "Name is mandatory")
                         String name,

                         @Min(value = 1, message = "Must be bigger than zero")
                         float price) {
}
