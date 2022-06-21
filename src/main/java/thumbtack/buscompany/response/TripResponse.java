package thumbtack.buscompany.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.model.Schedule;
import thumbtack.buscompany.request.ScheduleDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponse {
    private Integer tripId;
    private String fromStation;
    private String toStation;
    private LocalTime start;
    private LocalTime duration;
    private BigDecimal price;
    private Bus bus;
    private Boolean approved;
    private Schedule schedule;
    private List<LocalDate> dates;
}
