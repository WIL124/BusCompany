package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.validator.annototion.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotNull
    private Integer tripId;
    @Date
    @NotNull
    private String date;
    @NotNull
    @Size(min = 1)
    private List<@Valid @NotNull Passenger> passengers;
}
