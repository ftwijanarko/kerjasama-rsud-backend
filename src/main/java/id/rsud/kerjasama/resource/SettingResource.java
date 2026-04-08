package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.SettingDto;
import id.rsud.kerjasama.entity.Setting;
import id.rsud.kerjasama.service.SettingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/settings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SettingResource {

    @Inject
    SettingService settingService;

    @GET
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public List<Setting> list() {
        return settingService.findAll();
    }

    @GET
    @Path("/key/{key}")
    @RolesAllowed({"MAKER", "CHECKER", "ADMIN"})
    public Response getByKey(@PathParam("key") String key) {
        return settingService.findByKey(key)
                .map(s -> Response.ok(s).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(@Valid SettingDto dto) {
        Setting setting = settingService.create(dto);
        return Response.status(Response.Status.CREATED).entity(setting).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Setting update(@PathParam("id") Long id, @Valid SettingDto dto) {
        return settingService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        settingService.delete(id);
        return Response.noContent().build();
    }
}
