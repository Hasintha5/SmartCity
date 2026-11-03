package com.urbantech.smartcity360.decorator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class AuthenticationDecorator extends StreamDecorator {

    private static final byte[] KEY = "SmartCity360AuthKey".getBytes(StandardCharsets.UTF_8);

    public AuthenticationDecorator(DataStream wrapped) {
        super(wrapped);
    }

    @Override
    protected String layerName() {
        return "Authentication";
    }

    @Override
    protected String transform(String data) throws Exception {

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(KEY, "HmacSHA256"));


        byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));


        return data + ".sig=" + HexFormat.of().formatHex(signature);
    }
}