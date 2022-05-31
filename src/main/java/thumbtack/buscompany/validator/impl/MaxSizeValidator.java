package thumbtack.buscompany.validator.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thumbtack.buscompany.validator.annototion.MaxSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class MaxSizeValidator implements ConstraintValidator<MaxSize, String> {
    private final int maxNameLength;

    MaxSizeValidator(@Value("${property.max_name_length}") int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            return s.length() <= maxNameLength;
        } else return true;
    }
}
