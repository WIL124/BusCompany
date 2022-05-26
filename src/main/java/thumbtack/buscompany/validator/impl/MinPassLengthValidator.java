package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.validator.annototion.MinPassLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinPassLengthValidator implements ConstraintValidator<MinPassLength, String> {
    private int min_password_length;

    @Override
    public void initialize(MinPassLength constraintAnnotation) {
        this.min_password_length = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            return s.length() >= min_password_length;
        } else return true;
    }
}
