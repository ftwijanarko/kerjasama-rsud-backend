-- ===================================================================
-- V1: Initial Schema - RSUD Kerjasama Platform
-- ===================================================================

-- Users table
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    full_name       VARCHAR(100),
    email           VARCHAR(100),
    role            VARCHAR(20) NOT NULL,
    active          BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

-- Mitra (Partner) table
CREATE TABLE mitra (
    id              BIGSERIAL PRIMARY KEY,
    nama_mitra      VARCHAR(255) NOT NULL,
    email_mitra     VARCHAR(255),
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW(),
    created_by      VARCHAR(50),
    updated_by      VARCHAR(50)
);

-- Setting (Configuration) table
CREATE TABLE setting (
    id              BIGSERIAL PRIMARY KEY,
    config_key      VARCHAR(100) UNIQUE NOT NULL,
    config_value    TEXT NOT NULL
);

-- Kerjasama (Partnership Agreement) table
CREATE TABLE kerjasama (
    id                  BIGSERIAL PRIMARY KEY,
    mitra1_id           BIGINT NOT NULL REFERENCES mitra(id),
    mitra2_id           BIGINT REFERENCES mitra(id),
    tipe_kerjasama      VARCHAR(100) NOT NULL,
    judul_kerjasama     VARCHAR(500),
    dasar_hukum_mitra1  TEXT,
    dasar_hukum_mitra2  TEXT,
    dasar_hukum_rs      TEXT,
    tanggal_mulai       DATE NOT NULL,
    tanggal_selesai     DATE NOT NULL,
    approval_status     VARCHAR(50) DEFAULT 'PENDING',
    approval_notes      TEXT,
    status_kerjasama    VARCHAR(100),
    keterangan          TEXT,
    last_reminder_sent  DATE,
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    created_by          VARCHAR(50),
    updated_by          VARCHAR(50)
);

-- Audit Trail table
CREATE TABLE audit_trail (
    id              BIGSERIAL PRIMARY KEY,
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       BIGINT NOT NULL,
    action          VARCHAR(20) NOT NULL,
    old_data        TEXT,
    new_data        TEXT,
    performed_by    VARCHAR(50),
    performed_at    TIMESTAMP DEFAULT NOW()
);

-- Dokumen (Document) table
CREATE TABLE dokumen (
    id              BIGSERIAL PRIMARY KEY,
    kerjasama_id    BIGINT NOT NULL REFERENCES kerjasama(id) ON DELETE CASCADE,
    file_name       VARCHAR(255) NOT NULL,
    file_path       VARCHAR(500) NOT NULL,
    file_size       BIGINT,
    content_type    VARCHAR(100),
    uploaded_at     TIMESTAMP DEFAULT NOW(),
    uploaded_by     VARCHAR(50)
);

-- Indexes
CREATE INDEX idx_kerjasama_mitra1 ON kerjasama(mitra1_id);
CREATE INDEX idx_kerjasama_mitra2 ON kerjasama(mitra2_id);
CREATE INDEX idx_kerjasama_approval ON kerjasama(approval_status);
CREATE INDEX idx_kerjasama_tgl_selesai ON kerjasama(tanggal_selesai);
CREATE INDEX idx_audit_entity ON audit_trail(entity_type, entity_id);
CREATE INDEX idx_dokumen_kerjasama ON dokumen(kerjasama_id);
CREATE INDEX idx_users_role ON users(role);

-- Seed default settings
INSERT INTO setting (config_key, config_value) VALUES
    ('tipe_kerjasama', '["Kerjasama Manajemen", "Kerjasama Klinis"]'),
    ('status_kerjasama', '["Berjalan", "Perlu Proses Perpanjangan", "Proses Oleh TU", "Proses Mitra", "Tidak Diperpanjang"]'),
    ('reminder_days_before', '90');
