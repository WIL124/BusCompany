package thumbtack.buscompany.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import thumbtack.buscompany.ApiErrors;
import thumbtack.buscompany.Errors;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Errors handleValidation(MethodArgumentNotValidException exc) {
        Errors errors = new Errors();
        exc.getBindingResult().getFieldErrors().forEach(fieldError -> errors.getErrors().add(new ApiErrors(exc.getClass().toString(), fieldError.getField(), fieldError.getDefaultMessage())));
        return errors;
    }
}
