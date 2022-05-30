package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validator.annototion.DayPeriod;
import thumbtack.buscompany.validator.annototion.Schedule;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schedule
public class ScheduleDto {
    private String fromDate;
    private String toDate;
    @DayPeriod
    private String period;
}
