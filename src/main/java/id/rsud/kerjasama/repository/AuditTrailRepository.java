package id.rsud.kerjasama.repository;

import id.rsud.kerjasama.entity.AuditTrail;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AuditTrailRepository implements PanacheRepository<AuditTrail> {

    public List<AuditTrail> findByEntity(String entityType, Long entityId) {
        return list("entityType = ?1 and entityId = ?2 order by performedAt desc", entityType, entityId);
    }

    public List<AuditTrail> findAllOrdered() {
        return list("order by performedAt desc");
    }
}
