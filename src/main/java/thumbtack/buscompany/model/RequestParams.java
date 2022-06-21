package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestParams {
    private String fromStation;
    private String toStation;
    private String busName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer clientId;
}
