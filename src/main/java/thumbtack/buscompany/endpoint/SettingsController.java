package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.AppProperties;
import thumbtack.buscompany.response.SettingsResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/settings")
public class SettingsController {
    AppProperties appProperties;

    @GetMapping
    public SettingsResponse get(@CookieValue(value = "JAVASESSIONID", required = false) String sessionId) {
        return new SettingsResponse(appProperties.getMaxNameLength(), appProperties.getMinPasswordLength());
    }
}
