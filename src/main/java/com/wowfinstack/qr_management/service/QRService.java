package com.wowfinstack.qr_management.service;

public interface QRService {
    byte[] generateQRCode(String qrCodeContent);
}
