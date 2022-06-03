package thumbtack.buscompany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "property")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppProperties {
    private int port;
    private int maxNameLength;
    private int minPasswordLength;
    private int userIdleTimeout;
}
