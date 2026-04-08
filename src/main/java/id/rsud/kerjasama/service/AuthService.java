package id.rsud.kerjasama.service;

import id.rsud.kerjasama.dto.LoginRequest;
import id.rsud.kerjasama.dto.LoginResponse;
import id.rsud.kerjasama.entity.User;
import id.rsud.kerjasama.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new WebApplicationException("Username atau password salah", Response.Status.UNAUTHORIZED));

        if (!user.getActive()) {
            throw new WebApplicationException("Akun tidak aktif", Response.Status.FORBIDDEN);
        }

        if (!BCrypt.checkpw(request.password(), user.getPassword())) {
            throw new WebApplicationException("Username atau password salah", Response.Status.UNAUTHORIZED);
        }

        String token = Jwt.issuer("rsud-kerjasama")
                .upn(user.getUsername())
                .groups(Set.of(user.getRole()))
                .claim("fullName", user.getFullName() != null ? user.getFullName() : "")
                .expiresIn(Duration.ofHours(24))
                .sign();

        return new LoginResponse(token, user.getUsername(), user.getFullName(), user.getRole());
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
}
