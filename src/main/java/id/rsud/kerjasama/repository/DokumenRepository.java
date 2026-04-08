package id.rsud.kerjasama.repository;

import id.rsud.kerjasama.entity.Dokumen;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DokumenRepository implements PanacheRepository<Dokumen> {

    public List<Dokumen> findByKerjasamaId(Long kerjasamaId) {
        return list("kerjasama.id", kerjasamaId);
    }
}
