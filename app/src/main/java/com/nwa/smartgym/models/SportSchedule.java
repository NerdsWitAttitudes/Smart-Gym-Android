package com.nwa.smartgym.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class SportSchedule {

    private UUID id;

    @SerializedName("user_id")
    private UUID userId;

    private String name;

    @SerializedName("reminder_minutes")
    private int reminderMinutes;

    public SportSchedule(UUID id, UUID userId, String name, int reminderMinutes) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.reminderMinutes = reminderMinutes;
    }

    public SportSchedule() {

    }
}
