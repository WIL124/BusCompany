package thumbtack.buscompany.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoosingPlaceResponse {
    private Integer orderId;
    private String ticket;
    private String lastName;
    private String firstName;
    private Integer passport;
    private Integer place;
}
