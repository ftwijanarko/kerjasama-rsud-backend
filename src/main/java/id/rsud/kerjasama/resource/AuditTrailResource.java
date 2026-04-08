package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.AuditTrailDto;
import id.rsud.kerjasama.entity.AuditTrail;
import id.rsud.kerjasama.service.AuditTrailService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/audit-trail")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"CHECKER", "ADMIN"})
public class AuditTrailResource {

    @Inject
    AuditTrailService auditTrailService;

    @GET
    public List<AuditTrailDto> list() {
        return auditTrailService.findAll().stream()
                .map(this::toDto).toList();
    }

    @GET
    @Path("/kerjasama/{id}")
    public List<AuditTrailDto> byKerjasama(@PathParam("id") Long id) {
        return auditTrailService.findByEntity("KERJASAMA", id).stream()
                .map(this::toDto).toList();
    }

    private AuditTrailDto toDto(AuditTrail a) {
        return new AuditTrailDto(
                a.getId(), a.getEntityType(), a.getEntityId(),
                a.getAction(), a.getOldData(), a.getNewData(),
                a.getPerformedBy(), a.getPerformedAt()
        );
    }
}
