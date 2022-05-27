package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.validator.annototion.OnlyScheduleOrDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeParseException;

public class OnlyScheduleOrDatesValidator implements ConstraintValidator<OnlyScheduleOrDates, TripRequest> {
    @Override
    public boolean isValid(TripRequest request, ConstraintValidatorContext context) {
        if ((request.getDates() == null) == (request.getScheduleDto() == null)) {
            return false;
        }
        try {
        } catch (DateTimeParseException ex) {
            return false;
        }

        return true;
    }
}
