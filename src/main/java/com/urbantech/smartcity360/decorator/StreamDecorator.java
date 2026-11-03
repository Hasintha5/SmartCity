package com.urbantech.smartcity360.decorator;

public abstract class StreamDecorator implements DataStream {

    protected final DataStream wrapped;

    protected StreamDecorator(DataStream wrapped) {
        this.wrapped = wrapped;
    }

    protected abstract String layerName();

    protected abstract String transform(String data) throws Exception;

    @Override
    public String process(String data) {

        String inner = wrapped.process(data);

        try {
            return transform(inner);
        } catch (Exception e) {
            throw new IllegalStateException(layerName() + " layer failed", e);
        }
    }

    @Override
    public String describe() {
        return wrapped.describe() + " + " + layerName();
    }
}