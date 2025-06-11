package com.wowfinstack.qr_management.controller;

import com.wowfinstack.qr_management.dto.request.QRRequest;
import com.wowfinstack.qr_management.service.QRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Tag(name = "QR", description = "APIs related to managing QR")
public class QRController {
    private final QRService qrService;

    @Operation(summary = "Generate QR", description = "Generates QR with company logo.")
    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@Valid @RequestBody QRRequest qrRequest) {
        byte[] qrCode = qrService.generateQRCode(qrRequest.getContent());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }
}
