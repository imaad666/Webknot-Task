package com.webknot.campus.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeService {
    
    @Value("${app.qr.base-url:https://campus-event-qr.vercel.app/checkin/}")
    private String qrBaseUrl;
    
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;
    
    /**
     * Generates a QR code for event check-in
     * @param qrToken Unique token for the registration
     * @return Base64 encoded QR code image
     */
    public String generateQRCode(String qrToken) {
        try {
            // Create the QR code content - URL that can be scanned for check-in
            String qrContent = qrBaseUrl + qrToken;
            
            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
            
            // Convert to PNG image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            // Encode to Base64
            byte[] qrCodeBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(qrCodeBytes);
            
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code for token: " + qrToken, e);
        }
    }
    
    /**
     * Generates QR code content without image encoding (for testing/debugging)
     * @param qrToken Unique token for the registration
     * @return QR code content string
     */
    public String getQRCodeContent(String qrToken) {
        return qrBaseUrl + qrToken;
    }
    
    /**
     * Validates if a QR token is in the correct format
     * @param qrToken Token to validate
     * @return true if valid format
     */
    public boolean isValidQRToken(String qrToken) {
        if (qrToken == null || qrToken.trim().isEmpty()) {
            return false;
        }
        
        // Expected format: eventId_studentId_timestamp
        // Example: WKUe120925_1WKU21CS001_1672531200
        String[] parts = qrToken.split("_");
        return parts.length == 3 && 
               parts[0].startsWith("WKUe") && 
               parts[1].startsWith("1WKU") &&
               parts[2].matches("\\d+");
    }
    
    /**
     * Extracts event ID from QR token
     * @param qrToken QR token
     * @return Event ID or null if invalid
     */
    public String getEventIdFromToken(String qrToken) {
        if (!isValidQRToken(qrToken)) {
            return null;
        }
        return qrToken.split("_")[0];
    }
    
    /**
     * Extracts student ID from QR token
     * @param qrToken QR token
     * @return Student ID or null if invalid
     */
    public String getStudentIdFromToken(String qrToken) {
        if (!isValidQRToken(qrToken)) {
            return null;
        }
        return qrToken.split("_")[1];
    }
    
    /**
     * Extracts timestamp from QR token
     * @param qrToken QR token
     * @return Timestamp or null if invalid
     */
    public Long getTimestampFromToken(String qrToken) {
        if (!isValidQRToken(qrToken)) {
            return null;
        }
        try {
            return Long.parseLong(qrToken.split("_")[2]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
