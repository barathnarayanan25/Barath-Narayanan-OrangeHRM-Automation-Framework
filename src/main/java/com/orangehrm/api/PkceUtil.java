package com.orangehrm.api;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PkceUtil {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";

    private PkceUtil() {
    }

    public static String generateCodeVerifier() {

        SecureRandom random = new SecureRandom();
        StringBuilder verifier = new StringBuilder();

        for (int i = 0; i < 64; i++) {
            verifier.append(
                    CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))
            );
        }

        return verifier.toString();
    }

    public static String generateCodeChallenge(String codeVerifier) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public static String generateState() {
        return generateCodeVerifier();
    }
}