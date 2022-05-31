package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TripParams {
    private String fromStation;
    private String toStation;
    private String busName;
    private LocalDate fromDate;
    private LocalDate toDate;
}
