package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Session {
    // REVU лучше так
    // private User user,
    // private String sessionId;
    // private Long lastActivityTime;
    // и все
    // UserType вообще лишнее, он есть в User
    // а вместо id лучше использовать класс модели
    // не стоит экспонировать реляционную модель БД в Java
    private Integer userId;
    private String sessionId;
    private Long lastActivityTime;
    private UserType userType;
}