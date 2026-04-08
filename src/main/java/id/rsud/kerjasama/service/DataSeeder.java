package id.rsud.kerjasama.service;

import id.rsud.kerjasama.entity.User;
import id.rsud.kerjasama.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class DataSeeder {

    private static final Logger LOG = Logger.getLogger(DataSeeder.class);

    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if (userRepository.count() == 0) {
            LOG.info("No users found. Seeding default admin user...");

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt(12)));
            admin.setFullName("Administrator");
            admin.setEmail("admin@rsud-soedono.go.id");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.persist(admin);

            User maker = new User();
            maker.setUsername("maker");
            maker.setPassword(BCrypt.hashpw("maker123", BCrypt.gensalt(12)));
            maker.setFullName("Maker User");
            maker.setEmail("maker@rsud-soedono.go.id");
            maker.setRole("MAKER");
            maker.setActive(true);
            userRepository.persist(maker);

            User checker = new User();
            checker.setUsername("checker");
            checker.setPassword(BCrypt.hashpw("checker123", BCrypt.gensalt(12)));
            checker.setFullName("Checker User");
            checker.setEmail("checker@rsud-soedono.go.id");
            checker.setRole("CHECKER");
            checker.setActive(true);
            userRepository.persist(checker);

            LOG.info("Default users seeded: admin/admin123, maker/maker123, checker/checker123");
        }
    }
}
