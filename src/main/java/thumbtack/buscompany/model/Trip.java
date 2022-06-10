package thumbtack.buscompany.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private Integer tripId;
    private String fromStation;
    private String toStation;
    private LocalTime start;
    private LocalTime duration;
    private Long price;
    private Bus bus;
    private Boolean approved;
    private Schedule schedule;
    private List<TripDay> tripDays;
}
