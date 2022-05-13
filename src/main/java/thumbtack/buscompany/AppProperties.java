package thumbtack.buscompany;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
    public static int PORT;

    @Value("${server.port}")
    public void setPort(int port) {
        PORT = port;
    }
}
