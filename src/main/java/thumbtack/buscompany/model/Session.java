package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Session {
    private Integer userId;
    private String sessionId;
    private Long lastActivityTime;
    private UserType userType;
}