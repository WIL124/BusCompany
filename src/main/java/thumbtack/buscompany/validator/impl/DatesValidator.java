package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.validator.annototion.Dates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DatesValidator implements ConstraintValidator<Dates, String[]> {

    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        for (String s : value) {
            try {
                LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                return false;
            }
        }
        return true;
    }
}
