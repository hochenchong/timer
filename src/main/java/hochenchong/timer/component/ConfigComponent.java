package hochenchong.timer.component;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConfigComponent {
    @Bean
    public String getDynamicCron() {
        // 读数据库，或者随机生成，这里就直接返回了
        return "1 1 1 1 1 ?";
    }
}
