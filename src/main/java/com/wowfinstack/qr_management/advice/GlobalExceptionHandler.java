package com.wowfinstack.qr_management.advice;

import com.wowfinstack.qr_management.dto.response.WowExceptionResponse;
import com.wowfinstack.qr_management.exception.InvalidContentException;
import com.wowfinstack.qr_management.exception.LogoNotFound;
import com.wowfinstack.qr_management.exception.QrCodeGenerationException;
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

    @ExceptionHandler(InvalidContentException.class)
    public ResponseEntity<WowExceptionResponse> handleInvalidContentException(InvalidContentException ex) {
        WowExceptionResponse response = new WowExceptionResponse();
        response.setMessage("Invalid content");
        response.setActualMessage(ex.getMessage());
        response.setErrorCode(400);
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<WowExceptionResponse> handleQrCodeGenerationException(QrCodeGenerationException ex) {
        WowExceptionResponse response = new WowExceptionResponse();
        response.setMessage("Qr code generation failed");
        response.setActualMessage(ex.getMessage());
        response.setErrorCode(500);
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @ExceptionHandler(LogoNotFound.class)
    public ResponseEntity<WowExceptionResponse> handleLogoNotFound(LogoNotFound ex) {
        WowExceptionResponse wowExceptionResponse = new WowExceptionResponse();
        wowExceptionResponse.setMessage("Logo loading error");
        wowExceptionResponse.setActualMessage(ex.getMessage());
        wowExceptionResponse.setErrorCode(404);
        wowExceptionResponse.setHttpStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(wowExceptionResponse, HttpStatus.NOT_FOUND);
    }
}
