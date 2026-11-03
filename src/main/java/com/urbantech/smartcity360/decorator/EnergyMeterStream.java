package com.urbantech.smartcity360.decorator;

public class EnergyMeterStream implements DataStream {

    @Override
    public String process(String data) {

        return data;
    }

    @Override
    public String describe() {
        return "EnergyMeterStream";
    }
}