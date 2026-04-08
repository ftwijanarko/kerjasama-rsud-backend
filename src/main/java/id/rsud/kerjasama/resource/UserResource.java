package id.rsud.kerjasama.resource;

import id.rsud.kerjasama.dto.UserDto;
import id.rsud.kerjasama.entity.User;
import id.rsud.kerjasama.repository.UserRepository;
import id.rsud.kerjasama.service.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @Inject
    AuthService authService;

    @GET
    public List<User> list() {
        return userRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public User getById(@PathParam("id") Long id) {
        return userRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("User tidak ditemukan", Response.Status.NOT_FOUND));
    }

    @POST
    @Transactional
    public Response create(@Valid UserDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new WebApplicationException("Username sudah digunakan", Response.Status.CONFLICT);
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(authService.hashPassword(dto.password()));
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setActive(dto.active() != null ? dto.active() : true);
        userRepository.persist(user);

        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public User update(@PathParam("id") Long id, @Valid UserDto dto) {
        User user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("User tidak ditemukan", Response.Status.NOT_FOUND));

        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setRole(dto.role());
        user.setActive(dto.active() != null ? dto.active() : user.getActive());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(authService.hashPassword(dto.password()));
        }

        userRepository.persist(user);
        return user;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        User user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("User tidak ditemukan", Response.Status.NOT_FOUND));
        userRepository.delete(user);
        return Response.noContent().build();
    }
}
