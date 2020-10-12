package com.solomoon.mytriptracker.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    private static final String ENCRYPT_ALGORITHM = "SHA-256";

    public static String encryptSHA256(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = java.security.MessageDigest.getInstance(ENCRYPT_ALGORITHM);
        digest.update(data.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

        return hexString.toString();
    }
}
