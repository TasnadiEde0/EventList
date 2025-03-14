package edu.bbte.idde.teim2310.spring.controller;

import edu.bbte.idde.teim2310.spring.exception.IncorrectFullParameterException;
import edu.bbte.idde.teim2310.spring.exception.ProcessingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final Stream<String> handleConstraintViolation(ConstraintViolationException ex) {
        log.info("Constraint violation exception occurred", ex);

        return ex.getConstraintViolations().stream().map(it -> it.getPropertyPath().toString() + " " + it.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final Stream<String> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
        log.info("Constraint violation exception occurred", ex);

        return ex.getBindingResult().getFieldErrors().stream().map(it -> it.getField() + " " + it.getDefaultMessage());
    }

    @ExceptionHandler(ProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final String handleProcessingException(ProcessingException ex) {
        log.error("Processing exception occurred", ex);
        return "Internal Server Error: " + ex.getMessage();
    }

    @ExceptionHandler(IncorrectFullParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final String handleIncorrectFullParameterException(IncorrectFullParameterException ex) {
        log.error("Incorrect parameter", ex);
        return "Bad Request: Incorrect parameter";
    }




}
