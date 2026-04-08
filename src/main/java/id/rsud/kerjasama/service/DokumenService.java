package id.rsud.kerjasama.service;

import id.rsud.kerjasama.entity.Dokumen;
import id.rsud.kerjasama.entity.Kerjasama;
import id.rsud.kerjasama.repository.DokumenRepository;
import id.rsud.kerjasama.repository.KerjasamaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DokumenService {

    @ConfigProperty(name = "app.upload.dir", defaultValue = "./uploads")
    String uploadDir;

    @Inject
    DokumenRepository dokumenRepository;

    @Inject
    KerjasamaRepository kerjasamaRepository;

    public List<Dokumen> findByKerjasamaId(Long kerjasamaId) {
        return dokumenRepository.findByKerjasamaId(kerjasamaId);
    }

    public Dokumen findById(Long id) {
        return dokumenRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Dokumen tidak ditemukan", Response.Status.NOT_FOUND));
    }

    @Transactional
    public Dokumen upload(Long kerjasamaId, String fileName, String contentType,
                          Long fileSize, InputStream fileStream, String username) throws IOException {
        Kerjasama kerjasama = kerjasamaRepository.findByIdOptional(kerjasamaId)
                .orElseThrow(() -> new WebApplicationException("Kerjasama tidak ditemukan", Response.Status.NOT_FOUND));

        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDir, String.valueOf(kerjasamaId));
        Files.createDirectories(uploadPath);

        // Generate unique file name to avoid collision
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path filePath = uploadPath.resolve(uniqueFileName);

        // Save file to disk
        Files.copy(fileStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata to database
        Dokumen dokumen = new Dokumen();
        dokumen.setKerjasama(kerjasama);
        dokumen.setFileName(fileName);
        dokumen.setFilePath(filePath.toString());
        dokumen.setFileSize(fileSize);
        dokumen.setContentType(contentType);
        dokumen.setUploadedBy(username);

        dokumenRepository.persist(dokumen);
        return dokumen;
    }

    @Transactional
    public void delete(Long id) throws IOException {
        Dokumen dokumen = findById(id);

        // Delete file from disk
        Path filePath = Paths.get(dokumen.getFilePath());
        Files.deleteIfExists(filePath);

        // Delete from database
        dokumenRepository.delete(dokumen);
    }

    public Path getFilePath(Long id) {
        Dokumen dokumen = findById(id);
        return Paths.get(dokumen.getFilePath());
    }
}
