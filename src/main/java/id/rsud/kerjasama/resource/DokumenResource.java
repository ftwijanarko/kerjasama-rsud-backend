package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.entity.Dokumen;
import id.rsud.kerjasama.service.DokumenService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Path("/api/dokumen")
@Produces(MediaType.APPLICATION_JSON)
public class DokumenResource {

    @Inject
    DokumenService dokumenService;

    @GET
    @Path("/{id}/download")
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") Long id) throws IOException {
        Dokumen dokumen = dokumenService.findById(id);
        java.nio.file.Path filePath = dokumenService.getFilePath(id);

        if (!java.nio.file.Files.exists(filePath)) {
            throw new WebApplicationException("File tidak ditemukan di server", Response.Status.NOT_FOUND);
        }

        return Response.ok(java.nio.file.Files.newInputStream(filePath))
                .header("Content-Disposition", "attachment; filename=\"" + dokumen.getFileName() + "\"")
                .header("Content-Type", dokumen.getContentType())
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"MAKER", "ADMIN"})
    public Response delete(@PathParam("id") Long id) throws IOException {
        dokumenService.delete(id);
        return Response.noContent().build();
    }
}
