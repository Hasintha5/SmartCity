package com.urbantech.smartcity360.decorator;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionDecorator extends StreamDecorator {

    private static final byte[] KEY = "SmartCity360Key!".getBytes(StandardCharsets.UTF_8);
    private static final SecureRandom RANDOM = new SecureRandom();

    public EncryptionDecorator(DataStream wrapped) {
        super(wrapped);
    }

    @Override
    protected String layerName() {
        return "Encryption";
    }

    @Override
    protected String transform(String data) throws Exception {

        byte[] iv = new byte[12];
        RANDOM.nextBytes(iv);


        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "AES"), new GCMParameterSpec(128, iv));


        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));


        byte[] output = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, output, 0, iv.length);
        System.arraycopy(encrypted, 0, output, iv.length, encrypted.length);


        return Base64.getEncoder().encodeToString(output);
    }
}