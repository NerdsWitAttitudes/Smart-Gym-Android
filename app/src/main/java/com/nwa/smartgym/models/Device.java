package com.nwa.smartgym.models;

/**
 * Created by robin on 24-5-16.
 */
public class Device {
    private String MACAddress;
    private String name;

    public Device(String MACAddress, String name) {
        this.MACAddress = MACAddress;
        this.name = name;
    }
}
