package com.wowfinstack.qr_management.dto.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QRRequest {
    @NotBlank(message = "QR Content should not be blank")
    @Schema(name = "content", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
}
