package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.ApprovalRequest;
import id.rsud.kerjasama.dto.DokumenDto;
import id.rsud.kerjasama.dto.KerjasamaDetailDto;
import id.rsud.kerjasama.dto.KerjasamaDto;
import id.rsud.kerjasama.entity.Dokumen;
import id.rsud.kerjasama.entity.Kerjasama;
import id.rsud.kerjasama.service.DokumenService;
import id.rsud.kerjasama.service.KerjasamaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Path("/api/kerjasama")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KerjasamaResource {

    @Inject
    KerjasamaService kerjasamaService;

    @Inject
    DokumenService dokumenService;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public List<Kerjasama> list(
            @QueryParam("search") String search,
            @QueryParam("status") String status,
            @QueryParam("tipe") String tipe) {
        if (status != null && !status.isBlank()) {
            return kerjasamaService.findByStatus(status);
        }
        if (tipe != null && !tipe.isBlank()) {
            return kerjasamaService.findByTipe(tipe);
        }
        return kerjasamaService.search(search);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public KerjasamaDetailDto getById(@PathParam("id") Long id) {
        return kerjasamaService.getDetail(id);
    }

    @POST
    @RolesAllowed({"MAKER", "ADMIN"})
    public Response create(@Valid KerjasamaDto dto) {
        Kerjasama k = kerjasamaService.create(dto, jwt.getName());
        return Response.status(Response.Status.CREATED).entity(k).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"MAKER", "ADMIN"})
    public Kerjasama update(@PathParam("id") Long id, @Valid KerjasamaDto dto) {
        return kerjasamaService.update(id, dto, jwt.getName());
    }

    @PUT
    @Path("/{id}/approve")
    @RolesAllowed({"CHECKER", "ADMIN"})
    public Kerjasama approve(@PathParam("id") Long id, ApprovalRequest request) {
        return kerjasamaService.approve(id, request != null ? request.notes() : null, jwt.getName());
    }

    @PUT
    @Path("/{id}/reject")
    @RolesAllowed({"CHECKER", "ADMIN"})
    public Kerjasama reject(@PathParam("id") Long id, ApprovalRequest request) {
        return kerjasamaService.reject(id, request != null ? request.notes() : null, jwt.getName());
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        kerjasamaService.delete(id, jwt.getName());
        return Response.noContent().build();
    }

    // === Dokumen endpoints ===

    @GET
    @Path("/{kerjasamaId}/dokumen")
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public List<DokumenDto> listDokumen(@PathParam("kerjasamaId") Long kerjasamaId) {
        return dokumenService.findByKerjasamaId(kerjasamaId).stream()
                .map(d -> new DokumenDto(
                        d.getId(), kerjasamaId, d.getFileName(),
                        d.getFileSize(), d.getContentType(),
                        d.getUploadedAt(), d.getUploadedBy()
                )).toList();
    }

    @POST
    @Path("/{kerjasamaId}/dokumen")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"MAKER", "ADMIN"})
    public Response uploadDokumen(
            @PathParam("kerjasamaId") Long kerjasamaId,
            @RestForm("file") FileUpload file) throws IOException {

        if (file == null) {
            throw new WebApplicationException("File wajib diunggah", Response.Status.BAD_REQUEST);
        }

        InputStream inputStream = java.nio.file.Files.newInputStream(file.uploadedFile());
        Dokumen dokumen = dokumenService.upload(
                kerjasamaId,
                file.fileName(),
                file.contentType(),
                java.nio.file.Files.size(file.uploadedFile()),
                inputStream,
                jwt.getName()
        );

        DokumenDto dto = new DokumenDto(
                dokumen.getId(), kerjasamaId, dokumen.getFileName(),
                dokumen.getFileSize(), dokumen.getContentType(),
                dokumen.getUploadedAt(), dokumen.getUploadedBy()
        );

        return Response.status(Response.Status.CREATED).entity(dto).build();
    }
}
