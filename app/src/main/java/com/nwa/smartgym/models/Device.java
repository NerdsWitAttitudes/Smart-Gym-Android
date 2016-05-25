package com.nwa.smartgym.models;

/**
 * Created by robin on 24-5-16.
 */
public class Device {
    private String deviceAddress;
    private String name;

    public Device(String device_address, String name) {
        this.deviceAddress = device_address;
        this.name = name;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
