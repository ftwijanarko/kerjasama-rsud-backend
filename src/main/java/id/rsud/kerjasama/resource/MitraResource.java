package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.MitraDto;
import id.rsud.kerjasama.entity.Mitra;
import id.rsud.kerjasama.service.MitraService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/mitra")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MitraResource {

    @Inject
    MitraService mitraService;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public List<Mitra> list(@QueryParam("search") String search) {
        return mitraService.search(search);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public Mitra getById(@PathParam("id") Long id) {
        return mitraService.findById(id);
    }

    @POST
    @RolesAllowed({"MAKER", "ADMIN"})
    public Response create(@Valid MitraDto dto) {
        Mitra mitra = mitraService.create(dto, jwt.getName());
        return Response.status(Response.Status.CREATED).entity(mitra).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"MAKER", "ADMIN"})
    public Mitra update(@PathParam("id") Long id, @Valid MitraDto dto) {
        return mitraService.update(id, dto, jwt.getName());
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        mitraService.delete(id, jwt.getName());
        return Response.noContent().build();
    }
}
