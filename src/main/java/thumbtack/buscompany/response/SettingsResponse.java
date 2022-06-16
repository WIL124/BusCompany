package thumbtack.buscompany.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SettingsResponse {
    private int maxNameLength;
    private int minPasswordLength;
}
