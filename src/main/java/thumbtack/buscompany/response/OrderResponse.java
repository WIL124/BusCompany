package thumbtack.buscompany.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.model.Passenger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private Integer tripId;
    private String fromStation;
    private String toStation;
    private String busName;
    private LocalDate date;
    private LocalTime start;
    private LocalTime duration;
    private Long price;
    private Long totalPrice;
    private List<Passenger> passengers;
}
