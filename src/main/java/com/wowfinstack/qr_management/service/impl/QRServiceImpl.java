package com.wowfinstack.qr_management.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wowfinstack.qr_management.exception.InvalidContentException;
import com.wowfinstack.qr_management.exception.LogoNotFound;
import com.wowfinstack.qr_management.exception.QrCodeGenerationException;
import com.wowfinstack.qr_management.service.QRService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRServiceImpl implements QRService {
    @Override
    public byte[] generateQRCode(String content) {
        if(content == null || content.trim().isEmpty()) {
            throw new InvalidContentException("Content cannot be null or empty");
        }
        try {
            QRCodeWriter writer = new QRCodeWriter();
            int dimension = 300;
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction for embedded logo
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, dimension, dimension, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
            qrImage = addLogoToQr(qrImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            return baos.toByteArray();

        }
        catch(WriterException | IOException e) {
            throw new QrCodeGenerationException("Error while generating QR Code", e);
        }
    }

    private BufferedImage addLogoToQr(BufferedImage qrImage) throws IOException {
        InputStream logoStream = getClass().getClassLoader().getResourceAsStream("company_logo.png");
        if(logoStream == null) {
            throw new LogoNotFound("Logo not found");
        }
        int logoSize = qrImage.getHeight() / 5;
        BufferedImage logo = ImageIO.read(logoStream);
        int margin = logoSize / 10;
        int totalSize = logoSize + margin * 2;
        BufferedImage bufferedImage = new BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D gl = bufferedImage.createGraphics();
        gl.drawImage(bufferedImage, 63, 63, margin, margin, null);
        gl.dispose();
        // centering the logo
        int centerX = (qrImage.getWidth() - logoSize) / 2;
        int centerY = (qrImage.getHeight() - logoSize) / 2;

        Graphics2D g = qrImage.createGraphics();
        //g.drawImage(bufferedImage, 63, 63, margin, margin, null);
        g.drawImage(logo.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH), centerX, centerY, null);
        g.dispose();
        return qrImage;
    }
}
