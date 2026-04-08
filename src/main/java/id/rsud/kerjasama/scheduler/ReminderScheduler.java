package id.rsud.kerjasama.scheduler;

import id.rsud.kerjasama.service.EmailReminderService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReminderScheduler {

    private static final Logger LOG = Logger.getLogger(ReminderScheduler.class);

    @Inject
    EmailReminderService emailReminderService;

    /**
     * Runs every day at 08:00 to check for expiring partnerships
     * and send reminder emails.
     */
    @Scheduled(cron = "0 0 8 * * ?")
    void sendDailyReminders() {
        LOG.info("Starting daily reminder check...");
        emailReminderService.sendReminders();
        LOG.info("Daily reminder check completed.");
    }
}
