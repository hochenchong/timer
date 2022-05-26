package hochenchong.timer.cron;

import hochenchong.timer.util.SensitiveWordUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SensitiveWordScheduled {

    /**
     * 每隔 1 天重新加载一次屏蔽字文件
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void reloadSensitiveWord() {
        SensitiveWordUtils.init();
    }
}
