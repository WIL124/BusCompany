package thumbtack.buscompany.validator.impl;

import org.springframework.beans.factory.annotation.Value;
import thumbtack.buscompany.validator.annototion.MinPassLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinPassLengthValidator implements ConstraintValidator<MinPassLength, String> {
    private final int minPasswordLength;

    MinPassLengthValidator(@Value("${property.min_password_length}") int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            return s.length() >= minPasswordLength;
        } else return true;
    }
}
