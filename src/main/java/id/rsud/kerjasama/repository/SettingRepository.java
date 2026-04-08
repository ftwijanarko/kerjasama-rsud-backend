package id.rsud.kerjasama.repository;

import id.rsud.kerjasama.entity.Setting;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class SettingRepository implements PanacheRepository<Setting> {

    public Optional<Setting> findByKey(String key) {
        return find("configKey", key).firstResultOptional();
    }
}
