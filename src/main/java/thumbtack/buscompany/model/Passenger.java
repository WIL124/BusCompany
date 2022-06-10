package thumbtack.buscompany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @JsonIgnore
    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private Integer passport;

    public Passenger(String firstName, String lastName, Integer passport) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
    }
}
