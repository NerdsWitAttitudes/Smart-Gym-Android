package com.nwa.smartgym.models;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalTime;

import java.util.UUID;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreference {

    @SerializedName("genre")
    private String genre;

    public MusicPreference(String genre) {
        this.genre = genre;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
