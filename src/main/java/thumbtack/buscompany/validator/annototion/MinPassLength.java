package thumbtack.buscompany.validator.annototion;

import thumbtack.buscompany.validator.impl.MinPassLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinPassLengthValidator.class)
@Target( { ElementType. METHOD, ElementType. FIELD })
@Retention(RetentionPolicy. RUNTIME)
public @interface MinPassLength {
    int minLength() default 8;
    String message() default "{minLength} is min password length";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
