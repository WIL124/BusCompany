package thumbtack.buscompany.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientResponse extends UserResponse {
    private String email;
    private String phone;
}
