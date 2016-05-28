package com.nwa.smartgym.models;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.UUID;

public class SportSchedule {

    private UUID id;

    @SerializedName("user_id")
    private UUID userId;

    private String name;

    @SerializedName("reminder_minutes")
    private int reminderMinutes;

    @SerializedName("datetime")
    private DateTime dateTime;

    @SerializedName("is_active")
    private boolean isActive;


    public SportSchedule(UUID id, UUID userId, String name, int reminderMinutes, DateTime dateTime, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.reminderMinutes = reminderMinutes;
        this.dateTime = dateTime;
        this.isActive = isActive;
    }

    public SportSchedule() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(int reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
