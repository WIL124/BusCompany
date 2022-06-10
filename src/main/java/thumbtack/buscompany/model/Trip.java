package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Data
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
