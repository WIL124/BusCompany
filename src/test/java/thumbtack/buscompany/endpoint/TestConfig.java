package thumbtack.buscompany.endpoint;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import thumbtack.buscompany.AppProperties;

@TestConfiguration
public class TestConfig {
    @Bean
    public AppProperties appProperties(){
        return new AppProperties(8080,50,8,60);
    }
}
