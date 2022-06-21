package thumbtack.buscompany.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private Client client;
    private TripDay tripDay;
    private List<Passenger> passengers;
}
