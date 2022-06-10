package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDay {
    private Integer tripDayId;
    private LocalDate date;
    private Trip trip;
    private List<Order> orders;

    public TripDay(LocalDate date, Trip trip, List<Order> orders) {
        tripDayId = null;
        this.date = date;
        this.trip = trip;
        this.orders = orders;
    }
}
