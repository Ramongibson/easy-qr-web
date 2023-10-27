package com.ramongibson.easyqr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ramongibson.easyqr.exception.QRCodeGenerationException;
import com.ramongibson.easyqr.model.QRCode;
import com.ramongibson.easyqr.model.User;
import com.ramongibson.easyqr.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@AllArgsConstructor
public class QRCodeGeneratorService {

    private final UserRepository userRepository;

    public QRCode generateQRCode(String url, int width, int height, String backgroundColor, String dotsColor) {
        try {
            log.debug("Generating QR code for URL: {}", url);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            Map<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0);

            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);


            Color bg = Color.decode(backgroundColor);
            Color dots = Color.decode(dotsColor);

            MatrixToImageConfig config = new MatrixToImageConfig(bg.getRGB(), dots.getRGB());

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);


            // qrImage = addLogoToQRCode(qrImage);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] qrCodeData = baos.toByteArray();

            String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeData);

            QRCode qrCode = new QRCode();
            qrCode.setUrl(url);
            qrCode.setQrCodeBase64(qrCodeBase64);

            log.debug("QR code generated successfully for URL: {}", url);
            return qrCode;
        } catch (Exception e) {
            log.error("Failed to generate QR code for URL: {}", url, e);
            throw new QRCodeGenerationException("Failed to generate QR code.", e);
        }
    }


//    private BufferedImage addLogoToQRCode(BufferedImage qrImage) {
//        try {
//
//            BufferedImage logoImage = ImageIO.read(getClass().getResourceAsStream("/path/to/your/logo.png"));
//
//
//            int centerX = (qrImage.getWidth() - logoImage.getWidth()) / 2;
//            int centerY = (qrImage.getHeight() - logoImage.getHeight()) / 2;
//
//            // Draw the logo on the QR code
//            Graphics2D graphics = qrImage.createGraphics();
//            graphics.drawImage(logoImage, centerX, centerY, null);
//            graphics.dispose();
//
//            return qrImage;
//        } catch (IOException e) {
//            log.error("Failed to add logo to QR code.", e);
//            return qrImage;
//        }
//    }

    @Transactional
    public void saveQRCodeToUser(String username, QRCode qrCode) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                log.error("User not found: {}", username);
                throw new UsernameNotFoundException("User not found");
            }

            List<QRCode> qrCodes = user.getQrCodes();
            qrCodes.add(qrCode);

            userRepository.save(user);
            log.info("QR code saved for user: {}", username);
        } catch (Exception e) {
            log.error("Error saving QR code for user: {}", username, e);
            throw e;
        }
    }
}
