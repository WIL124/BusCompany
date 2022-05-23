package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private User user;
    private String sessionId;
    private Long lastActivityTime;

    public Session(User user) {
        this.user = user;
        sessionId = UUID.randomUUID().toString();
        this.lastActivityTime = new Date().getTime();
    }
}