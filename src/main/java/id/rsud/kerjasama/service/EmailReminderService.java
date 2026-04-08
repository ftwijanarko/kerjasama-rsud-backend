package id.rsud.kerjasama.service;

import id.rsud.kerjasama.entity.Kerjasama;
import id.rsud.kerjasama.entity.Mitra;
import id.rsud.kerjasama.repository.KerjasamaRepository;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class EmailReminderService {

    private static final Logger LOG = Logger.getLogger(EmailReminderService.class);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    @Inject
    Mailer mailer;

    @Inject
    KerjasamaRepository kerjasamaRepository;

    @Inject
    SettingService settingService;

    @Transactional
    public void sendReminders() {
        int reminderDays = Integer.parseInt(
                settingService.getValueByKey("reminder_days_before", "90"));

        LocalDate reminderDate = LocalDate.now().plusDays(reminderDays);
        List<Kerjasama> expiringList = kerjasamaRepository.findExpiringBefore(reminderDate);

        LOG.infof("Found %d kerjasama expiring before %s", expiringList.size(), reminderDate);

        for (Kerjasama k : expiringList) {
            try {
                sendReminderEmail(k);
                k.setLastReminderSent(LocalDate.now());
                kerjasamaRepository.persist(k);
                LOG.infof("Reminder sent for kerjasama id=%d, judul=%s", k.getId(), k.getJudulKerjasama());
            } catch (Exception e) {
                LOG.errorf("Failed to send reminder for kerjasama id=%d: %s", k.getId(), e.getMessage());
            }
        }
    }

    private void sendReminderEmail(Kerjasama k) {
        Mitra mitra1 = k.getMitra1();
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), k.getTanggalSelesai());

        String subject = String.format("[REMINDER] Kerjasama akan berakhir dalam %d hari - %s",
                daysLeft, k.getJudulKerjasama());

        String body = String.format("""
                Yth. %s,
                
                Dengan ini kami menginformasikan bahwa perjanjian kerjasama berikut akan segera berakhir:
                
                Judul Kerjasama : %s
                Tipe            : %s
                Tanggal Mulai   : %s
                Tanggal Selesai : %s
                Sisa Waktu      : %d hari
                
                Mohon segera melakukan tindak lanjut untuk perpanjangan kerjasama.
                
                Hormat kami,
                RSUD dr. Soedono Madiun
                """,
                mitra1.getNamaMitra(),
                k.getJudulKerjasama(),
                k.getTipeKerjasama(),
                k.getTanggalMulai().format(DATE_FMT),
                k.getTanggalSelesai().format(DATE_FMT),
                daysLeft
        );

        // Send to mitra 1
        if (mitra1.getEmailMitra() != null && !mitra1.getEmailMitra().isBlank()) {
            mailer.send(Mail.withText(mitra1.getEmailMitra(), subject, body));
        }

        // Also send to mitra 2 if exists
        if (k.getMitra2() != null && k.getMitra2().getEmailMitra() != null
                && !k.getMitra2().getEmailMitra().isBlank()) {
            mailer.send(Mail.withText(k.getMitra2().getEmailMitra(), subject, body));
        }
    }
}
