package hochenchong.timer.cron;

import hochenchong.timer.component.ConfigComponent;
import hochenchong.timer.util.SensitiveWordUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SensitiveWordScheduled {

    /**
     * 每隔 1 天重新加载一次屏蔽字文件
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void reloadSensitiveWord() {
        SensitiveWordUtils.init();
    }

    // 测试从配置文件中读取配置
//    @Scheduled(cron = "${hochenchong.timer.scheduled.cron}")
//    public void readApplicationConfig() {
//        System.out.println(LocalDateTime.now() + "测试一下读取配置文件的定时任务");
//    }

    /**
     * 动态定时任务，从数据库读取或者随机生成
     *    服务启动时，读取一次 cron 表达式，之后不会刷新
     *    查看相关类 {@link ConfigComponent#getDynamicCron()}
     */
    @Scheduled(cron = "#{@getDynamicCron}")
    public void dynamicCron() {
        System.out.println(LocalDateTime.now() + "测试一下启动时动态读取 cron 的定时任务");
    }
}
