package com.wowfinstack.qr_management.controller;

import com.wowfinstack.qr_management.dto.QRRequest;
import com.wowfinstack.qr_management.service.QRService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
public class QRController {
    private final QRService qrService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateQRCode(@Valid @RequestBody QRRequest qrRequest) {
        byte[] qrCode = qrService.generateQRCode(qrRequest.getContent());
        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(qrCode);
    }
}
