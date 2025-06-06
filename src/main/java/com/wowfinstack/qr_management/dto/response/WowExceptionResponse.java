package com.wowfinstack.qr_management.dto.response;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class WowExceptionResponse {
    private Integer errorCode;
    private HttpStatus httpStatus;
    private String message;
    private String actualMessage;
}

