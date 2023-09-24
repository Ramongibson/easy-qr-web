package com.ramongibson.easyqr.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class QRCodeGeneratorTest {

    private QRCodeGenerator qrCodeGenerator;

    @BeforeEach
    public void setUp() {
        qrCodeGenerator = new QRCodeGenerator();
    }

    @Test
    public void testGenerateQRCodeValidUrl() {
        String validUrl = "https://www.google.com";
        int width = 200;
        int height = 200;

        byte[] qrCode = qrCodeGenerator.generateQRCode(validUrl, width, height);

        assertNotNull(qrCode);
        assertTrue(qrCode.length > 0);
    }

    @Test
    public void testGenerateQRCodeInvalidUrl() {
        String invalidUrl = "not_a_valid_url";
        int width = 200;
        int height = 200;

        assertThrows(IllegalArgumentException.class, () -> {
            qrCodeGenerator.generateQRCode(invalidUrl, width, height);
        });
    }

    @Test
    public void testGenerateQRCodeWithEmptyUrl() {
        String emptyUrl = "";
        int width = 200;
        int height = 200;

        assertThrows(IllegalArgumentException.class, () -> {
            qrCodeGenerator.generateQRCode(emptyUrl, width, height);
        });
    }

    @Test
    public void testGenerateQRCodeWithNullUrl() {
        String nullUrl = null;
        int width = 200;
        int height = 200;

        assertThrows(IllegalArgumentException.class, () -> {
            qrCodeGenerator.generateQRCode(nullUrl, width, height);
        });
    }
}
