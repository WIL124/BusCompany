package thumbtack.buscompany.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxSizeValidator.class)
@Target( { ElementType. METHOD, ElementType. FIELD })
@Retention(RetentionPolicy. RUNTIME)
public @interface MaxSize {
    int maxLength() default 50;
    String message() default "{maxLength} is max size";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
