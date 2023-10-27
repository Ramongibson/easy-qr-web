package com.ramongibson.easyqr.controller;

import com.ramongibson.easyqr.model.QRCode;
import com.ramongibson.easyqr.service.QRCodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QRCodeController {

    private final QRCodeGeneratorService qrCodeGenerator;

    @GetMapping("/")
    public String redirectToShowQRCodeForm() {
        return "redirect:/generateQRCode";
    }

    @GetMapping("/generateQRCode")
    public String showQRCodeForm() {
        return "qr-code";
    }

    @PostMapping("/generateQRCode")
    public String generateQRCode(
            @RequestParam("url") String url,
            @RequestParam(value = "backgroundColor", required = false) String backgroundColor,
            @RequestParam(value = "dotsColor", required = false) String dotsColor,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            if (url.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please enter a valid URL.");
                return "redirect:/generateQRCode";
            }

            log.debug("Generating QR code with URL: {}", url);
            log.debug("Background Color: {}", backgroundColor);
            log.debug("Dots Color: {}", dotsColor);

            // Default colors if they are null or empty
            backgroundColor = StringUtils.defaultIfBlank(backgroundColor, "#ffffff");
            dotsColor = StringUtils.defaultIfBlank(dotsColor, "#000000");

            QRCode qrCode = qrCodeGenerator.generateQRCode(url, 400, 400, backgroundColor, dotsColor);

            model.addAttribute("qrCode", qrCode.getQrCodeBase64());
            model.addAttribute("url", qrCode.getUrl());

            if (userDetails != null) {
                qrCodeGenerator.saveQRCodeToUser(userDetails.getUsername(), qrCode);
            }

            return "qr-code";
        } catch (Exception e) {
            log.error("An error occurred while generating the QR code.", e);
            model.addAttribute("error", "An error occurred while generating the QR code.");
            return "qr-code";
        }
    }
}








