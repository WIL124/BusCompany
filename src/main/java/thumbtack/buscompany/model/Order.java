package thumbtack.buscompany.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer orderId;
    private Client client;
    private TripDay tripDay;
    private List<Passenger> passengers;
}
