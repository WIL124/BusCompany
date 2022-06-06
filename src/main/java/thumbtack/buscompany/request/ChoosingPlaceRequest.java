package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoosingPlaceRequest {
    @NotEmpty
    private Integer orderId;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private Integer passport;
    @NotEmpty
    private Integer place;
}
