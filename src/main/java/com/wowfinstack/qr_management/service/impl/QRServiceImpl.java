package com.wowfinstack.qr_management.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
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
import java.awt.geom.Ellipse2D;
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
            BufferedImage rawQrImage = MatrixToImageWriter.toBufferedImage(
                    matrix,
                    new MatrixToImageConfig(Color.BLACK.getRGB(), Color.WHITE.getRGB())
            );

            // Convert to ARGB to preserve colors when overlaying the logo
            BufferedImage qrImage = new BufferedImage(rawQrImage.getWidth(), rawQrImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = qrImage.createGraphics();
            g2d.drawImage(rawQrImage, 0, 0, null);
            g2d.dispose();

            // Now add the logo
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

        BufferedImage logoWithMargin = new BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = logoWithMargin.createGraphics();

        // Enable anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill the background with transparency
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, totalSize, totalSize);
        g.setComposite(AlphaComposite.SrcOver);

        // Draw a white circular background for the logo
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Float(0, 0, totalSize, totalSize));

        // Draw the resized logo in the center, keeping original colors
        g.setClip(new Ellipse2D.Float(margin, margin, logoSize, logoSize)); // Clip to circle
        g.drawImage(logo.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH), margin, margin, null);
        g.dispose();

        // centering the logo
        int centerX = (qrImage.getWidth() - logoSize) / 2;
        int centerY = (qrImage.getHeight() - logoSize) / 2;
        // Overlay the logo onto the QR code
        Graphics2D graphics = qrImage.createGraphics();
        graphics.drawImage(logoWithMargin,centerX , centerY, null);
        graphics.dispose();
        return qrImage;
    }
}
