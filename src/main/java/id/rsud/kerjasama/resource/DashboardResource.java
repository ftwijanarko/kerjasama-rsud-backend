package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.DashboardSummaryDto;
import id.rsud.kerjasama.repository.KerjasamaRepository;
import id.rsud.kerjasama.service.SettingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/api/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
public class DashboardResource {

    @Inject
    KerjasamaRepository kerjasamaRepository;

    @Inject
    SettingService settingService;

    @GET
    @Path("/summary")
    public DashboardSummaryDto getSummary() {
        long total = kerjasamaRepository.count();

        // Count by tipe
        long klinis = kerjasamaRepository.countByTipe("Kerjasama Klinis");
        long manajemen = kerjasamaRepository.countByTipe("Kerjasama Manajemen");

        // Count pending approval
        long pending = kerjasamaRepository.countByApprovalStatus("PENDING");

        // Count by status kerjasama
        Map<String, Long> byStatus = new LinkedHashMap<>();
        String statusConfig = settingService.getValueByKey("status_kerjasama",
                "[\"Berjalan\",\"Perlu Proses Perpanjangan\",\"Proses Oleh TU\",\"Proses Mitra\",\"Tidak Diperpanjang\"]");

        // Parse JSON array manually (simple approach)
        String[] statuses = statusConfig.replace("[", "").replace("]", "")
                .replace("\"", "").split(",");
        for (String s : statuses) {
            String status = s.trim();
            if (!status.isEmpty()) {
                byStatus.put(status, kerjasamaRepository.countByStatusKerjasama(status));
            }
        }

        return new DashboardSummaryDto(total, klinis, manajemen, pending, byStatus);
    }
}
