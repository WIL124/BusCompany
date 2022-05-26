package thumbtack.buscompany.validator.annototion;

import thumbtack.buscompany.validator.impl.DatesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DatesValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dates {

    String message() default "Неверный формат дат";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
