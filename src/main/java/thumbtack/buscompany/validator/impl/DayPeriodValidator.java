package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.model.Weekday;
import thumbtack.buscompany.validator.annototion.DayPeriod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DayPeriodValidator implements ConstraintValidator<DayPeriod, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        switch (value.toLowerCase()) {
            case ("daily"):
            case ("odd"):
            case ("even"):
                return true;
        }
        return isNumberOfDays(value) || isDayOfWeek(value);
    }

    private boolean isNumberOfDays(String str) {
        String[] array = str.trim().split(",");
        for (String day : array) {
            if (Pattern.matches("^(0[1-9]|[12]\\d|3[01])$", day)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDayOfWeek(String str) {
        String[] array = str.trim().split(",");
        for (String elem : array) {
            try {
                Weekday.valueOf(elem);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}

