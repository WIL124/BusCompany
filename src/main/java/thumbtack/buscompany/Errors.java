package thumbtack.buscompany;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
public class Errors {
    private final List<ApiErrors> errors = new ArrayList<>();
}
