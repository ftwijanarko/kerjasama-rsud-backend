package id.rsud.kerjasama.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kerjasama")
public class Kerjasama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mitra1_id", nullable = false)
    private Mitra mitra1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mitra2_id")
    private Mitra mitra2;

    @Column(name = "tipe_kerjasama", nullable = false, length = 100)
    private String tipeKerjasama;

    @Column(name = "judul_kerjasama", length = 500)
    private String judulKerjasama;

    @Column(name = "dasar_hukum_mitra1", columnDefinition = "TEXT")
    private String dasarHukumMitra1;

    @Column(name = "dasar_hukum_mitra2", columnDefinition = "TEXT")
    private String dasarHukumMitra2;

    @Column(name = "dasar_hukum_rs", columnDefinition = "TEXT")
    private String dasarHukumRs;

    @Column(name = "tanggal_mulai", nullable = false)
    private LocalDate tanggalMulai;

    @Column(name = "tanggal_selesai", nullable = false)
    private LocalDate tanggalSelesai;

    @Column(name = "approval_status", length = 50)
    private String approvalStatus = "PENDING";

    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;

    @Column(name = "status_kerjasama", length = 100)
    private String statusKerjasama;

    @Column(columnDefinition = "TEXT")
    private String keterangan;

    @Column(name = "last_reminder_sent")
    private LocalDate lastReminderSent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Mitra getMitra1() { return mitra1; }
    public void setMitra1(Mitra mitra1) { this.mitra1 = mitra1; }

    public Mitra getMitra2() { return mitra2; }
    public void setMitra2(Mitra mitra2) { this.mitra2 = mitra2; }

    public String getTipeKerjasama() { return tipeKerjasama; }
    public void setTipeKerjasama(String tipeKerjasama) { this.tipeKerjasama = tipeKerjasama; }

    public String getJudulKerjasama() { return judulKerjasama; }
    public void setJudulKerjasama(String judulKerjasama) { this.judulKerjasama = judulKerjasama; }

    public String getDasarHukumMitra1() { return dasarHukumMitra1; }
    public void setDasarHukumMitra1(String dasarHukumMitra1) { this.dasarHukumMitra1 = dasarHukumMitra1; }

    public String getDasarHukumMitra2() { return dasarHukumMitra2; }
    public void setDasarHukumMitra2(String dasarHukumMitra2) { this.dasarHukumMitra2 = dasarHukumMitra2; }

    public String getDasarHukumRs() { return dasarHukumRs; }
    public void setDasarHukumRs(String dasarHukumRs) { this.dasarHukumRs = dasarHukumRs; }

    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public String getApprovalNotes() { return approvalNotes; }
    public void setApprovalNotes(String approvalNotes) { this.approvalNotes = approvalNotes; }

    public String getStatusKerjasama() { return statusKerjasama; }
    public void setStatusKerjasama(String statusKerjasama) { this.statusKerjasama = statusKerjasama; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public LocalDate getLastReminderSent() { return lastReminderSent; }
    public void setLastReminderSent(LocalDate lastReminderSent) { this.lastReminderSent = lastReminderSent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
