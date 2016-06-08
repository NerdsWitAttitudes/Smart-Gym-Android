package com.nwa.smartgym.models;

import org.joda.time.DateTime;

/**
 * Created by robin on 8-6-16.
 */
public class Buddy extends User {
    // Since we only want to store the buddy of the logged in user we can relate the buddy to a user
    User user;

    public Buddy(String email, String firstName, String lastName, String country, DateTime dateOfBirth) {
        super(email, firstName, lastName, country, dateOfBirth);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
