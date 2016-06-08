package com.nwa.smartgym.models;


import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by robin on 8-5-16.
 */
public class User {
    private String password;

    @SerializedName("passwordConfirm")
    private String passwordConfirm;

    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String country;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    public User(String password, String passwordConfirm, String email, String firstName, String lastName, String country, DateTime dateOfBirth) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.dateOfBirth = formatDate(dateOfBirth);
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
}
