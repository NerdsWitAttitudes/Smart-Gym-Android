package com.nwa.smartgym.models;


import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.UUID;

/**
 * Created by robin on 8-5-16.
 */
public class User {
    @DatabaseField(columnName = "_id", id = true, unique = true)
    private UUID id;

    private String password;

    @SerializedName("password_confirm")
    private String passwordConfirm;

    @DatabaseField(unique = true)
    private String email;

    @DatabaseField
    @SerializedName("first_name")
    private String firstName;

    @DatabaseField
    @SerializedName("last_name")
    private String lastName;

    @SerializedName("buddy_ids")
    private List<UUID> buddyIDs;

    private Boolean recommended = false;

    // For ORMLite use
    User(){}

    public User(String password, String passwordConfirm, String email, String firstName, String lastName, String country, DateTime dateOfBirth) {
        this(null, email, firstName, lastName, country, dateOfBirth, null);
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public User(UUID id, String email, String firstName, String lastName, String country, DateTime dateOfBirth, List<UUID> buddyIDs) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        String country1 = country;
        String dateOfBirth1 = formatDate(dateOfBirth);
        this.buddyIDs = buddyIDs;
    }
    private String formatDate(DateTime date) {
        // format string for serialization into a Python, UTC compatible date
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return date.toString(dateTimeFormatter);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {return firstName + " " + lastName;}

    public UUID getId() {return id;}

    public List<UUID> getBuddyIDs() {
        return buddyIDs;
    }

    public boolean getRecommended() { return recommended;}

    public void setRecommended(Boolean recommended) { this.recommended = recommended;}
}
