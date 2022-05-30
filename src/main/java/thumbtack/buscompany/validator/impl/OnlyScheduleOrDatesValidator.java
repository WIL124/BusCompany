package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.validator.annototion.OnlyScheduleOrDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyScheduleOrDatesValidator implements ConstraintValidator<OnlyScheduleOrDates, TripRequest> {
    @Override
    public boolean isValid(TripRequest request, ConstraintValidatorContext context) {
        if ((request.getDates() == null) == (request.getScheduleDto() == null)) {
            return false;
        }
        if (request.getDates() != null){
            return !request.getDates().isEmpty();
        }
        return true;
    }
}
