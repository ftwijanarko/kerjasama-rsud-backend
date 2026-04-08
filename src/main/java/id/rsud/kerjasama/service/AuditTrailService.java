package id.rsud.kerjasama.service;

import id.rsud.kerjasama.entity.AuditTrail;
import id.rsud.kerjasama.repository.AuditTrailRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AuditTrailService {

    @Inject
    AuditTrailRepository auditTrailRepository;

    public List<AuditTrail> findAll() {
        return auditTrailRepository.findAllOrdered();
    }

    public List<AuditTrail> findByEntity(String entityType, Long entityId) {
        return auditTrailRepository.findByEntity(entityType, entityId);
    }

    @Transactional
    public void log(String entityType, Long entityId, String action,
                    String oldData, String newData, String performedBy) {
        AuditTrail audit = new AuditTrail();
        audit.setEntityType(entityType);
        audit.setEntityId(entityId);
        audit.setAction(action);
        audit.setOldData(oldData);
        audit.setNewData(newData);
        audit.setPerformedBy(performedBy);
        auditTrailRepository.persist(audit);
    }
}
