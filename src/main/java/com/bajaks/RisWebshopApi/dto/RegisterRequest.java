package com.bajaks.RisWebshopApi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    @Min(3)
    private String username;
    @NotNull
    @NotBlank
    @Min(8)
    private String password;
    private String firstName;
    private String lastName;
}
