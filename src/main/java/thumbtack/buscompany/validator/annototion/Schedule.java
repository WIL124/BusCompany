package thumbtack.buscompany.validator.annototion;

import thumbtack.buscompany.validator.impl.ScheduleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ScheduleValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {

    String message() default "Неверный формат расписания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
