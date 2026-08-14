package ch.szclsb.test.jdbi.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JdbiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            ApiException.class
    })
    ResponseEntity<Object> handleConflict(ApiException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getStatus(), request);
    }
}
