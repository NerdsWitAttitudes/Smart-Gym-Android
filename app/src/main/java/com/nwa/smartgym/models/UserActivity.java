package com.nwa.smartgym.models;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.UUID;

public class UserActivity {

    private UUID id;

    @SerializedName("user_id")
    private UUID userId;

    @SerializedName("start_date")
    private DateTime startDate;

    @SerializedName("end_date")
    private DateTime endDate;

    @SerializedName("gym_id")
    private UUID gymId;

    public UserActivity() {
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

    public UUID getGymId() {
        return gymId;
    }

    public void setGymId(UUID gymId) {
        this.gymId = gymId;
    }
}
