package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.model.Schedule;
import thumbtack.buscompany.validator.annototion.Dates;
import thumbtack.buscompany.validator.annototion.OnlyScheduleOrDates;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@OnlyScheduleOrDates
public class TripRequest {
    @NotEmpty
    private String busName;
    @NotEmpty
    private String fromStation;
    @NotEmpty
    private String toStation;
    @Pattern(regexp = "^(0\\d|1\\d|2[0-3]):[0-5]\\d$")
    private String start;
    @Pattern(regexp = "^(0\\d|1\\d|2[0-3]):[0-5]\\d$")
    private String duration;
    @Min(value = 1)
    private Long price;
    @Valid
    private Schedule schedule;
    @Dates
    private String[] dates;
}
