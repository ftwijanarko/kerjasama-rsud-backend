package id.rsud.kerjasama.repository;

import id.rsud.kerjasama.entity.Mitra;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MitraRepository implements PanacheRepository<Mitra> {

    public List<Mitra> search(String keyword) {
        return list("lower(namaMitra) like ?1 or lower(emailMitra) like ?1",
                "%" + keyword.toLowerCase() + "%");
    }
}
