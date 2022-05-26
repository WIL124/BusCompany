package thumbtack.buscompany.validator.impl;

import thumbtack.buscompany.validator.annototion.MaxSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxSizeValidator implements ConstraintValidator<MaxSize, String> {
    private int max_name_length;

    @Override
    public void initialize(MaxSize constraintAnnotation) {
        this.max_name_length = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s !=null) {
            return s.length() <= max_name_length;
        }
        else return true;
    }
}
