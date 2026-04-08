package id.rsud.kerjasama.dto;

import jakarta.validation.constraints.NotBlank;

public record SettingDto(
    Long id,
    @NotBlank(message = "Config key wajib diisi")
    String configKey,
    @NotBlank(message = "Config value wajib diisi")
    String configValue
) {}
