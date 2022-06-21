package thumbtack.buscompany.validator.impl;


import thumbtack.buscompany.validator.annototion.ValidName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class FioValidator implements ConstraintValidator<ValidName, String> {
    private String pattern;

    @Override
    public void initialize(ValidName constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            return Pattern.matches(pattern, s);
        } else return true;
    }
}
