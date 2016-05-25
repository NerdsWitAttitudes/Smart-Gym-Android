package com.nwa.smartgym.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.UUID;

public class SportSchedule implements JsonDeserializer<SportSchedule> {

    private UUID id;
    private UUID userId;
    private String name;
    private int reminderMinutes;

    public SportSchedule(UUID id, UUID userId, String name, int reminderMinutes) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.reminderMinutes = reminderMinutes;
    }

    public SportSchedule() {

    }

    @Override
    public String toString() {
        return "Sportschema: " + this.name + " reminder: " + this.reminderMinutes + " door: " + this.userId;
    }

    @Override
    public SportSchedule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement jsonElement = json.getAsJsonArray().get(0);

        return new Gson().fromJson(jsonElement, SportSchedule.class);
    }
}
