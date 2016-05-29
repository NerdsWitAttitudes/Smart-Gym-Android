package com.nwa.smartgym.models;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.UUID;

public class SportSchedule {

    private UUID id;

    @SerializedName("user_id")
    private UUID userId;

    private String name;

    @SerializedName("reminder_minutes")
    private int reminderMinutes;

    @SerializedName("time")
    private LocalTime dateTime;

    @SerializedName("weekdays")
    private List<LocalDate.Property> weekdays;

    @SerializedName("is_active")
    private boolean isActive;


    public SportSchedule(UUID id, UUID userId, String name, int reminderMinutes, LocalTime dateTime, List<LocalDate.Property> weekdays, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.reminderMinutes = reminderMinutes;
        this.dateTime = dateTime;
        this.weekdays = weekdays;
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

    public LocalTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<LocalDate.Property> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<LocalDate.Property> weekdays) {
        this.weekdays = weekdays;
    }
}
