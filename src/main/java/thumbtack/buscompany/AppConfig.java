package thumbtack.buscompany;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AppConfig {
    @Bean(name = "executor")
    public Executor executor() {
        return new ThreadPoolTaskExecutor();
    }
}
