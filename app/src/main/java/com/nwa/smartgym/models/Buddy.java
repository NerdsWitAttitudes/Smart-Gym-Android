package com.nwa.smartgym.models;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by robin on 8-6-16.
 */
public class Buddy extends User {
    // For ORMLite use
    Buddy(){};

    public Buddy(UUID id, String email, String firstName, String lastName, String country, DateTime dateOfBirth) {
        super(id, email, firstName, lastName, country, dateOfBirth);
    }
}
