package ru.antoncharov.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestapiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CodeNotUniqueException.class)
    protected ResponseEntity<RestapiException> handleThereIsNoSuchUserException() {
        return new ResponseEntity<>(new RestapiException("Field CODE not unique"), HttpStatus.BAD_REQUEST);
    }

    private static class RestapiException {
        private String message;

        public RestapiException(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
