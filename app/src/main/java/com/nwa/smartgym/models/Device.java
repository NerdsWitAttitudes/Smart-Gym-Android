package com.nwa.smartgym.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

/**
 * Created by robin on 24-5-16.
 */
@DatabaseTable(tableName = "device")
public class Device {
    @DatabaseField(columnName = "_id", unique = true)
    private UUID id;

    @DatabaseField
    private String device_address;

    @DatabaseField
    private String name;

    // For ORMLite use
    Device(){};

    public Device(UUID id, String device_address, String name) {
        this.id = id;
        this.device_address = device_address;
        this.name = name;
    }

    public String getDeviceAddress() {
        return device_address;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.device_address = deviceAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
