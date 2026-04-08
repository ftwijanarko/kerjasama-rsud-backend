package id.rsud.kerjasama.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(
    Long id,
    @NotBlank(message = "Username wajib diisi")
    String username,
    String password,
    String fullName,
    String email,
    @NotBlank(message = "Role wajib diisi")
    String role,
    Boolean active
) {}
