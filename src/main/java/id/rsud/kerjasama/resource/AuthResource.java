package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.LoginRequest;
import id.rsud.kerjasama.dto.LoginResponse;
import id.rsud.kerjasama.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Response.ok(response).build();
    }

    @GET
    @Path("/me")
    public Response me() {
        if (jwt.getName() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(new LoginResponse(
                null,
                jwt.getName(),
                jwt.getClaim("fullName"),
                jwt.getGroups().stream().findFirst().orElse("UNKNOWN")
        )).build();
    }
}
