package thumbtack.buscompany;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
    public static int PORT;

    @Value("${port}")
    public void setPort(int port) {
        PORT = port;
    }
}
