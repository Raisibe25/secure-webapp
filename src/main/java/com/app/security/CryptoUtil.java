package com.app.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {
    private static final String KEY_ENV = "APP_AES_KEY";
    private static final SecretKey KEY;

    static {
        String raw = loadKeyRaw();
        byte[] keyBytes = decodeKey(raw);
        validateKeyLength(keyBytes);
        KEY = new SecretKeySpec(keyBytes, "AES");
        // Diagnostic - remove or change to proper logging in production
        System.out.println("CryptoUtil: APP_AES_KEY decoded length: " + keyBytes.length);
    }

    private static String loadKeyRaw() {
        String k = System.getProperty(KEY_ENV);
        String src = "system property";
        if (k == null || k.isBlank()) {
            k = System.getenv(KEY_ENV);
            src = "env var";
        }
        if (k == null || k.isBlank()) {
            String msg = "APP_AES_KEY missing from system property or environment";
            System.out.println("CryptoUtil: " + msg);
            throw new IllegalStateException(msg);
        }
        k = k.trim().replaceAll("^\"|\"$", "");
        String trimmed = k.length() <= 12 ? k : k.substring(0, 12) + "...";
        System.out.println("CryptoUtil: APP_AES_KEY source: " + src + " value (start): '" + trimmed + "'");
        return k;
    }

    private static byte[] decodeKey(String k) {
        try {
            if (k.contains("-") || k.contains("_")) {
                return Base64.getUrlDecoder().decode(k);
            } else {
                return Base64.getDecoder().decode(k);
            }
        } catch (IllegalArgumentException ex) {
            String msg = "APP_AES_KEY decode failed: " + ex.getMessage();
            System.out.println("CryptoUtil: " + msg);
            throw new IllegalArgumentException(msg, ex);
        }
    }

    private static void validateKeyLength(byte[] keyBytes) {
        int len = keyBytes.length;
        if (len != 16 && len != 24 && len != 32) {
            String msg = "APP_AES_KEY must decode to 16, 24, or 32 bytes but was " + len;
            System.out.println("CryptoUtil: " + msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public static String encrypt(String plaintext) throws Exception {
        if (plaintext == null) return null;
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, KEY, new GCMParameterSpec(128, iv));
        byte[] ct = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        ByteBuffer bb = ByteBuffer.allocate(iv.length + ct.length).put(iv).put(ct);
        return Base64.getEncoder().encodeToString(bb.array());
    }

    public static String decrypt(String b64) throws Exception {
        if (b64 == null) return null;
        byte[] in = Base64.getDecoder().decode(b64);
        if (in.length < 12) throw new IllegalArgumentException("ciphertext too short");
        byte[] iv = Arrays.copyOfRange(in, 0, 12);
        byte[] ct = Arrays.copyOfRange(in, 12, in.length);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, KEY, new GCMParameterSpec(128, iv));
        return new String(cipher.doFinal(ct), StandardCharsets.UTF_8);
    }
}