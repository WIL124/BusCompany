package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.validator.annototion.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private String pattern;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value.startsWith("-") || value.endsWith("-")){
            return false;
        }
        String number = value.replaceAll("-", "");
        return Pattern.matches(pattern, number);
    }
}
