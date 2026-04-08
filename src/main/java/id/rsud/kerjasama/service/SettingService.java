package id.rsud.kerjasama.service;

import id.rsud.kerjasama.dto.SettingDto;
import id.rsud.kerjasama.entity.Setting;
import id.rsud.kerjasama.repository.SettingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SettingService {

    @Inject
    SettingRepository settingRepository;

    public List<Setting> findAll() {
        return settingRepository.listAll();
    }

    public Setting findById(Long id) {
        return settingRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Setting tidak ditemukan", Response.Status.NOT_FOUND));
    }

    public Optional<Setting> findByKey(String key) {
        return settingRepository.findByKey(key);
    }

    public String getValueByKey(String key, String defaultValue) {
        return settingRepository.findByKey(key)
                .map(Setting::getConfigValue)
                .orElse(defaultValue);
    }

    @Transactional
    public Setting create(SettingDto dto) {
        if (settingRepository.findByKey(dto.configKey()).isPresent()) {
            throw new WebApplicationException("Config key sudah ada", Response.Status.CONFLICT);
        }
        Setting setting = new Setting();
        setting.setConfigKey(dto.configKey());
        setting.setConfigValue(dto.configValue());
        settingRepository.persist(setting);
        return setting;
    }

    @Transactional
    public Setting update(Long id, SettingDto dto) {
        Setting setting = findById(id);
        setting.setConfigKey(dto.configKey());
        setting.setConfigValue(dto.configValue());
        settingRepository.persist(setting);
        return setting;
    }

    @Transactional
    public void delete(Long id) {
        Setting setting = findById(id);
        settingRepository.delete(setting);
    }
}
