package com.nwa.smartgym.models;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by robin on 8-5-16.
 */
public class User {
    private String password;
    private String password_confirm;
    private String email;
    private String first_name;
    private String last_name;
    private String country;
    private String date_of_birth;

    public User(String password, String password_confirm, String email, String firstname, String lastname, String country, DateTime dateOfBirth) {
        this.password = password;
        this.password_confirm = password_confirm;
        this.email = email;
        this.first_name = firstname;
        this.last_name = lastname;
        this.country = country;
        this.date_of_birth = formatDate(dateOfBirth);
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
