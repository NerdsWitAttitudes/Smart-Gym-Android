package com.nwa.smartgym.models;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.UUID;

public class CardioActivity {

    private UUID id;

    @SerializedName("activity_id")
    private UUID activityId;

    @SerializedName("start_date")
    private DateTime startDate;

    @SerializedName("end_date")
    private DateTime endDate;

    private Double calories;
    private Double ditsance;
    private Double speed;

    @SerializedName("is_active")
    private boolean active;

    public CardioActivity(UUID id, UUID activityId, DateTime startDate, DateTime endDate, Double calories, Double ditsance, Double speed, boolean active) {
        this.id = id;
        this.activityId = activityId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.calories = calories;
        this.ditsance = ditsance;
        this.speed = speed;
        this.active = active;
    }

    public CardioActivity(UUID activityId) {
        this.activityId = activityId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getActivityId() {
        return activityId;
    }

    public void setActivityId(UUID activityId) {
        this.activityId = activityId;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getDitsance() {
        return ditsance;
    }

    public void setDitsance(Double ditsance) {
        this.ditsance = ditsance;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}