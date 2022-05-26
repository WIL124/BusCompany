package thumbtack.buscompany.validator.annototion;

import thumbtack.buscompany.validator.impl.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target( { ElementType. METHOD, ElementType. FIELD })
@Retention(RetentionPolicy. RUNTIME)
public @interface Phone {
    String pattern() default "^((\\+7|8)+([0-9]){10})$";
    String message() default "incorrect phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
