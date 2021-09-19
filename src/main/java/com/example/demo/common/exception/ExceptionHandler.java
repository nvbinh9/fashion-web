package com.example.demo.common.exception;

import com.example.demo.common.exception.error.ErrorsMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

//    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
//    public ErrorsMessage errorMessage(Exception e, WebRequest request) {
//        return new ErrorsMessage(HttpStatus.BAD_REQUEST, "Error");
//    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsMessage handlerNotFoundIdException(NotFoundIdException exception, WebRequest webRequest) {
        return new ErrorsMessage(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
