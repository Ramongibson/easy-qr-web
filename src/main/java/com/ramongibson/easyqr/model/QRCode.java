package com.ramongibson.easyqr.model;

import lombok.Data;

@Data
public class QRCode {
    private String url;
    private String qrCodeBase64;
}
