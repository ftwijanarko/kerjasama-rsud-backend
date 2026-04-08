package id.rsud.kerjasama.repository;

import id.rsud.kerjasama.entity.Kerjasama;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class KerjasamaRepository implements PanacheRepository<Kerjasama> {

    public List<Kerjasama> findByApprovalStatus(String status) {
        return list("approvalStatus", status);
    }

    public List<Kerjasama> findByStatusKerjasama(String status) {
        return list("statusKerjasama", status);
    }

    public List<Kerjasama> findByTipe(String tipe) {
        return list("tipeKerjasama", tipe);
    }

    public List<Kerjasama> findExpiringBefore(LocalDate date) {
        return list("tanggalSelesai <= ?1 and approvalStatus = 'APPROVED' and (lastReminderSent is null or lastReminderSent < ?2)",
                date, LocalDate.now().minusDays(1));
    }

    public long countByStatusKerjasama(String status) {
        return count("statusKerjasama", status);
    }

    public long countByTipe(String tipe) {
        return count("tipeKerjasama", tipe);
    }

    public long countByApprovalStatus(String status) {
        return count("approvalStatus", status);
    }

    public List<Kerjasama> search(String keyword) {
        return list("lower(judulKerjasama) like ?1 or lower(mitra1.namaMitra) like ?1",
                "%" + keyword.toLowerCase() + "%");
    }
}
