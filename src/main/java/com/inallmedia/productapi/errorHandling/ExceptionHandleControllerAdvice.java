package com.inallmedia.productapi.errorHandling;

import com.inallmedia.productapi.exception.EntityNotFoundException;
import com.inallmedia.productapi.generated.model.ErrorDetails;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Hidden
public class ExceptionHandleControllerAdvice {

  @ExceptionHandler(BindException.class)
  public ResponseEntity<List<ErrorDetails>> handleBindException(BindException exception) {
    List<ErrorDetails> errors = exception.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> ErrorDetails.builder().detail(fieldError.getDefaultMessage())
                    .code(400)
                    .field(fieldError.getField())
                    .value(fieldError.getRejectedValue())
                    .location(ErrorDetails.LocationEnum.BODY).build()).toList();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<List<ErrorDetails>> handleConstraintViolationException(ConstraintViolationException ex) {
    List<ErrorDetails> errors = ex.getConstraintViolations().stream()
            .map(constraintViolation -> ErrorDetails.builder()
                    .field(constraintViolation.getPropertyPath().toString())
                    .value(constraintViolation.getInvalidValue())
                    .detail(constraintViolation.getMessage())
                    .code(400)
                    .location(ErrorDetails.LocationEnum.PATH)
                    .build()).toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorDetails.builder()
                    .detail(ex.getMessage())
                    .code(404)
                    .build());
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorDetails> handleGeneralExceptions(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorDetails.builder()
                    .detail(ex.getMessage())
                    .code(501)
                    .build()
    );
  }
}
