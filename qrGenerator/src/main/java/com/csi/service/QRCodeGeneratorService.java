package com.csi.service;
import com.csi.model.QRCodeData;
import org.springframework.stereotype.Service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.QRCode;
import com.google.zxing.common.BitMatrix;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;

@Service
public class QRCodeGeneratorService {
    public BufferedImage generateQRCodeImage(QRCodeData qrCodeData, int width, int height) throws Exception {
        String text = qrCodeData.getText();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        return image;
    }
}
