package thumbtack.buscompany.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private int tripId;
    private String fromStation;
    private String toStation;
    private LocalTime start;
    private LocalTime duration;
    private BigDecimal price;
    private Bus bus;
    private boolean approved;
    private Schedule schedule;
    private List<TripDay> tripDays;
}
