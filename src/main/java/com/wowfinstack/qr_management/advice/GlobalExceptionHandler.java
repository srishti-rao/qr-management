package com.wowfinstack.qr_management.advice;

import com.wowfinstack.qr_management.dto.response.WowExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WowExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        WowExceptionResponse wowExceptionResponse = new WowExceptionResponse();
        wowExceptionResponse.setMessage("Validation error");
        wowExceptionResponse.setActualMessage(errors.toString());
        wowExceptionResponse.setErrorCode(400);
        wowExceptionResponse.setHttpStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(wowExceptionResponse, wowExceptionResponse.getHttpStatus());
    }
}
