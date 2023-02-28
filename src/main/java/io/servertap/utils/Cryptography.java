package io.servertap.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptography {

    public static String encrypt(String toEncrypt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(toEncrypt.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashedText = new StringBuilder(no.toString(16));

            while (hashedText.length() < 32) {
                hashedText.insert(0, "0");
            }

            return hashedText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean check(String toCheck, String encryptedData) {
        String encryptedToCheck = encrypt(toCheck);

        return encryptedData.contentEquals(encryptedToCheck);
    }

}
