package com.wowfinstack.qr_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QRRequest {
    @NotBlank(message = "QR Content should not be blank")
    private String content;
}
