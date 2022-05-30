package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.request.ScheduleDto;
import thumbtack.buscompany.validator.annototion.Schedule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ScheduleValidator implements ConstraintValidator<Schedule, ScheduleDto> {

    @Override
    public boolean isValid(ScheduleDto value, ConstraintValidatorContext context) {
        try {
            if (value == null) return true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.d");
            LocalDate from = LocalDate.parse(value.getFromDate(), formatter);
            LocalDate to = LocalDate.parse(value.getToDate(), formatter);
            if (from.isAfter(to)) {
                return false;
            }
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
