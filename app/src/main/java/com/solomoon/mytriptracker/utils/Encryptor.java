package com.solomoon.mytriptracker.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public static String encryptSHA2(String data){
        String resultData = "";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA2");
            digest.update(data.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            resultData = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultData;
    }
}
