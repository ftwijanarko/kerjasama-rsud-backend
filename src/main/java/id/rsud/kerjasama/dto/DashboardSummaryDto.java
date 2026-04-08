package id.rsud.kerjasama.dto;

import java.util.Map;

public record DashboardSummaryDto(
    long totalKerjasama,
    long totalKlinis,
    long totalManajemen,
    long pendingApproval,
    Map<String, Long> byStatus
) {}
