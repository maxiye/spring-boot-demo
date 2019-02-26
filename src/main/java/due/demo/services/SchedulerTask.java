package due.demo.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerTask {
    private int count = 0;
    @Scheduled(cron = "0 */1 * * * ?")
    private void task() {
        System.out.println("Schedule task :" + count++);
    }
}
