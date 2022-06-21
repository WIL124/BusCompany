package thumbtack.buscompany.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripDay {
    private Integer tripDayId;
    private LocalDate date;
    private Trip trip;
    // REVU нужно ли ? Зачем Вам все заказы на этот день ?
    // впрочем, если нужно - пусть будет
    private List<Order> orders;

    public TripDay(LocalDate date, Trip trip, List<Order> orders) {
        tripDayId = null;
        this.date = date;
        this.trip = trip;
        this.orders = orders;
    }
}
