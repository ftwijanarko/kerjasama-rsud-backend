package id.rsud.kerjasama.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record KerjasamaDetailDto(
    Long id,
    Long mitra1Id,
    String mitra1Nama,
    String mitra1Email,
    Long mitra2Id,
    String mitra2Nama,
    String mitra2Email,
    String tipeKerjasama,
    String judulKerjasama,
    String dasarHukumMitra1,
    String dasarHukumMitra2,
    String dasarHukumRs,
    LocalDate tanggalMulai,
    LocalDate tanggalSelesai,
    String approvalStatus,
    String approvalNotes,
    String statusKerjasama,
    String keterangan,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy,
    List<DokumenDto> dokumen
) {}
