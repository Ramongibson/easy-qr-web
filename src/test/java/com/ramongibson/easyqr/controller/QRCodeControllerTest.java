package com.ramongibson.easyqr.controller;

import com.ramongibson.easyqr.util.QRCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QRCodeControllerTest {

    @InjectMocks
    private QRCodeController qrCodeController;

    @Mock
    private QRCodeGenerator qrCodeGenerator;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowQRCodeForm() {

        String viewName = qrCodeController.showQRCodeForm();

        assertEquals("qrCode", viewName);
    }

    @Test
    public void testGenerateQRCode_ValidText() {
        // Arrange
        String text = "https://www.example.com";
        byte[] mockQRCodeData = "mockQRCodeData".getBytes();
        String expectedQRCodeBase64 = Base64.getEncoder().encodeToString(mockQRCodeData);

        when(qrCodeGenerator.generateQRCode(text, 200, 200)).thenReturn(mockQRCodeData);

        String viewName = qrCodeController.generateQRCode(text, model, redirectAttributes);

        assertEquals("qrCode", viewName);
        verify(model).addAttribute("qrCode", expectedQRCodeBase64);
        verifyNoInteractions(redirectAttributes);
    }

    @Test
    public void testGenerateQRCode_EmptyText() {
        String text = "";
        String expectedErrorMessage = "Please enter a valid URL.";

        String viewName = qrCodeController.generateQRCode(text, model, redirectAttributes);

        assertEquals("redirect:/generateQRCode", viewName);
        verify(redirectAttributes).addFlashAttribute("error", expectedErrorMessage);
        verifyNoInteractions(qrCodeGenerator);
        verifyNoInteractions(model);
    }
}
