package com.urbantech.smartcity360.decorator;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class CompressionDecorator extends StreamDecorator {

    public CompressionDecorator(DataStream wrapped) {
        super(wrapped);
    }

    @Override
    protected String layerName() {
        return "Compression";
    }

    @Override
    protected String transform(String data) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bytes)) {
            gzip.write(data.getBytes(StandardCharsets.UTF_8));
        }
        return Base64.getEncoder().encodeToString(bytes.toByteArray());
    }
}