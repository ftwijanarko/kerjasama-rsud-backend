package id.rsud.kerjasama.dto;

import jakarta.validation.constraints.NotBlank;

public record MitraDto(
    Long id,
    @NotBlank(message = "Nama mitra wajib diisi")
    String namaMitra,
    String emailMitra
) {}
