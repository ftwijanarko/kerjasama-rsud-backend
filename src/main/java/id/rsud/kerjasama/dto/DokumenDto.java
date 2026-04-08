package id.rsud.kerjasama.dto;

import java.time.LocalDateTime;

public record DokumenDto(
    Long id,
    Long kerjasamaId,
    String fileName,
    Long fileSize,
    String contentType,
    LocalDateTime uploadedAt,
    String uploadedBy
) {}
