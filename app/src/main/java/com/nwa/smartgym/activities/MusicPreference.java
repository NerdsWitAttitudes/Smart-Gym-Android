package com.nwa.smartgym.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.UserAPIInterface;
import com.nwa.smartgym.lib.adapters.UserAdapter;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music_preferences);
        

    }

}

