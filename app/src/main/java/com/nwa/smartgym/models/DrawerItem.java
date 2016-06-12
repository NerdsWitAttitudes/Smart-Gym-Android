package com.nwa.smartgym.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by robin on 30-5-16.
 */
public class DrawerItem {
    private Drawable icon;
    private String name;
    private Intent intent;

    public DrawerItem(Drawable icon, String name, Intent intent) {
        this.icon = icon;
        this.name = name;
        this.intent = intent;
    }

    public DrawerItem(Drawable icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void executeDrawerAction(Context context) {
        context.startActivity(this.intent);
    }
}
