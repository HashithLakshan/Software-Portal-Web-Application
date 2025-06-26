package Software.Portal.web.utill;


import Software.Portal.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CodeCleanupScheduler {

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 60000)
    public void clearExpiredCode() {
        userService.clearCodeIfExpired();
    }
}