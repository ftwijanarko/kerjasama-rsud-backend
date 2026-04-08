package id.rsud.kerjasama.service;

import id.rsud.kerjasama.dto.MitraDto;
import id.rsud.kerjasama.entity.Mitra;
import id.rsud.kerjasama.repository.MitraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class MitraService {

    @Inject
    MitraRepository mitraRepository;

    @Inject
    AuditTrailService auditTrailService;

    public List<Mitra> findAll() {
        return mitraRepository.listAll();
    }

    public List<Mitra> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return mitraRepository.search(keyword);
    }

    public Mitra findById(Long id) {
        return mitraRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Mitra tidak ditemukan", Response.Status.NOT_FOUND));
    }

    @Transactional
    public Mitra create(MitraDto dto, String username) {
        Mitra mitra = new Mitra();
        mitra.setNamaMitra(dto.namaMitra());
        mitra.setEmailMitra(dto.emailMitra());
        mitra.setCreatedBy(username);
        mitra.setUpdatedBy(username);
        mitraRepository.persist(mitra);

        auditTrailService.log("MITRA", mitra.getId(), "CREATE", null,
                toJson(mitra), username);

        return mitra;
    }

    @Transactional
    public Mitra update(Long id, MitraDto dto, String username) {
        Mitra mitra = findById(id);
        String oldData = toJson(mitra);

        mitra.setNamaMitra(dto.namaMitra());
        mitra.setEmailMitra(dto.emailMitra());
        mitra.setUpdatedBy(username);
        mitraRepository.persist(mitra);

        auditTrailService.log("MITRA", mitra.getId(), "UPDATE", oldData,
                toJson(mitra), username);

        return mitra;
    }

    @Transactional
    public void delete(Long id, String username) {
        Mitra mitra = findById(id);
        auditTrailService.log("MITRA", mitra.getId(), "DELETE", toJson(mitra),
                null, username);
        mitraRepository.delete(mitra);
    }

    private String toJson(Mitra m) {
        return String.format("{\"id\":%d,\"namaMitra\":\"%s\",\"emailMitra\":\"%s\"}",
                m.getId(), m.getNamaMitra(), m.getEmailMitra() != null ? m.getEmailMitra() : "");
    }
}
