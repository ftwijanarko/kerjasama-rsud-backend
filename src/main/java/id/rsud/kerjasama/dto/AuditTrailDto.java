package id.rsud.kerjasama.dto;

import java.time.LocalDateTime;

public record AuditTrailDto(
    Long id,
    String entityType,
    Long entityId,
    String action,
    String oldData,
    String newData,
    String performedBy,
    LocalDateTime performedAt
) {}
