package thumbtack.buscompany.validator.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import thumbtack.buscompany.AppProperties;
import thumbtack.buscompany.validator.annototion.MinPassLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class MinPassLengthValidator implements ConstraintValidator<MinPassLength, String> {
    private AppProperties properties;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            return s.length() >= properties.getMinPasswordLength();
        } else return true;
    }
}
