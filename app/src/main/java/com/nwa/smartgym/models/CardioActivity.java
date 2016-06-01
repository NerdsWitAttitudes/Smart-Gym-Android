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

    private double calories;

    public CardioActivity(UUID id, UUID activityId, DateTime startDate, DateTime endDate, double calories) {
        this.id = id;
        this.activityId = activityId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.calories = calories;
    }

    public CardioActivity(UUID activityId) {
        this.activityId = activityId;
    }
}
