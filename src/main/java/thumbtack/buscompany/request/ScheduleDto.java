package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validator.annototion.Dates;
import thumbtack.buscompany.validator.annototion.DayPeriod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    @Dates
    private String fromDate;
    @Dates
    private String toDate;
    @DayPeriod
    private String period;
}
