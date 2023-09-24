package com.ramongibson.easyqr.controller;

import com.ramongibson.easyqr.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;

@Controller
public class QRCodeController {

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @GetMapping("/generateQRCode")
    public String showQRCodeForm() {
        return "qrCode";
    }

    @PostMapping("/generateQRCode")
    public String generateQRCode(@RequestParam("url") String url, Model model, RedirectAttributes redirectAttributes) {
        if (url.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please enter a valid URL.");
            return "redirect:/generateQRCode";
        }

        byte[] qrCodeData = qrCodeGenerator.generateQRCode(url, 200, 200);
        String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeData);

        model.addAttribute("qrCode", qrCodeBase64);
        return "qrCode";
    }
}
