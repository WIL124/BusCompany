package thumbtack.buscompany.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FioValidator.class)
@Target( { ElementType. METHOD, ElementType. FIELD })
@Retention(RetentionPolicy. RUNTIME)
public @interface Fio {
    String pattern() default "[а-яА-ЯёЁ -]+$";
    String message() default "Может содержать только русские буквы, пробел и знак \"минус\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}