package com.tstartup.tserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationCodeGenerator {

    public static String generateRegistrationCode(String deviceId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(deviceId.getBytes());
            return bytesToHex(hash).substring(0, 16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Example method to generate a device ID (you'll need to implement this)
    private static String generateDeviceId() {
        // Replace with actual device ID generation logic
        return "unique-device-id";
    }

    public static void main(String[] args) {
        String deviceId = "255845923995029";
        String registrationCode = generateRegistrationCode(deviceId);
        System.out.println("Registration Code: " + registrationCode);
    }
}
