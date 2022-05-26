package thumbtack.buscompany.validator.annototion;

import thumbtack.buscompany.validator.impl.OnlyScheduleOrDatesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OnlyScheduleOrDatesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyScheduleOrDates {
    String message() default "incorrect parameters of request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
