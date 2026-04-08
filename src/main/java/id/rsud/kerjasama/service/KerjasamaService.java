package id.rsud.kerjasama.service;

import id.rsud.kerjasama.dto.KerjasamaDto;
import id.rsud.kerjasama.dto.KerjasamaDetailDto;
import id.rsud.kerjasama.dto.DokumenDto;
import id.rsud.kerjasama.entity.Dokumen;
import id.rsud.kerjasama.entity.Kerjasama;
import id.rsud.kerjasama.entity.Mitra;
import id.rsud.kerjasama.repository.DokumenRepository;
import id.rsud.kerjasama.repository.KerjasamaRepository;
import id.rsud.kerjasama.repository.MitraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class KerjasamaService {

    @Inject
    KerjasamaRepository kerjasamaRepository;

    @Inject
    MitraRepository mitraRepository;

    @Inject
    DokumenRepository dokumenRepository;

    @Inject
    AuditTrailService auditTrailService;

    public List<Kerjasama> findAll() {
        return kerjasamaRepository.listAll();
    }

    public List<Kerjasama> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return kerjasamaRepository.search(keyword);
    }

    public List<Kerjasama> findByStatus(String status) {
        return kerjasamaRepository.findByStatusKerjasama(status);
    }

    public List<Kerjasama> findByTipe(String tipe) {
        return kerjasamaRepository.findByTipe(tipe);
    }

    public Kerjasama findById(Long id) {
        return kerjasamaRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Kerjasama tidak ditemukan", Response.Status.NOT_FOUND));
    }

    public KerjasamaDetailDto getDetail(Long id) {
        Kerjasama k = findById(id);
        List<Dokumen> docs = dokumenRepository.findByKerjasamaId(id);
        List<DokumenDto> docDtos = docs.stream().map(d -> new DokumenDto(
                d.getId(), id, d.getFileName(), d.getFileSize(),
                d.getContentType(), d.getUploadedAt(), d.getUploadedBy()
        )).toList();

        return new KerjasamaDetailDto(
                k.getId(),
                k.getMitra1().getId(),
                k.getMitra1().getNamaMitra(),
                k.getMitra1().getEmailMitra(),
                k.getMitra2() != null ? k.getMitra2().getId() : null,
                k.getMitra2() != null ? k.getMitra2().getNamaMitra() : null,
                k.getMitra2() != null ? k.getMitra2().getEmailMitra() : null,
                k.getTipeKerjasama(),
                k.getJudulKerjasama(),
                k.getDasarHukumMitra1(),
                k.getDasarHukumMitra2(),
                k.getDasarHukumRs(),
                k.getTanggalMulai(),
                k.getTanggalSelesai(),
                k.getApprovalStatus(),
                k.getApprovalNotes(),
                k.getStatusKerjasama(),
                k.getKeterangan(),
                k.getCreatedAt(),
                k.getCreatedBy(),
                k.getUpdatedAt(),
                k.getUpdatedBy(),
                docDtos
        );
    }

    @Transactional
    public Kerjasama create(KerjasamaDto dto, String username) {
        Mitra mitra1 = mitraRepository.findByIdOptional(dto.mitra1Id())
                .orElseThrow(() -> new WebApplicationException("Mitra 1 tidak ditemukan", Response.Status.BAD_REQUEST));

        Kerjasama k = new Kerjasama();
        k.setMitra1(mitra1);

        if (dto.mitra2Id() != null) {
            Mitra mitra2 = mitraRepository.findByIdOptional(dto.mitra2Id())
                    .orElseThrow(() -> new WebApplicationException("Mitra 2 tidak ditemukan", Response.Status.BAD_REQUEST));
            k.setMitra2(mitra2);
        }

        k.setTipeKerjasama(dto.tipeKerjasama());
        k.setJudulKerjasama(dto.judulKerjasama());
        k.setDasarHukumMitra1(dto.dasarHukumMitra1());
        k.setDasarHukumMitra2(dto.dasarHukumMitra2());
        k.setDasarHukumRs(dto.dasarHukumRs());
        k.setTanggalMulai(dto.tanggalMulai());
        k.setTanggalSelesai(dto.tanggalSelesai());
        k.setStatusKerjasama(dto.statusKerjasama());
        k.setKeterangan(dto.keterangan());
        k.setApprovalStatus("PENDING");
        k.setCreatedBy(username);
        k.setUpdatedBy(username);

        kerjasamaRepository.persist(k);

        auditTrailService.log("KERJASAMA", k.getId(), "CREATE", null,
                toAuditJson(k), username);

        return k;
    }

    @Transactional
    public Kerjasama update(Long id, KerjasamaDto dto, String username) {
        Kerjasama k = findById(id);
        String oldData = toAuditJson(k);

        Mitra mitra1 = mitraRepository.findByIdOptional(dto.mitra1Id())
                .orElseThrow(() -> new WebApplicationException("Mitra 1 tidak ditemukan", Response.Status.BAD_REQUEST));
        k.setMitra1(mitra1);

        if (dto.mitra2Id() != null) {
            Mitra mitra2 = mitraRepository.findByIdOptional(dto.mitra2Id())
                    .orElseThrow(() -> new WebApplicationException("Mitra 2 tidak ditemukan", Response.Status.BAD_REQUEST));
            k.setMitra2(mitra2);
        } else {
            k.setMitra2(null);
        }

        k.setTipeKerjasama(dto.tipeKerjasama());
        k.setJudulKerjasama(dto.judulKerjasama());
        k.setDasarHukumMitra1(dto.dasarHukumMitra1());
        k.setDasarHukumMitra2(dto.dasarHukumMitra2());
        k.setDasarHukumRs(dto.dasarHukumRs());
        k.setTanggalMulai(dto.tanggalMulai());
        k.setTanggalSelesai(dto.tanggalSelesai());
        k.setStatusKerjasama(dto.statusKerjasama());
        k.setKeterangan(dto.keterangan());
        k.setUpdatedBy(username);

        kerjasamaRepository.persist(k);

        auditTrailService.log("KERJASAMA", k.getId(), "UPDATE", oldData,
                toAuditJson(k), username);

        return k;
    }

    @Transactional
    public Kerjasama approve(Long id, String notes, String username) {
        Kerjasama k = findById(id);
        String oldData = toAuditJson(k);

        k.setApprovalStatus("APPROVED");
        k.setApprovalNotes(notes);
        k.setUpdatedBy(username);
        kerjasamaRepository.persist(k);

        auditTrailService.log("KERJASAMA", k.getId(), "APPROVE", oldData,
                toAuditJson(k), username);

        return k;
    }

    @Transactional
    public Kerjasama reject(Long id, String notes, String username) {
        Kerjasama k = findById(id);
        String oldData = toAuditJson(k);

        k.setApprovalStatus("REJECTED");
        k.setApprovalNotes(notes);
        k.setUpdatedBy(username);
        kerjasamaRepository.persist(k);

        auditTrailService.log("KERJASAMA", k.getId(), "REJECT", oldData,
                toAuditJson(k), username);

        return k;
    }

    @Transactional
    public void delete(Long id, String username) {
        Kerjasama k = findById(id);
        auditTrailService.log("KERJASAMA", k.getId(), "DELETE", toAuditJson(k),
                null, username);
        kerjasamaRepository.delete(k);
    }

    private String toAuditJson(Kerjasama k) {
        return String.format(
            "{\"id\":%d,\"judul\":\"%s\",\"tipe\":\"%s\",\"status\":\"%s\",\"approval\":\"%s\",\"mitra1\":\"%s\",\"tanggalSelesai\":\"%s\"}",
            k.getId(),
            k.getJudulKerjasama() != null ? k.getJudulKerjasama() : "",
            k.getTipeKerjasama(),
            k.getStatusKerjasama() != null ? k.getStatusKerjasama() : "",
            k.getApprovalStatus(),
            k.getMitra1().getNamaMitra(),
            k.getTanggalSelesai()
        );
    }
}
