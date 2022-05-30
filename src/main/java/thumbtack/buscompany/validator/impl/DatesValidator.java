package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.validator.annototion.Dates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DatesValidator implements ConstraintValidator<Dates, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy.MM.d"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
