package thumbtack.buscompany.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripDay {
    private int tripDayId;
    private LocalDate date;
    private Trip trip;
    private List<Order> orders;

    public TripDay(LocalDate date, Trip trip, List<Order> orders) {
        tripDayId = 0;
        this.date = date;
        this.trip = trip;
        this.orders = orders;
    }
}
