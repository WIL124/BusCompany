package thumbtack.buscompany.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import thumbtack.buscompany.exception.ApiErrors;
import thumbtack.buscompany.exception.Errors;
import thumbtack.buscompany.exception.ServerException;

@ControllerAdvice
@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Errors handleValidation(MethodArgumentNotValidException exc) {
        Errors errors = new Errors();
        exc.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.getErrors().add(new ApiErrors(exc.getClass().toString(), fieldError.getField(), fieldError.getDefaultMessage())));
        return errors;
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public Errors handleMissingRequestCookieException(MissingRequestCookieException exc) {
        Errors errors = new Errors();
        errors.getErrors().add(new ApiErrors(exc.getClass().toString(), exc.getCookieName(), "send JAVASESSIONID in cookie"));
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Errors handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) {
        Errors errors = new Errors();
        errors.getErrors().add(new ApiErrors(exc.getClass().getName(), "body", "Required request body is missing"));
        return errors;
    }

    @ExceptionHandler(ServerException.class)
    public Errors handleServerException(ServerException exc) {
        Errors errors = new Errors();
        errors.getErrors().add(new ApiErrors(exc.getErrorCode().name(), exc.getField(), exc.getErrorCode().getMessage()));
        return errors;
    }
}
