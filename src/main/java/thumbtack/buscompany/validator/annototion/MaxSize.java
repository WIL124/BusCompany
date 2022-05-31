package thumbtack.buscompany.validator.annototion;

import thumbtack.buscompany.validator.impl.MaxSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxSizeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSize {
    String message() default "50 is max size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
