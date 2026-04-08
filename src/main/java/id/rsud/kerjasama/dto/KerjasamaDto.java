package id.rsud.kerjasama.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record KerjasamaDto(
    Long id,
    @NotNull(message = "Mitra 1 wajib diisi")
    Long mitra1Id,
    Long mitra2Id,
    @NotBlank(message = "Tipe kerjasama wajib diisi")
    String tipeKerjasama,
    String judulKerjasama,
    String dasarHukumMitra1,
    String dasarHukumMitra2,
    String dasarHukumRs,
    @NotNull(message = "Tanggal mulai wajib diisi")
    LocalDate tanggalMulai,
    @NotNull(message = "Tanggal selesai wajib diisi")
    LocalDate tanggalSelesai,
    String statusKerjasama,
    String keterangan
) {}
