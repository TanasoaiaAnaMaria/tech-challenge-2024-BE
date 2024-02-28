package com.usv.Team.Finder.App.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {
    public static KeyPair generateRsaKey() {
        KeyPair keyPair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException();
        }
        return keyPair;
    }
}