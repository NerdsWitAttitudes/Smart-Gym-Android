package com.nwa.smartgym.models;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by robin on 8-6-16.
 */
public class Buddy extends User {
    // Since we only want to store the buddy of the logged in user we can relate the buddy to a user
    @DatabaseField
    UUID buddy_of_user_id;

    // For ORMLite use
    Buddy(){};

    public Buddy(UUID id, String email, String firstName, String lastName, String country, DateTime dateOfBirth) {
        super(id, email, firstName, lastName, country, dateOfBirth);
    }

    public void setUser(UUID buddy_of_user_id) {
        this.buddy_of_user_id = buddy_of_user_id;
    }
}
