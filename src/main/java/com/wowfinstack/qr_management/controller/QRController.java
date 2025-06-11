package com.wowfinstack.qr_management.controller;

import com.wowfinstack.qr_management.dto.request.QRRequest;
import com.wowfinstack.qr_management.service.QRService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
public class QRController {
    private final QRService qrService;

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@Valid @RequestBody QRRequest qrRequest) {
        byte[] qrCode = qrService.generateQRCode(qrRequest.getContent());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }
}
